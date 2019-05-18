package com.netcracker.edu.kulich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication
public class ProcessorApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessorApplication.class, args);
    }
}
