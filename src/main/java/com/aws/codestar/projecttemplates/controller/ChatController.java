package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aws.codestar.projecttemplates.ApplicationProperties;
import com.aws.codestar.projecttemplates.api.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		private BlockingQueue<ChatMessage> chatQueue;
		
		@RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON)
		public ResponseEntity write(@RequestBody ChatMessage message) throws InterruptedException  {			
			chatQueue.put(message);			
			return ResponseEntity.ok().build();
		}
		
		@RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON)
		public ResponseEntity read() throws IOException {
			List<ChatMessage> content = new LinkedList<>();
			try(Scanner scan = new Scanner(new File(properties.getChatFilePath()))){
				while(scan.hasNextLine()) {
					String line = scan.nextLine();
					ObjectMapper mapper = new ObjectMapper();
					ChatMessage json = mapper.readValue(line, ChatMessage.class);
					content.add(json);
				}
			}
			return ResponseEntity.ok(content);
		}

	}

