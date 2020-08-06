package com.github.kaellybot.commons.model.entity;

import com.github.kaellybot.commons.model.constants.Language;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "dimensions")
public class Dimension extends MultilingualEntity {

    private final String urlImg;
    private final int color;

    @Builder
    public Dimension(String id, String urlImg, int color, Map<Language, String> labels){
        super(id, labels);
        this.urlImg = urlImg;
        this.color = color;
    }
}
