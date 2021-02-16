package com.bank;

// STS is the serve, should be open all the time

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankofstatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankofstatesApplication.class, args);
	}
	
}

/*
 @Component - Higher level, Utility class (like react component)
 
 @RestController - Get data from UI and pass to server RestController goes to service then repository
 @Service - Service Provider 
 @Repository - Data layer fetch data and deliver
 
********************************** 
 How you deploy the spring boot project in production
 1)Step 1 : Create jar file through mvn command
 2)Step 2 : Move that jar o production & run with java -jar <<FILE_NAME>>
*/