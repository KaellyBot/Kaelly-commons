package com.github.kaellybot.commons.util;

import com.github.kaellybot.commons.model.constants.*;
import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class TranslatorTest {

    private static final String TEST_KEY = "translator.test";
    private static final String TEST_ARGUMENT_KEY = "translator.argument_test";
    private static final String EMPTY_TEST_KEY = "translator.test.empty";
    private static final String NO_EXISTING_TEST_KEY = "translator.test_not_found";
    private static final String TEST_VALUE = "Test";

    private static final Language EXISTING_LANGUAGE = new Language(){

        @Override
        public String getFullName() {
            return "TEST";
        }

        @Override
        public String getFileName() {
            return "label_TEST.properties";
        }

        @Override
        public String getAbbreviation() {
            return "TEST";
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }
    };

    private static final Language NO_EXISTING_LANGUAGE = new Language(){

        @Override
        public String getFullName() {
            return null;
        }

        @Override
        public String getFileName() {
            return null;
        }

        @Override
        public String getAbbreviation() {
            return null;
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }
    };

    private static final Language NOT_REGISTERED_LANGUAGE = new Language(){

        @Override
        public String getFullName() {
            return null;
        }

        @Override
        public String getFileName() {
            return null;
        }

        @Override
        public String getAbbreviation() {
            return null;
        }

        @Override
        public boolean isDisplayed() {
            return false;
        }
    };

    @Spy
    private Translator translator;

    @BeforeEach
    void beforeEach(){
        Mockito.reset(translator);
    }

    @Test
    void registerDoesNotThrowExceptionWithNoExistingLanguage(){
        assertThatCode(() -> translator.register(NO_EXISTING_LANGUAGE)).doesNotThrowAnyException();
    }

    @Test
    void registerWhenIOExceptionOccursTest() throws IOException {
        Mockito.doThrow(new RuntimeException("registerWhenIOExceptionOccursTest"))
                .when(translator).loadLabels(Mockito.any(), Mockito.any());
        assertThatCode(() -> translator.register(NOT_REGISTERED_LANGUAGE)).doesNotThrowAnyException();
    }

    @Test
    void getLabelPropertyTest()
    {
        translator.register(EXISTING_LANGUAGE);
        assertThat(translator.getLabel(EXISTING_LANGUAGE, TEST_KEY)).isNotNull().isEqualTo(TEST_VALUE);
        assertThat(translator.getLabel(EXISTING_LANGUAGE, EMPTY_TEST_KEY)).isNotNull().isEqualTo(EMPTY_TEST_KEY);
        assertThat(translator.getLabel(EXISTING_LANGUAGE, TEST_ARGUMENT_KEY, TEST_VALUE))
                .isNotNull().isEqualTo(TEST_VALUE);
    }

    @Test
    void getLabelPropertyForNoExistingKeyTest()
    {
        assertThat(translator.getLabel(EXISTING_LANGUAGE, NO_EXISTING_TEST_KEY)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }

    @Test
    void getRandomLabelPropertyTest()
    {
        translator.register(EXISTING_LANGUAGE);
        assertThat(translator.getRandomLabel(EXISTING_LANGUAGE, TEST_KEY)).isNotNull().isEqualTo(TEST_VALUE);
        assertThat(translator.getRandomLabel(EXISTING_LANGUAGE, EMPTY_TEST_KEY)).isNotNull().isEqualTo(EMPTY_TEST_KEY);
        assertThat(translator.getRandomLabel(EXISTING_LANGUAGE, TEST_ARGUMENT_KEY, TEST_VALUE))
                .isNotNull().isEqualTo(TEST_VALUE);
    }

    @Test
    void getRandomLabelPropertyForNoExistingKeyTest()
    {
        assertThat(translator.getRandomLabel(EXISTING_LANGUAGE, NO_EXISTING_TEST_KEY)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }

    @Test
    void getLabelEnumerationTest()
    {
        translator.register(EXISTING_LANGUAGE);
        MultilingualEnum noExistingEnum = () -> TEST_KEY;
        assertThat(translator.getLabel(EXISTING_LANGUAGE, noExistingEnum)).isNotNull().isEqualTo(TEST_VALUE);
    }

    @Test
    void getLabelEnumerationForNoExistingEnumerationTest()
    {
        MultilingualEnum noExistingEnum = () -> NO_EXISTING_TEST_KEY;
        assertThat(translator.getLabel(EXISTING_LANGUAGE, noExistingEnum)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }

    @Test
    void getLabelEntityTest()
    {
        MultilingualEntity entity = new MultilingualEntity(TEST_KEY, Map.of(EXISTING_LANGUAGE, TEST_VALUE)){};
        assertThat(translator.getLabel(EXISTING_LANGUAGE, entity)).isNotNull().isNotEmpty().isEqualTo(TEST_VALUE);
    }

    @Test
    void getLabelErrorTest()
    {
        Error error = (translator, language1) -> translator.getLabel(language1, TEST_VALUE);
        assertThat(translator.getLabel(EXISTING_LANGUAGE, error)).isNotNull().isNotEmpty()
                .isEqualTo(translator.getLabel(EXISTING_LANGUAGE, TEST_VALUE));
    }

    @Test
    void getLabelEntityForNoExistingEntityTest()
    {
        MultilingualEntity noExistingEntity = new MultilingualEntity(NO_EXISTING_TEST_KEY, null){};
        assertThat(translator.getLabel(EXISTING_LANGUAGE, noExistingEntity)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }

    @Test
    void getInternalLabelWithNoRegisteredLanguage()
    {
        assertThat(translator.getInternalLabel(NOT_REGISTERED_LANGUAGE, TEST_KEY)).isNotNull().isEqualTo(TEST_KEY);
    }

    @ParameterizedTest
    @EnumSource(Translator.LanguageEnum.class)
    void isLanguageFieldsFilled(Language language){
        assertThat(language.getFullName()).isNotNull().isNotEmpty();
        assertThat(language.getAbbreviation()).isNotNull().isNotEmpty();
        assertThat(language.getFileName()).isNotNull().isNotEmpty();
        assertThat(language.isDisplayed()).isTrue();
    }
}