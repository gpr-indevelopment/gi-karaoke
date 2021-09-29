package com.gprindevelopment.gikaraoke;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
public class KaraokeQueueService {

    private Queue<SongRequest> songQueue = new LinkedList<>();

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
