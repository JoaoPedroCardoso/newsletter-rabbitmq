package com.iftm.newsletter.models.dtos;

import com.iftm.newsletter.models.News;
import com.iftm.newsletter.models.Post;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public record NewsDTO (String id, String title, String date, String editorName, List<PostDTO> posts) {
    public static NewsDTO newsDTO(News news) {
        List<PostDTO> postDTOS = null;
        if(news.getPosts() != null && !news.getPosts().isEmpty())
            postDTOS = news.getPosts().stream().map(PostDTO::postDTO).collect(Collectors.toList());

        return new NewsDTO(news.getId() != null ? news.getId().toString() : null, news.getTitle(), news.getDate(),
                news.getEditorName(), postDTOS);
    }

    public News toNews() {
        ObjectId id = null;
        if(this.id != null)
            id = new ObjectId(this.id);

        List<Post> postList = null;
        if(this.posts != null)
            postList = this.posts.stream().map(PostDTO::toPost
            ).collect(Collectors.toList());

        return new News(id,
                this.title,
                this.date,
                this.editorName,
                postList);
    }
}
