package com.honda.interauto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages={"com.honda.interauto.dao.auto","com.honda.interauto.dao.test"})

@MapperScan({"com.honda.interauto.dao.auto","com.honda.interauto.dao.user"})
@ComponentScan
//选择手动注入bean
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, MybatisAutoConfiguration.class})
//@SpringBootApplication
public class InterautoApplication {
	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(InterautoApplication.class);
		SpringApplication.run(InterautoApplication.class, args);
		logger.info("=====================>" + InterautoApplication.class.toString() + "<===================== started...");
	}
}
