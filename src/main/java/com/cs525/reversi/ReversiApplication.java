package com.cs525.reversi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ReversiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReversiApplication.class, args);
    }

    //for DTOs
    @Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
