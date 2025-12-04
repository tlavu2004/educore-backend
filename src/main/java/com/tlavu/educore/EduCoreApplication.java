package com.tlavu.educore;

import com.tlavu.educore.shared.config.environment.EnvironmentConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EduCoreApplication {

	public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(EduCoreApplication.class);
        springApplication.addInitializers(new EnvironmentConfig());
        springApplication.run(args);
	}

}
