package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sqs")
public class SQSConfig {

	@Value("${sqs.name}")
	private String name;

	@Value("${sqs.protocol:}")
	private String protocol;

	@Value("${sqs.host:}")
	private String host;

	@Value("${sqs.port:}")
	private String port;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("SQSConfig [name=%s, protocol=%s, host=%s, port=%s]", name, protocol, host, port);
	}

}
