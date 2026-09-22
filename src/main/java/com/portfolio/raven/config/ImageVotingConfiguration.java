package com.portfolio.raven.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;

@Configuration
public class ImageVotingConfiguration {
    @Bean
    public Clock imageVotingClock() {
        return Clock.systemUTC();
    }
}
