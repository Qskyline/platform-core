package com.skyline.platform.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class Run {

	public static void main(String[] args) {
		SpringApplication.run(Run.class, args);
	}
}
