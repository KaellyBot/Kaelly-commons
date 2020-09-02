package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.entity.Dimension;
import com.github.kaellybot.commons.repository.DimensionRepository;
import com.github.kaellybot.commons.util.Translator;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DimensionService extends AbstractEntityService<Dimension, String> {

    private final DimensionRepository dimensionRepository;

    public DimensionService(Translator translator, DimensionRepository dimensionRepository) {
        super(translator);
        this.dimensionRepository = dimensionRepository;
    }

    public Mono<Dimension> findById(String id){
        return dimensionRepository.findById(id);
    }

    public Flux<Dimension> findAll(){
        return dimensionRepository.findAll();
    }

    public Mono<Dimension> save(Dimension dimension) {
        return dimensionRepository.save(dimension);
    }

    @Override
    protected ReactiveMongoRepository<Dimension, String> getRepository() {
        return dimensionRepository;
    }
}