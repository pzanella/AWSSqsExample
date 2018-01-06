package com.example.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;

import com.example.config.FileConfig;
import com.example.dto.MessageDto;

@Component
@Profile("dev")
@TestPropertySource(locations = "classpath:application-dev.yml")
public class MessageConsumerDev {

	private static Logger logger = LoggerFactory.getLogger(MessageConsumerDev.class);

	@Autowired
	private FileConfig fileConfig;

	@JmsListener(destination = "${sqs.name}", containerFactory = "jmsListenerDev")
	public void consumeMessage(MessageDto message) {

		logger.debug("consumeMessage()");

		logger.info("Received => " + message.getMessage());

		writeFile(message);

	}

	private void writeFile(MessageDto receivedMessage) {

		logger.info("writeFile()");

		try {
			Files.write(Paths.get(fileConfig.getFilePath() + fileConfig.getFileName()),
					receivedMessage.getMessage().getBytes());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
