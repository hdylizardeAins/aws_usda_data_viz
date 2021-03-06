package com.aws.codestar.workers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


import com.aws.codestar.projecttemplates.api.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Worker service that appends and reads from {@link ChatMessage}s the chat
 * file.
 * 
 * @author jfoley
 */
public class ChatWorker implements Runnable {

	private final String chatFilePath;

	private final BlockingQueue<ChatMessage> queue;

	private final ExecutorService executorSvc;

	private ReentrantLock lock;
	
	private boolean shuttingDown = false;

	public ChatWorker(BlockingQueue<ChatMessage> queue, String chatFilePath) {
		this.queue = queue;
		this.executorSvc = Executors.newSingleThreadExecutor();
		this.chatFilePath = chatFilePath;
		this.lock = new ReentrantLock();
	}

	/**
	 * Starts the service
	 */
	@PostConstruct
	public void doPostConstruct() throws IOException {
		// Start the worker
		executorSvc.execute(this);

	}

	/**
	 * Attempts to complete in-progress task before shutting down the service
	 */
	@PreDestroy
	public void doPreDestroy() {
		executorSvc.shutdown();
		this.shuttingDown = true;
		try {
			if (!executorSvc.awaitTermination(5, TimeUnit.SECONDS)) {
				executorSvc.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorSvc.shutdownNow();
		}
	}

	@Override
	public void run() {
		while (!shuttingDown) {

			// Grab message from queue
			ChatMessage msg;
			try {
				msg = queue.poll(100, TimeUnit.MILLISECONDS);
				if (msg == null) continue;
				List<ChatMessage> messages = new LinkedList<>();
				messages.add(msg);
				writeToFile(messages);
				messages.forEach(m -> m.setWritten(true));
			} catch (InterruptedException e) {

			}

		}
		List<ChatMessage> messages = new LinkedList<>();
		queue.drainTo(messages);
		writeToFile(messages);

	}

	private void writeToFile(List<ChatMessage> messages) {
		lock.lock();
		
		try {
			List<String> messageStrings = new LinkedList<>();
			for (ChatMessage msg : messages) {
				// Set the date time
				msg.setDateTime(Instant.now().toString());
				
				//Set the id
				msg.setId(UUID.randomUUID().toString());
				
				// Convert to string
				ObjectMapper mapper = new ObjectMapper();
				String json = null;
				try {
					json = mapper.writeValueAsString(msg);
					messageStrings.add(json);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			}

			// Write to file
			try (BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(chatFilePath, true), // true to append
							StandardCharsets.UTF_8))) {
				if (!messageStrings.isEmpty()) {
					for (String json : messageStrings) {
						out.write(json);
						out.newLine();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			lock.unlock();
		}
	}

	public List<ChatMessage> read() {
		List<ChatMessage> content = new LinkedList<>();
		lock.lock();
		try {
			try (Scanner scan = new Scanner(new File(chatFilePath))) {
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					ObjectMapper mapper = new ObjectMapper();
					ChatMessage json = null;
					try {
						json = mapper.readValue(line, ChatMessage.class);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}

					content.add(json);

				}
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		} finally {
			lock.unlock();
		}
		return content;
	}

	/**
	 * Puts a {@link ChatMessage} into the queue of pending writes.
	 * 
	 * @param msg the message to put
	 * @throws InterruptedException if interrupted while waiting to put
	 */
	public void put(ChatMessage msg) throws InterruptedException {
		queue.put(msg);
	}

}
