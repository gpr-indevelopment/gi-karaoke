package com.gprindevelopment.gikaraoke;

import org.springframework.stereotype.Service;

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
