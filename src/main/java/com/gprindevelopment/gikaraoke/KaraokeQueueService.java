package com.gprindevelopment.gikaraoke;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class KaraokeQueueService {

    private final LinkedList<SongRequest> songQueue = new LinkedList<>();

    private final CurrentlyPlayingService currentlyPlayingService;

    public KaraokeQueueService(CurrentlyPlayingService currentlyPlayingService) {
        this.currentlyPlayingService = currentlyPlayingService;
    }

    @PostConstruct
    public void addValues() {
//        SongRequest songRequest1 = new SongRequest("Song 1", "Request 1", LocalDateTime.now());
//        SongRequest songRequest2 = new SongRequest("Song 2", "Request 2", LocalDateTime.now());
//        SongRequest songRequest3 = new SongRequest("Song 3", "Request 3", LocalDateTime.now());
//        SongRequest songRequest4 = new SongRequest("Song 4", "Request 4", LocalDateTime.now());
//        addSong(songRequest1);
//        addSong(songRequest2);
//        addSong(songRequest3);
//        addSong(songRequest4);
    }

    public void addSong(SongRequest songRequest) {
        songQueue.add(songRequest);
    }

    public Optional<SongRequest> playNextSong() {
        Optional<SongRequest> dequeuedOpt = Optional.ofNullable(songQueue.poll());
        dequeuedOpt.ifPresent(currentlyPlayingService::setCurrentlyPlaying);
        return dequeuedOpt;
    }

    public List<SongRequest> getQueueContent() {
        return new ArrayList<>(songQueue);
    }

    public void revert() {
        songQueue.addFirst(currentlyPlayingService.pop());
    }

    public Optional<SongRequest> getCurrentlyPlaying() {
        return currentlyPlayingService.getCurrentlyPlaying();
    }
}
