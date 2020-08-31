package com.github.kaellybot.commons.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.constants.MultilingualEnum;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class Translator {
    private final Map<Language, Properties> labels;
    private final Random random;

    public Translator(){
        labels = new ConcurrentHashMap<>();
        random = new Random();

        for(Language lg : Language.values())
            try(InputStream file = com.github.kaellybot.commons.util.Translator.class.getResourceAsStream("/label_" + lg + ".properties")) {
                Properties prop = new Properties();
                prop.load(new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8)));
                labels.put(lg, prop);
            } catch (IOException e) {
                log.error("Translator.getLabel", e);
            }
    }

    public String getLabel(Language lang, String property){
        String value = labels.get(lang).getProperty(property);
        if (value == null || value.trim().isEmpty()) {
            log.warn("Missing label in {} for property '{}'", lang, property);
            return property;
        }

        return value;
    }

    public String getLabel(Language lang, MultilingualEnum anEnum){
        String value = labels.get(lang).getProperty(anEnum.getKey());
        if (value == null || value.trim().isEmpty()) {
            log.warn("Missing label in {} for enum {}[{}]", lang, anEnum.getClass().getSimpleName(), anEnum);
            return anEnum.getKey();
        }

        return value;
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
        String value = labels.get(lang).getProperty(property);

        if (value == null || value.trim().isEmpty()) {
            log.error("Missing label in {} : {}", lang, property);
            return property;
        }

        String[] values = value.split(";");
        value = values[random.nextInt(values.length)];

        for(Object arg : arguments)
            value = value.replaceFirst("\\{}", arg.toString());

        return value;
    }
}