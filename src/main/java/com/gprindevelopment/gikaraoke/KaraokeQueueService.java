package com.gprindevelopment.gikaraoke;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
public class KaraokeQueueService {

    private Queue<SongRequest> songQueue = new LinkedList<>();

    @PostConstruct
    public void addValues() {
//        SongRequest songRequest1 = new SongRequest("Song 1", "Request 1", LocalDateTime.now());
//        SongRequest songRequest2 = new SongRequest("Song 2", "Request 2", LocalDateTime.now());
//        SongRequest songRequest3 = new SongRequest("Song 3", "Request 3", LocalDateTime.now());
//        SongRequest songRequest4 = new SongRequest("Song 4", "Request 4", LocalDateTime.now());
//        enqueue(songRequest1);
//        enqueue(songRequest2);
//        enqueue(songRequest3);
//        enqueue(songRequest4);
    }

    public void enqueue(SongRequest songRequest) {
        songQueue.add(songRequest);
    }

    public Optional<SongRequest> dequeue() {
        return Optional.ofNullable(songQueue.poll());
    }

    public List<SongRequest> getQueueContent() {
        return new ArrayList<>(songQueue);
    }
}
