package com.netcracker.edu.kulich.controller.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfiguration {
    private DiscoveryClient discoveryClient;
    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    public WebConfiguration(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public String catalogUrl(@Value("${catalog.name}") String catalog) {
        return catalog;
    }

    @Bean
    public String inventoryUrl(@Value("${inventory.name}") String inventory) {
        return inventory;
    }

    @Bean
    public String customerManagementUrl(@Value("${customer-management.name}") String customerManagement) {
        return customerManagement;
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
