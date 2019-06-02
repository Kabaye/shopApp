package com.netcracker.edu.kulich.controller.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class WebConfiguration {
    private DiscoveryClient discoveryClient;
    private static final Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    public WebConfiguration(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Bean
    public String catalogUrl(@Value("${catalog.name}") String catalog) {
        List<ServiceInstance> list = discoveryClient.getInstances(catalog);
        if (list != null && list.size() > 0) {
            logger.info("Catalog URL was successfully got from eureka with URl: " + list.get(0).getUri().toString());
            return list.get(0).getUri().toString();
        }
        logger.error("Catalog URL is not got");
        return "";
    }

    @Bean
    public String inventoryUrl(@Value("${inventory.name}") String inventory) {
        List<ServiceInstance> list = discoveryClient.getInstances(inventory);
        if (list != null && list.size() > 0) {
            logger.info("Inventory URL was successfully got from eureka with URl: " + list.get(0).getUri().toString());
            return list.get(0).getUri().toString();
        }
        logger.error("Inventory URL is not got");
        return "";
    }

    @Bean
    public String customerManagementUrl(@Value("${customer-management.name}") String customerManagement) {
        List<ServiceInstance> list = discoveryClient.getInstances(customerManagement);
        if (list != null && list.size() > 0) {
            logger.info("Customer-Management URL was successfully got from eureka with URl: " + list.get(0).getUri().toString());
            return list.get(0).getUri().toString();
        }
        logger.error("Customer-Management URL is not got");
        return "";
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
