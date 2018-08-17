package com.aws.codestar.workers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.aws.codestar.projecttemplates.api.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Worker service that appends {@link ChatMessage}s to the chat file.
 * 
 * @author jfoley
 */
public class ChatWorker implements Runnable{
	
	private final String chatFilePath;
	
	private final BlockingQueue<ChatMessage> queue;
	
	private final ExecutorService executorSvc;
	
	public ChatWorker(BlockingQueue<ChatMessage> queue, String chatFilePath) {
		this.queue = queue;
		this.executorSvc = Executors.newSingleThreadExecutor();
		this.chatFilePath = chatFilePath;
	}
	
	/**
	 * Checks for the chat file and creates if it does not exist, then starts the service
	 * 
	 * @throws IOException if the chatfile cannot be created
	 */
	@PostConstruct
	public void doPostConstruct() throws IOException {
		//Checks if file exists and creates it if it doesn't
		new File(chatFilePath).createNewFile();
		
		//Start the worker
		executorSvc.execute(this);
		
	}
	
	/**
	 * Attempts to complete in-progress task before shutting down the service
	 */
	@PreDestroy
	public void doPreDestroy() {
		executorSvc.shutdown();
		try {
			if (!executorSvc.awaitTermination(5, TimeUnit.SECONDS)) {
				executorSvc.shutdownNow();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			executorSvc.shutdownNow();
		}
	}

	@Override
	public void run() {
		while(true) {
			
			//Grab message from queue
			ChatMessage msg;
			try {
				msg = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			
			//Set the date time 
			msg.setDateTime(Instant.now().toString());
			
			//Convert to string
			ObjectMapper mapper = new ObjectMapper();
			String json;
			try {
				json = mapper.writeValueAsString(msg);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return;
			}
			
			//Write to file
			try(
				BufferedWriter out = new BufferedWriter(
				    new OutputStreamWriter(
				        new FileOutputStream(chatFilePath, true), //true to append
				        StandardCharsets.UTF_8                  
				    )
				)
			)
			{
				out.write(json);
				out.newLine();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
	}
	
	

}
