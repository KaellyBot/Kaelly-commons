package com.github.kaellybot.commons.model.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {

    FR        ("Français", "FR", ":flag_fr:", "https://i.imgur.com/wPtcAve.png", true ),
    EN        ("English" , "EN", ":flag_gb:", "https://i.imgur.com/5byrtQs.png", true ),
    ES        ("Español" , "ES", ":flag_es:", "https://i.imgur.com/IaZjfOy.png", true ),
    APRIL_FOOL("Français", "FR", ":flag_fr:", "https://i.imgur.com/wPtcAve.png", false);

    private final String fullName;
    private final String abbreviation;
    private final String emoji;
    private final String image;
    private final boolean isDisplayed;

    public String getFileName() {
        return "label_" + getAbbreviation() + ".properties";
    }
}