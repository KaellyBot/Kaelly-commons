package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.constants.Language;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LanguageServiceTest {

    private static final LanguageService LANGUAGE_SERVICE = new LanguageService();

    @ParameterizedTest
    @EnumSource(Language.class)
    void findPassingCaseTest(Language language){
        assertAll(
                () -> assertThat(LANGUAGE_SERVICE.findByName(language.name())).isPresent(),
                () -> assertThat(LANGUAGE_SERVICE.findByName(language.name().toLowerCase())).isPresent(),
                () -> assertThat(LANGUAGE_SERVICE.findByName(language.name().toUpperCase())).isPresent(),
                () -> assertThat(LANGUAGE_SERVICE.findByName(StringUtils.stripAccents(language.name()))).isPresent()
        );

        LANGUAGE_SERVICE.findByName(language.name())
                .ifPresent(potentialServer -> assertThat(language).isNotNull().isEqualTo(potentialServer));
    }

    @ParameterizedTest
    @EnumSource(Language.class)
    void findNoPassingCaseTest(Language language){
        assertThat(LANGUAGE_SERVICE.findByName(language.name() + "_BAD_NAME")).isEmpty();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findNullAndEmptyLanguageTest(String source){
        assertThat(LANGUAGE_SERVICE.findByName(source)).isEmpty();
    }
}