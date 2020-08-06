package com.github.kaellybot.commons.repository;

import com.github.kaellybot.commons.model.entity.Server;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ServerRepository extends ReactiveMongoRepository<Server, String> {
}