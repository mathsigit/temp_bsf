package com.island.mailalert;

import com.island.mailalert.service.EntryService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopConfigException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@EnableEncryptableProperties
@SpringBootApplication
public class MailAlertApplication implements ApplicationRunner {

	private static final Logger logger = LoggerFactory.getLogger(MailAlertApplication.class);

	@Value("${table.name:#{null}}")
	private String tableName;

	@Value("${table.owner:#{null}}")
	private String tableOwner;

	@Autowired
	EntryService entryService;

	public static void main(String[] args) {
		SpringApplication.run(MailAlertApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (tableName == null)
			throw new AopConfigException("The property -Dtable.name must be set!");
		logger.info("Application started to calculate table: {}", tableName);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void execute() throws Exception {
		entryService.executed(this.tableName, this.tableOwner);
	}
}
