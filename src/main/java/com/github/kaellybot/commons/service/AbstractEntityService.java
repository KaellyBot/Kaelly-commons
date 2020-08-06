package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import com.github.kaellybot.commons.util.Translator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public abstract class AbstractEntityService<E extends MultilingualEntity, I> {

    private final Translator translator;

    public Flux<E> findAllForName(String name, Language language){
        String filteredName = StringUtils.stripAccents(name).toLowerCase().trim();
        Flux<E> likeEntities = getRepository().findAll().filter(entity -> StringUtils
                .stripAccents(translator.getLabel(language, entity)).toLowerCase().trim()
                .startsWith(filteredName));

        Flux<E> exactEntities = likeEntities.filter(entity -> StringUtils
                .stripAccents(translator.getLabel(language, entity)).toLowerCase().trim()
                .equals(filteredName));

        return exactEntities.count()
                .flatMapMany(count -> count == 1 ? exactEntities : likeEntities);
    }

    protected abstract ReactiveMongoRepository<E, I> getRepository();
}
