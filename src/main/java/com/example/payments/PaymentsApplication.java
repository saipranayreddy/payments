package com.example.payments;

import com.example.payments.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentsApplication {


	public static void main(String[] args) {
		SpringApplication.run(PaymentsApplication.class, args);
	}

}
