package com.iftm.newsletter.models.dtos;

import com.iftm.newsletter.models.Post;
import com.iftm.newsletter.models.Tag;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record PostDTO (String title, String authorName, String body, List<TagDTO> tags) {
    static PostDTO postDTO(Post post) {
        List<TagDTO> tags = null;
        if(post.getTags() != null && !post.getTags().isEmpty())
            tags = post.getTags().stream().map(TagDTO::tagDTO).collect(Collectors.toList());

        return new PostDTO(post.getTitle(), post.getAuthorName(), post.getBody(), tags);
    }

    public Post toPost() {
        List<Tag> tagList = null;
        if(this.tags != null && !this.tags.isEmpty())
            tagList = this.tags.stream().map(TagDTO::toTag
            ).collect(Collectors.toList());

        return new Post(this.title,
                this.authorName,
                this.body,
                tagList);
    }

}
