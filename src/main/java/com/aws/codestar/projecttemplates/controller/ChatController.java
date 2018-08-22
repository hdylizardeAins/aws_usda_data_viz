package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aws.codestar.projecttemplates.ApplicationProperties;
import com.aws.codestar.projecttemplates.api.ChatMessage;
import com.aws.codestar.workers.ChatWorker;

/**
 * Web service handling requests to the shared chat
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

	private static final String APPLICATION_JSON = "application/json";

	@Autowired
	private ApplicationProperties properties;

	@Autowired
	private ChatWorker chatWorker;

	@PostConstruct
	public void doPostConstruct() throws IOException {
		// Check if social folder exists and create if it doesn't
		File socialDir = new File(properties.getSocialDir());
		if (!socialDir.exists()) {
			socialDir.mkdir();
		}

		// Same for chat file
		new File(properties.getSocialDir() + properties.getChatFileName()).createNewFile();
	}

	@RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON)
	public ResponseEntity write(@RequestBody ChatMessage message) throws InterruptedException {
		chatWorker.put(message);
		//HACK: block until the write completes by waiting for the written field to be set on the message
		long time = System.currentTimeMillis();
		long timeout = time + 10000L; //10 second timeout
		while (System.currentTimeMillis() < timeout) {
			if (message.getWritten()) {
				break;
			}
			Thread.sleep(100L);
		}
		if (message.getWritten()) {
			return ResponseEntity.ok(message);
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
	public ResponseEntity read() throws IOException {
		List<ChatMessage> content = chatWorker.read();
		return ResponseEntity.ok(content);
	}

}
