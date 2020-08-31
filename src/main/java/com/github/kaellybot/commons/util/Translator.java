package com.github.kaellybot.commons.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.constants.MultilingualEnum;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Component
@Slf4j
public class Translator {
    protected Map<Language, Properties> labels;
    protected final Random random;

    public Translator(){
        labels = new ConcurrentHashMap<>();
        random = new Random();
        Stream.of(LanguageEnum.values()).forEach(this::register);
    }

    public void register(Language language){
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
            value = value.replaceFirst("\\{}", arg.toString());

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
            value = value.replaceFirst("\\{}", arg.toString());

        return value;
    }

    @AllArgsConstructor
    protected enum LanguageEnum implements Language {
        FR("Français", "FR", true),
        EN("English", "EN", true),
        ES("Español", "ES", true);

        private final String fullName;
        private final String abbreviation;
        private final boolean isDisplayed;

        @Override
        public String getFullName() {
            return fullName;
        }

        @Override
        public String getFileName() {
            return "label_" + getAbbreviation() + ".properties";
        }

        @Override
        public String getAbbreviation() {
            return abbreviation;
        }

        @Override
        public boolean isDisplayed() {
            return isDisplayed;
        }
    }
}