package com.netcracker.edu.kulich.controller.config;


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

    public WebConfiguration(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }


    @Bean
    public String catalogUrl(@Value("${catalog.name}") String catalog) {
        List<ServiceInstance> list = discoveryClient.getInstances(catalog);
        if (list != null && list.size() > 0) {
            return list.get(0).getUri().toString();
        }
        return "";
    }

    @Bean
    public String inventoryUrl(@Value("${inventory.name}") String inventory) {
        List<ServiceInstance> list = discoveryClient.getInstances(inventory);
        if (list != null && list.size() > 0) {
            return list.get(0).getUri().toString();
        }
        return "";
    }

    @Bean
    public String customerManagementUrl(@Value("${customer-management.name}") String customerManagement) {
        List<ServiceInstance> list = discoveryClient.getInstances(customerManagement);
        if (list != null && list.size() > 0) {
            return list.get(0).getUri().toString();
        }
        return "";
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
