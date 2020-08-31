package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.util.Translator;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LanguageService {

    private final Translator translator;

    public Optional<Language> findByFullName(String name) {
        if (name == null || name.trim().isEmpty())
            return Optional.empty();
        final String NORMALIZED_NAME = StringUtils.stripAccents(name.toUpperCase().trim());
        return translator.getRegisteredLanguages().stream()
                .filter(lang -> lang.isDisplayed() && StringUtils.stripAccents(lang.getFullName().toUpperCase().trim())
                        .startsWith(NORMALIZED_NAME))
                .findFirst();
    }

    public Optional<Language> findByAbbreviation(String name) {
        if (name == null || name.trim().isEmpty())
            return Optional.empty();
        final String NORMALIZED_NAME = StringUtils.stripAccents(name.toUpperCase().trim());
        return translator.getRegisteredLanguages().stream()
                .filter(lang -> lang.isDisplayed() && StringUtils.stripAccents(lang.getAbbreviation().toUpperCase().trim())
                        .equals(NORMALIZED_NAME))
                .findFirst();
    }
}