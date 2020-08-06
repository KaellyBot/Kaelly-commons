package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.commons.repository.ServerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ServerServiceTest {

    private static final Server DEFAULT_SERVER = Server.builder().id("DEFAULT_SERVER").build();

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerService serverService;

    @Test
    void findByIdPassingCaseTest(){
        Mockito.when(serverRepository.findById(DEFAULT_SERVER.getId())).thenReturn(Mono.just(DEFAULT_SERVER));

        StepVerifier.create(serverService.findById(DEFAULT_SERVER.getId()))
                .assertNext(server -> assertThat(server).isNotNull().isEqualTo(DEFAULT_SERVER))
                .expectComplete()
                .verify();
    }

    @Test
    void findByIdNoPassingCaseTest(){
        Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());

        StepVerifier.create(serverService.findById(DEFAULT_SERVER.getId() + "_BAD_NAME"))
                .expectComplete()
                .verify();
    }

    @Test
    void findAllPassingCaseTest(){
        Mockito.when(serverRepository.findAll()).thenReturn(Flux.just(DEFAULT_SERVER, DEFAULT_SERVER));

        StepVerifier.create(serverService.findAll())
                .assertNext(server -> assertThat(server).isNotNull().isEqualTo(DEFAULT_SERVER))
                .thenConsumeWhile(x -> true)
                .expectComplete()
                .verify();
    }

    @Test
    void findAllNoPassingCaseTest(){
        Mockito.when(serverRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(serverService.findAll())
                .expectComplete()
                .verify();
    }

    @Test
    void getRepositoryTest(){
        assertThat(serverService.getRepository()).isEqualTo(serverRepository);
    }
}