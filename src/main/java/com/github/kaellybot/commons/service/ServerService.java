package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.commons.repository.ServerRepository;
import com.github.kaellybot.commons.util.Translator;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ServerService extends AbstractEntityService<Server, String>{

    private final ServerRepository serverRepository;

    public ServerService(Translator translator, ServerRepository serverRepository) {
        super(translator);
        this.serverRepository = serverRepository;
    }

    public Mono<Server> findById(String id) {
        return serverRepository.findById(id);
    }

    public Flux<Server> findAll() {
        return serverRepository.findAll();
    }

    @Override
    protected ReactiveMongoRepository<Server, String> getRepository() {
        return serverRepository;
    }
}