package com.cs525.reversi.config;

import java.util.UUID;

import org.hibernate.id.UUIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.cs525.reversi.models.User;
import com.cs525.reversi.repositories.UserRepository;

@Component
public class OnApplicationStartUp {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepo;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info("Start Of onApplicationEvent");

		createDefaultUser();
		logger.info("End Of onApplicationEvent");

	}

	private void createDefaultUser() {
         
		User user = new User();
		user.setUsername("comp");
		user.setUuid(UUID.randomUUID());
		userRepo.save(user);
		
	}

}