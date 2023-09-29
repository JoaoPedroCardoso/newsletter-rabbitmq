package com.iftm.logs.services;

import com.iftm.logs.models.dtos.LogDTO;
import com.iftm.logs.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {

    @Autowired
    private LogRepository repository;

    public ResponseEntity<List<LogDTO<Object>>> findAll() {
        var dbLogs = repository.findAll();
        if(dbLogs.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        var dbLogsDTO = dbLogs.stream().map(LogDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok(dbLogsDTO);
    }

    public ResponseEntity<LogDTO> save(LogDTO logDTO) {
        if(logDTO.getAction().isBlank() || logDTO.getObject() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        var dbLog = repository.save(logDTO.toLog());
        return new ResponseEntity<>(new LogDTO(dbLog), HttpStatus.CREATED);
    }
}
