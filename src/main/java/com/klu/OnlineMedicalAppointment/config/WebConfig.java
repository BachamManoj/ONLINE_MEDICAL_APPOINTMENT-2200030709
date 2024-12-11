package com.klu.OnlineMedicalAppointment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://sdp2200030709.netlify.app","https://sdp-java.vercel.app") // Allow your React app’s origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow necessary methods
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
