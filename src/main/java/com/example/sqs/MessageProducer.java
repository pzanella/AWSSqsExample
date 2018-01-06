package com.example.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.example.config.SQSConfig;
import com.example.dto.MessageDto;

@Service
public class MessageProducer {

	private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private SQSConfig sqsConfig;

	public void sendMessage(MessageDto message) {

		logger.debug("sendMessage() => {}", message.getMessage());

		jmsTemplate.convertAndSend(sqsConfig.getName(), message);

		logger.debug("text message sent!");
	}
}
