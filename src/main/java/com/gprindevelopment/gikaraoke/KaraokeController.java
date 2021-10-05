package com.gprindevelopment.gikaraoke;

import org.springframework.http.HttpStatus;
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

    private final PushNotificationService pushNotificationService;

    public KaraokeController(KaraokeQueueService karaokeQueueService, PushNotificationService pushNotificationService) {
        this.karaokeQueueService = karaokeQueueService;
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addSong(@RequestBody SongRequest songRequest) {
        karaokeQueueService.addSong(songRequest);
        pushNotificationService.pushFrontendUpdate();
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
        pushNotificationService.pushFrontendUpdate();
    }

    @PostMapping("/next-song")
    public void nextSong() {
        karaokeQueueService.playNextSong();
        pushNotificationService.pushFrontendUpdate();
    }
}
