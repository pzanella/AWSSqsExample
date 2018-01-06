package com.example.sqs;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticmq.NodeAddress;
import org.elasticmq.rest.sqs.SQSRestServer;
import org.elasticmq.rest.sqs.SQSRestServerBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.example.config.FileConfig;
import com.example.config.SQSConfig;
import com.example.dto.MessageDto;
import com.example.sqs.MessageProducer;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@TestPropertySource(locations = "classpath:application-dev.yml")
public class MessageProducerDevTest {

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(MessageProducerDevTest.class);

	private AmazonSQS amazonSQSClient;

	@Autowired
	private MessageProducer messageProducer;

	@Autowired
	private SQSConfig sqsConfig;

	@Autowired
	private FileConfig fileConfig;

	private String queueUrl;

	private SQSRestServer server;

	File file;

	@SuppressWarnings("deprecation")
	@Before
	public void setup() throws IOException {

		logger.info("setup()");

		server = SQSRestServerBuilder.withPort(Integer.parseInt(sqsConfig.getPort()))
				.withServerAddress(new NodeAddress(sqsConfig.getProtocol(), sqsConfig.getHost(),
						Integer.parseInt(sqsConfig.getPort()), ""))
				.start();

		String endpoint = sqsConfig.getProtocol() + "://" + sqsConfig.getHost() + ":" + sqsConfig.getPort() + "/";

		amazonSQSClient = new AmazonSQSClient(new AnonymousAWSCredentials());
		amazonSQSClient.setEndpoint(endpoint);

		CreateQueueResult createQueueResult = amazonSQSClient.createQueue(sqsConfig.getName());
		queueUrl = createQueueResult.getQueueUrl();
		logger.info("created queue url: {}", queueUrl);

		assertThat(amazonSQSClient.listQueues().getQueueUrls()).isNotNull();
		assertThat(amazonSQSClient.listQueues().getQueueUrls()).hasSize(1);

		assertThat(amazonSQSClient.listQueues().getQueueUrls().contains(sqsConfig.getName()));

		file = new File(fileConfig.getFilePath() + fileConfig.getFileName());
		file.createNewFile();
	}

	@Test
	public void sendMessageTest() throws IOException {

		logger.info("sendMessageTest()");

		MessageDto message = new MessageDto();
		message.setMessage("test message from develop environment");

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

		server.stopAndWait();

	}
}
