package com.github.kaellybot.commons.service;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.util.Translator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class LanguageServiceTest {

    private static final Language EXISTING_LANGUAGE = new Language(){

        @Override
        public String getFullName() {
            return "EXISTING_LANGUAGE";
        }

        @Override
        public String getFileName() {
            return "label_TEST.properties";
        }

        @Override
        public String getAbbreviation() {
            return "EXISTING_LANGUAGE";
        }

        @Override
        public boolean isDisplayed() {
            return true;
        }
    };

    private static final Language NO_DISPLAYED_LANGUAGE = new Language(){

        @Override
        public String getFullName() {
            return "NO_DISPLAYED_LANGUAGE";
        }

        @Override
        public String getFileName() {
            return "label_TEST.properties";
        }

        @Override
        public String getAbbreviation() {
            return "NO_DISPLAYED_LANGUAGE";
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }
    };

    @Spy
    private Translator translator;

    @InjectMocks
    private LanguageService languageService;

    @BeforeEach
    void beforeEach(){
        translator.register(EXISTING_LANGUAGE);
        translator.register(NO_DISPLAYED_LANGUAGE);
    }

    @Test
    void findByFullNamePassingCaseTest(){
        assertAll(
                () -> assertThat(languageService.findByFullName(EXISTING_LANGUAGE.getFullName())).isPresent(),
                () -> assertThat(languageService.findByFullName(EXISTING_LANGUAGE.getFullName().toLowerCase())).isPresent(),
                () -> assertThat(languageService.findByFullName(EXISTING_LANGUAGE.getFullName().toUpperCase())).isPresent(),
                () -> assertThat(languageService.findByFullName(StringUtils.stripAccents(EXISTING_LANGUAGE.getFullName()))).isPresent()
        );

        assertAll(
                () -> assertThat(languageService.findByFullName(EXISTING_LANGUAGE.getAbbreviation())).isPresent(),
                () -> assertThat(languageService.findByFullName(EXISTING_LANGUAGE.getAbbreviation().toLowerCase())).isPresent(),
                () -> assertThat(languageService.findByFullName(EXISTING_LANGUAGE.getAbbreviation().toUpperCase())).isPresent(),
                () -> assertThat(languageService.findByFullName(StringUtils.stripAccents(EXISTING_LANGUAGE.getAbbreviation()))).isPresent()
        );

        languageService.findByFullName(EXISTING_LANGUAGE.getFullName())
                .ifPresent(potentialServer -> assertThat(EXISTING_LANGUAGE).isNotNull().isEqualTo(potentialServer));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findByFullNameNullAndEmptyLanguageTest(String source) {
        assertThat(languageService.findByFullName(source)).isEmpty();
    }

    @Test
    void findByFullNameNotDisplayedLanguageTest() {
        assertThat(languageService.findByFullName(NO_DISPLAYED_LANGUAGE.getFullName())).isEmpty();
    }


    @Test
    void findByAbbreviationPassingCaseTest(){
        assertAll(
                () -> assertThat(languageService.findByAbbreviation(EXISTING_LANGUAGE.getAbbreviation())).isPresent(),
                () -> assertThat(languageService.findByAbbreviation(EXISTING_LANGUAGE.getAbbreviation().toLowerCase())).isPresent(),
                () -> assertThat(languageService.findByAbbreviation(EXISTING_LANGUAGE.getAbbreviation().toUpperCase())).isPresent(),
                () -> assertThat(languageService.findByAbbreviation(StringUtils.stripAccents(EXISTING_LANGUAGE.getAbbreviation()))).isPresent()
        );

        languageService.findByAbbreviation(EXISTING_LANGUAGE.getAbbreviation())
                .ifPresent(potentialServer -> assertThat(EXISTING_LANGUAGE).isNotNull().isEqualTo(potentialServer));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void findByAbbreviationNullAndEmptyLanguageTest(String source){
        assertThat(languageService.findByAbbreviation(source)).isEmpty();
    }

    @Test
    void findByAbbreviationNotDisplayedLanguageTest() {
        assertThat(languageService.findByAbbreviation(NO_DISPLAYED_LANGUAGE.getAbbreviation())).isEmpty();
    }
}