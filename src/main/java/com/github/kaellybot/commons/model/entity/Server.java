package com.github.kaellybot.commons.model.entity;

import com.github.kaellybot.commons.model.constants.Game;
import com.github.kaellybot.commons.model.constants.Language;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "servers")
public class Server extends MultilingualEntity {

    private final Game game;
    private final String imgUrl;

    @Builder
    public Server(String id, Game game, String imgUrl, Map<Language, String> labels){
        super(id, labels);
        this.game = game;
        this.imgUrl = imgUrl;
    }
}
