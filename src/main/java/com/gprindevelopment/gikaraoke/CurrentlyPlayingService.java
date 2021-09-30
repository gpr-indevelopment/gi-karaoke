package com.gprindevelopment.gikaraoke;

import org.springframework.stereotype.Service;

import java.util.EmptyStackException;
import java.util.Optional;
import java.util.Stack;

@Service
public class CurrentlyPlayingService {

    private final Stack<SongRequest> currentlyPlayingStack = new Stack<>();

    public void setCurrentlyPlaying(SongRequest songRequest) {
        currentlyPlayingStack.push(songRequest);
    }

    public Optional<SongRequest> getCurrentlyPlaying() {
        try {
            return Optional.ofNullable(currentlyPlayingStack.peek());
        } catch (EmptyStackException e) {
            return Optional.empty();
        }
    }

    public SongRequest pop() {
        return currentlyPlayingStack.pop();
    }
}
