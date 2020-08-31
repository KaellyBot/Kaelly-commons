package com.github.kaellybot.commons.model.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Language {

    FR("Français", "FR", true),
    EN("English", "EN", true),
    ES("Español", "ES", true),
    APRIL_FOOL("Français", "FR", false);

    private final String fullName;
    private final String abbreviation;
    private final boolean isDisplayed;

    public String getFullName() {
        return fullName;
    }

    public String getFileName() {
        return "label_" + getAbbreviation() + ".properties";
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }
}