package com.ing.fr.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/***
 * @author Gautam Sahoo
 * @version 1.0
 * @apiNote AppConfig is the class maintains java based configurations
 */
@Configuration
public class AppConfig {

    @Bean
    public double minimumDepositAmount() {
        return 0.01;
    }

    @Bean
    public boolean overDraftAllowed() {
        return false;
    }

//    @Bean
//    public String customerProterties(){
//        ConfigurationUtils.getRequiredBeanClassName()
//    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*").allowedOrigins("http://localhost:8080");
            }
        };
    }
}
