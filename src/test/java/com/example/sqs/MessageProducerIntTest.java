package com.example.sqs;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.config.FileConfig;
import com.example.dto.MessageDto;
import com.example.sqs.MessageProducer;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("int")
public class MessageProducerIntTest {

	private static Logger logger = LoggerFactory.getLogger(MessageProducerIntTest.class);

	@Autowired
	private MessageProducer messageProducer;

	@Autowired
	private FileConfig fileConfig;

	private File file;

	@Before
	public void setup() throws IOException {

		logger.info("setup()");

		file = new File(fileConfig.getFilePath() + fileConfig.getFileName());
		file.createNewFile();

	}

	@Test
	public void sendMessageTest() throws IOException {

		logger.info("sendMessageTest()");

		MessageDto message = new MessageDto();
		message.setMessage("test message from integration environment");

		messageProducer.sendMessage(message);

		List<String> messagesList = new ArrayList<>();
		messagesList = Files.lines(Paths.get(fileConfig.getFilePath() + fileConfig.getFileName()))
				.collect(Collectors.toList());

		assertThat(messagesList.contains(message.getMessage()));
	}

	@After
	public void tearDown() {

		logger.info("tearDown()");

		if (file != null) {
			file.delete();
		}

	}
}
