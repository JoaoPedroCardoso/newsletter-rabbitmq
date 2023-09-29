package com.iftm.newsletter.models.dtos;

import com.iftm.newsletter.models.Tag;

public record TagDTO (String name) {

    static TagDTO tagDTO(Tag tag) {
        return new TagDTO(tag.getName());
    }

    public Tag toTag() {
        return new Tag(this.name);
    }

}
