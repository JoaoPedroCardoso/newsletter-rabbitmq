package com.iftm.newsletter.services;

import com.iftm.newsletter.mensages.RabbitLogProducer;
import com.iftm.newsletter.models.dtos.LogDTO;
import com.iftm.newsletter.models.dtos.NewsDTO;
import com.iftm.newsletter.models.dtos.PostDTO;
import com.iftm.newsletter.models.firebase.NotificationMessage;
import com.iftm.newsletter.repositories.NewsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsRepository repository;

    @Autowired
    private RabbitLogProducer logProducer;

    @Autowired
    private FirebaseMessagingService firebaseMessagingService;

    public ResponseEntity<List<NewsDTO>> findAll() {
        var dbNews = repository.findAll();
        if(dbNews.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(dbNews.stream().map( newDto -> {
                var dto = NewsDTO.newsDTO(newDto);
                logProducer.sendLog(new LogDTO<>("findAll", dto));
                return dto;
        }).collect(Collectors.toList()));
    }

    public ResponseEntity<NewsDTO> findById(ObjectId id) {
        if(id == null)
            return ResponseEntity.badRequest().build();
        var dbNews = repository.findById(id);

        return dbNews.map(news -> {
            var dto = NewsDTO.newsDTO(news);
            logProducer.sendLog(new LogDTO<>("findById", dto));
            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<NewsDTO> save(NewsDTO newsDTO) {
        var news = NewsDTO.newsDTO(repository.save(newsDTO.toNews()));
        logProducer.sendLog(new LogDTO<>("created", news));
        firebaseMessagingService.sendNotification(new NotificationMessage("Nova noticia cadastrada", news.title(),
                        "", Collections.emptyMap()));
        return ResponseEntity.ok(news);
    }

    public ResponseEntity<NewsDTO> update(NewsDTO newsDTO) {
        if(newsDTO.id() == null)
            return ResponseEntity.badRequest().build();

        var dbNews = repository.findById(new ObjectId(newsDTO.id()));
        if(dbNews.isEmpty())
            return ResponseEntity.notFound().build();

        var dbNewsObj = dbNews.get();
        dbNewsObj.setEditorName(newsDTO.editorName());
        dbNewsObj.setDate(newsDTO.date());
        dbNewsObj.setTitle(newsDTO.title());
        dbNewsObj.setPosts(newsDTO.posts().stream().map(PostDTO::toPost).collect(Collectors.toList()));

        var dto = NewsDTO.newsDTO(repository.save(dbNewsObj));
        logProducer.sendLog(new LogDTO<>("updated", dto));

        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<?> delete(ObjectId id) {
        if(id == null)
            return ResponseEntity.badRequest().build();

        return repository.findById(id).map(news -> {
            repository.deleteById(id);
            logProducer.sendLog(new LogDTO<>("removed", NewsDTO.newsDTO(news)));
            firebaseMessagingService.sendNotification(new NotificationMessage("Noticia Deletada", news.getTitle(),
                    "", Collections.emptyMap()));
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
    }
}
