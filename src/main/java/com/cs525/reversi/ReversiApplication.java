package com.cs525.reversi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ReversiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReversiApplication.class, args);
    }

    //for DTOs
    @Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
