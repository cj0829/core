package com.pmt.core.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class MyProducer {

	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("metadata.broker.list", "192.168.0.41:9092");
		props.setProperty("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		// 创建生产这对象
		Producer<String, String> producer = new Producer<String, String>(config);
		// 生成消息
		KeyedMessage<String, String> data = new KeyedMessage<String, String>("kafkainfo", "test-kafka_12");
		try {
			int i = 1;
			while (i < 100) {
				System.out.println(data);
				
				// 发送消息
				producer.send(data);
				Thread.sleep(1000);   
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		producer.close();
	}
}