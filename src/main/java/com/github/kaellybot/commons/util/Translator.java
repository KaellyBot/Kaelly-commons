package com.github.kaellybot.commons.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.constants.MultilingualEnum;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
public abstract class Translator {
    protected Map<Language, Properties> labels;
    protected final Random random;

    public Translator(){
        labels = new ConcurrentHashMap<>();
        random = new Random();
        Stream.of(Language.values()).forEach(this::register);
    }

    protected void register(Language language){
        try(InputStream file = getClass().getResourceAsStream("/" + language.getFileName())) {
            loadLabels(language, file);
        } catch (NullPointerException e) {
            log.error("{} cannot be registered because '{}' does not exist", language, language.getFileName());
        } catch (Exception e) {
            log.error("{} cannot be registered:", language, e);
        }
    }

    protected void loadLabels(Language language, InputStream file) throws IOException {
        Properties prop = new Properties();
        prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
        labels.put(language, prop);
    }

    protected String getInternalLabel(Language lang, String key){
        if (! labels.containsKey(lang)) {
            log.warn("The language {} is not registered", lang);
            return key;
        }

        return Optional.ofNullable(labels.get(lang))
                .map(property -> property.getProperty(key, key))
                .filter(label -> ! label.trim().isEmpty())
                .orElseGet(() -> {
                    log.warn("Missing label in {} for property '{}'", lang, key);
                    return key;
                });
    }

    public Set<Language> getRegisteredLanguages(){
        return labels.keySet();
    }

    public String getLabel(Language lang, String property, Object... arguments){
        String value = getInternalLabel(lang, property);

        for(Object arg : arguments)
            value = value.replaceFirst("\\{}", getLabelFromObject(lang, arg));

        return value;
    }

    public String getLabel(Language lang, MultilingualEnum anEnum){
        return getInternalLabel(lang, anEnum.getKey());
    }

    public String getLabel(Language lang, MultilingualEntity entity){
        if (! Optional.ofNullable(entity.getLabels()).map(map -> map.containsKey(lang)).orElse(false)) {
            log.warn("Missing label in {} for entity {}[{}]", lang, entity.getClass().getSimpleName(), entity.getId());
            return entity.getId();
        }

        return entity.getLabels().get(lang);
    }

    public String getLabel(Language lang, Error error){
        return error.getLabel(this, lang);
    }

    public String getRandomLabel(Language lang, String property, Object... arguments){
        String[] values = getInternalLabel(lang, property).split(";");
        String value = values[random.nextInt(values.length)];

        for(Object arg : arguments)
            value = value.replaceFirst("\\{}", getLabelFromObject(lang, arg));

        return value;
    }

    protected String getLabelFromObject(Language language, Object object){
        if (object instanceof MultilingualEntity)
            return getLabel(language, (MultilingualEntity) object);
        if (object instanceof MultilingualEnum)
            return getLabel(language, (MultilingualEnum) object);
        return String.valueOf(object);
    }
}