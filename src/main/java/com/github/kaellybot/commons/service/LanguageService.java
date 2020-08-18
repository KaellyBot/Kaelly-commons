package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.constants.Language;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@NoArgsConstructor
public class LanguageService {

    public Optional<Language> findByName(String name) {
        if (name == null || name.trim().isEmpty())
            return Optional.empty();
        final String NORMALIZED_NAME = StringUtils.stripAccents(name.toUpperCase().trim());
        return Stream.of(Language.values())
                .filter(lang -> StringUtils.stripAccents(lang.name().toUpperCase().trim())
                        .equals(NORMALIZED_NAME))
                .findFirst();
    }
}