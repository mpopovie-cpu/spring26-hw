package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.hw.service.IOService;
import ru.otus.hw.service.StreamsIOService;

@Configuration
@ComponentScan(basePackages = "ru.otus.hw")
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public IOService ioService() {
        return new StreamsIOService(System.out, System.in);
    }
}
