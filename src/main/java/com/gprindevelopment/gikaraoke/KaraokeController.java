package com.gprindevelopment.gikaraoke;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(KaraokeController.ROOT_URI)
public class KaraokeController {

    public static final String ROOT_URI = "/karaoke";

    private final KaraokeQueueService karaokeQueueService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public KaraokeController(KaraokeQueueService karaokeQueueService, SimpMessagingTemplate simpMessagingTemplate) {
        this.karaokeQueueService = karaokeQueueService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSong(@RequestBody SongRequest songRequest) {
        karaokeQueueService.addSong(songRequest);
        simpMessagingTemplate.convertAndSend("/topic/update", "Update!");
    }

    @GetMapping
    public List<SongRequest> getRequests() {
        return karaokeQueueService.getQueueContent();
    }

    @GetMapping(path = "/currently-playing")
    public SongRequest getCurrentlyPlaying() {
        return karaokeQueueService.getCurrentlyPlaying().orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Nothing is being played."
        ));
    }

    @PostMapping(path = "/revert")
    public void revert() {
        karaokeQueueService.revert();
        simpMessagingTemplate.convertAndSend("/topic/update", "Update!");
    }

    @PostMapping("/next-song")
    public void nextSong() {
        karaokeQueueService.playNextSong();
        simpMessagingTemplate.convertAndSend("/topic/update", "Update!");
    }
}
