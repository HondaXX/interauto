package com.honda.interauto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@MapperScan("com.honda.interauto.dao")
@ComponentScan
//选择手动注入bean
@EnableAutoConfiguration

public class InterautoApplication {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(InterautoApplication.class);
		SpringApplication.run(InterautoApplication.class, args);
		logger.info("=====================>" + InterautoApplication.class.toString() + "<===================== started...");
	}
}
