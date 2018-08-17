package com.aws.codestar.projecttemplates;

import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.aws.codestar.projecttemplates.api.ChatMessage;
import com.aws.codestar.workers.ChatWorker;

/**
 * Simple class to start up the application.
 *
 * @SpringBootApplication adds:
 * @Configuration
 * @EnableAutoConfiguration
 * @ComponentScan
 */
@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class Application extends SpringBootServletInitializer {

	@Autowired
	ApplicationProperties applicationProperties;

	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ChatWorker chatWorker() {
		return new ChatWorker(new LinkedBlockingQueue<ChatMessage>(),
				applicationProperties.getSocialDir() + applicationProperties.getChatFileName());
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
