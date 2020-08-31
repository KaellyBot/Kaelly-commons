package com.github.kaellybot.commons.model.entity;

import com.github.kaellybot.commons.model.constants.Language;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "guilds")
public class Guild {

    @Id
    private String id;

    private Language language;

    private Server server;

    private String prefix;
}
