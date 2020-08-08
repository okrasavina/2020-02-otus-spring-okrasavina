package ru.otus.spring.danceclub.config;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.danceclub.dto.DanceCouple;
import ru.otus.spring.danceclub.dto.Dancer;

import java.util.List;

@MessagingGateway
public interface DanceMakerGateway {

    @Gateway(requestChannel = "dancerChannel", replyChannel = "danceCoupleChannel")
    List<DanceCouple> createDance(List<Dancer> dancers);
}
