package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.entity.Dimension;
import com.github.kaellybot.commons.repository.DimensionRepository;
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
class DimensionServiceTest {

    private static final Dimension DEFAULT_DIMENSION = Dimension.builder().id("DEFAULT_DIMENSION").build();

    @Mock
    private DimensionRepository dimensionRepository;

    @InjectMocks
    private DimensionService dimensionService;

    @Test
    void findByIdPassingCaseTest(){
        Mockito.when(dimensionRepository.findById(DEFAULT_DIMENSION.getId())).thenReturn(Mono.just(DEFAULT_DIMENSION));

        StepVerifier.create(dimensionService.findById(DEFAULT_DIMENSION.getId()))
                .assertNext(server -> assertThat(server).isNotNull().isEqualTo(DEFAULT_DIMENSION))
                .expectComplete()
                .verify();
    }

    @Test
    void findByIdNoPassingCaseTest(){
        Mockito.when(dimensionRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());

        StepVerifier.create(dimensionService.findById(DEFAULT_DIMENSION.getId() + "_BAD_NAME"))
                .expectComplete()
                .verify();
    }

    @Test
    void findAllPassingCaseTest(){
        Mockito.when(dimensionRepository.findAll()).thenReturn(Flux.just(DEFAULT_DIMENSION, DEFAULT_DIMENSION));

        StepVerifier.create(dimensionService.findAll())
                .assertNext(server -> assertThat(server).isNotNull().isEqualTo(DEFAULT_DIMENSION))
                .thenConsumeWhile(x -> true)
                .expectComplete()
                .verify();
    }

    @Test
    void findAllNoPassingCaseTest(){
        Mockito.when(dimensionRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(dimensionService.findAll())
                .expectComplete()
                .verify();
    }

    @Test
    void getRepositoryTest(){
        assertThat(dimensionService.getRepository()).isEqualTo(dimensionRepository);
    }
}