package com.github.kaellybot.commons.repository;

import com.github.kaellybot.commons.model.entity.Dimension;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DimensionRepository extends ReactiveMongoRepository<Dimension, String> {
}