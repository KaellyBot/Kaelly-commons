package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import com.github.kaellybot.commons.util.Translator;
import lombok.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class AbstractEntityServiceTest {

    private static final String DEFAULT_LABEL = "DEFAULT_LABEL";
    private static final Language DEFAULT_LANGUAGE = new Language(){

        @Override
        public String getFullName() {
            return "TEST";
        }

        @Override
        public String getFileName() {
            return "label_TEST.properties";
        }

        @Override
        public String getAbbreviation() {
            return "TEST";
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }
    };

    private static final List<TestEntity> matchingEntities = List.of(
            TestEntity.builder().labels(Map.of(DEFAULT_LANGUAGE, "DEFAULT_LABEL1")).build(),
            TestEntity.builder().labels(Map.of(DEFAULT_LANGUAGE, "default_labeL2")).build(),
            TestEntity.builder().labels(Map.of(DEFAULT_LANGUAGE, "DéFâÜLt_LâBèL3")).build()
    );

    @Mock
    private TestEntityRepository entityRepository;

    @Spy
    private Translator translator;

    @InjectMocks
    private EntityService entityService;

    @BeforeEach
    void beforeEach(){
        Mockito.reset(entityRepository);
    }

    @Test
    void findAllForNameEmptyTest(){
        Mockito.when(entityRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(entityService.findAllForName(DEFAULT_LABEL, DEFAULT_LANGUAGE))
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    void findAllForNameLikeTest(){
        Mockito.when(entityRepository.findAll()).thenReturn(Flux.fromIterable(matchingEntities));
        StepVerifier.create(entityService.findAllForName(DEFAULT_LABEL, DEFAULT_LANGUAGE))
                .expectNext(matchingEntities.toArray(TestEntity[]::new))
                .verifyComplete();
    }

    @Test
    void findAllForNameLikeAndEqualsTest(){
        final TestEntity equalEntity = TestEntity.builder().labels(Map.of(DEFAULT_LANGUAGE, DEFAULT_LABEL)).build();

        Mockito.when(entityRepository.findAll()).thenReturn(Flux.fromIterable(matchingEntities).concatWithValues(equalEntity));
        StepVerifier.create(entityService.findAllForName(DEFAULT_LABEL, DEFAULT_LANGUAGE))
                .expectNext(equalEntity)
                .verifyComplete();
    }

    @Test
    void findAllForNameNoMatchingTest(){
        Mockito.when(entityRepository.findAll()).thenReturn(Flux.fromIterable(matchingEntities));
        StepVerifier.create(entityService.findAllForName(DEFAULT_LABEL + "_BAD_NAME", DEFAULT_LANGUAGE))
                .expectNextCount(0)
                .verifyComplete();
    }

    private static class TestEntity extends MultilingualEntity {

        @Builder
        public TestEntity(String id, Map<Language, String> labels) {
            super(id, labels);
        }

        @Override
        public String toString(){
            return getLabels().get(DEFAULT_LANGUAGE);
        }
    }

    @Service
    private static class EntityService extends AbstractEntityService<TestEntity, String> {

        private final TestEntityRepository repository;

        public EntityService(Translator translator, TestEntityRepository repository){
            super(translator);
            this.repository = repository;
        }

        @Override
        protected ReactiveMongoRepository<TestEntity, String> getRepository() {
            return repository;
        }
    }

    @Repository
    private interface TestEntityRepository extends ReactiveMongoRepository<TestEntity, String> {}
}