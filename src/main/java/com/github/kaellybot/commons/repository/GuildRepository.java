package com.github.kaellybot.commons.repository;

import com.github.kaellybot.commons.model.entity.Guild;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GuildRepository extends ReactiveMongoRepository<Guild, String> {
}
