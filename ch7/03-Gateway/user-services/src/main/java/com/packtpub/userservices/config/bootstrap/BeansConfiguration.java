package com.packtpub.userservices.config.bootstrap;

import com.packtpub.userservices.config.logging.CustomLoadBalancerInterceptor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeansConfiguration {

    @LoadBalanced
    @Bean
    public RestClient.Builder restClient(CustomLoadBalancerInterceptor customLoadBalancerInterceptor) {
        return RestClient
                .builder()
                .requestInterceptor(customLoadBalancerInterceptor);
    }

}