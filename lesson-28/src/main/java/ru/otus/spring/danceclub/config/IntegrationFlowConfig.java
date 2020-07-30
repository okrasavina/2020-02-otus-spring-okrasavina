package ru.otus.spring.danceclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationFlowConfig {

    @Bean
    public MessageChannel dancerChannel() {
        return MessageChannels.queue(10).get();
    }

    @Bean
    public MessageChannel danceCoupleChannel() {
        return MessageChannels.direct().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(3).get();
    }

    @Bean
    public IntegrationFlow danceFlow() {
        return IntegrationFlows.from("dancerChannel")
                .split()
                .handle("defaultDanceManagerService", "makeCouple")
                .aggregate()
                .channel("danceCoupleChannel")
                .get();
    }
}
