package com.github.kaellybot.commons.model.constants;

public interface Language {

    /**
     * @return The full name of the language in its language
     */
    String getFullName();

    /**
     * @return The file name of the labels used for the language
     */
    String getFileName();

    /**
     * @return The abbreviation usually used for accept-language header
     */
    String getAbbreviation();
    /**
     * @return True if the language can used by an end-user
     */
    boolean isDisplayed();
}