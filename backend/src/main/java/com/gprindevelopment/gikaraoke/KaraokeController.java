package com.gprindevelopment.gikaraoke;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(KaraokeController.ROOT_URI)
public class KaraokeController {

    public static final String ROOT_URI = "/karaoke";

    private final KaraokeQueueService karaokeQueueService;

    public KaraokeController(KaraokeQueueService karaokeQueueService) {
        this.karaokeQueueService = karaokeQueueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void enqueueRequest(@RequestBody SongRequest songRequest) {
        karaokeQueueService.enqueue(songRequest);
    }

    @GetMapping
    public List<SongRequest> getRequests() {
        return karaokeQueueService.getQueueContent();
    }
}
