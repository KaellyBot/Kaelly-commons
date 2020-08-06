package com.github.kaellybot.commons.util;

import com.github.kaellybot.commons.model.constants.*;
import com.github.kaellybot.commons.model.entity.MultilingualEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TranslatorTest {

    private static final String TEST_KEY = "translator.test";
    private static final String EMPTY_TEST_KEY = "translator.test.empty";
    private static final String NO_EXISTING_TEST_KEY = "translator.test_not_found";
    private static final String TEST_VALUE = "Test";

    @InjectMocks
    private Translator translator;

    @ParameterizedTest
    @EnumSource(Language.class)
    void getLabelPropertyTest(Language language)
    {
        assertThat(translator.getLabel(language, TEST_KEY)).isNotNull().isEqualTo(TEST_VALUE);
        assertThat(translator.getLabel(language, EMPTY_TEST_KEY)).isNotNull().isEqualTo(EMPTY_TEST_KEY);
    }

    @Test
    void getLabelPropertyForNoExistingKeyTest()
    {
        assertThat(translator.getLabel(Language.FR, NO_EXISTING_TEST_KEY)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }

    @ParameterizedTest
    @EnumSource(Language.class)
    void getLabelEnumerationTest(Language language)
    {
        MultilingualEnum noExistingEnum = () -> TEST_KEY;
        assertThat(translator.getLabel(language, noExistingEnum)).isNotNull().isEqualTo(TEST_VALUE);
    }

    @Test
    void getLabelEnumerationForNoExistingEnumerationTest()
    {
        MultilingualEnum noExistingEnum = () -> NO_EXISTING_TEST_KEY;
        assertThat(translator.getLabel(Language.FR, noExistingEnum)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }

    @ParameterizedTest
    @EnumSource(Language.class)
    void getLabelEntityTest(Language language)
    {
        MultilingualEntity entity = new MultilingualEntity(TEST_KEY, Map.of(language, TEST_VALUE)){};
        assertThat(translator.getLabel(language, entity)).isNotNull().isNotEmpty().isEqualTo(TEST_VALUE);
    }

    @Test
    void getLabelEntityForNoExistingEntityTest()
    {
        MultilingualEntity noExistingEntity = new MultilingualEntity(NO_EXISTING_TEST_KEY, null){};
        assertThat(translator.getLabel(Language.FR, noExistingEntity)).isNotNull().isEqualTo(NO_EXISTING_TEST_KEY);
    }
}