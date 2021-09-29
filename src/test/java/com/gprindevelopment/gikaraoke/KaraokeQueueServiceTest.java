package com.gprindevelopment.gikaraoke;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class KaraokeQueueServiceTest {

    @Test
    public void whenKaraokeEnqueuesThenGetsContentInTheRightOrder() {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        SongRequest songRequest2 = new SongRequest("Song 2", "Requester 2", LocalDateTime.now());
        KaraokeQueueService karaokeQueueService = new KaraokeQueueService();
        karaokeQueueService.enqueue(songRequest1);
        karaokeQueueService.enqueue(songRequest2);
        List<SongRequest> songs = karaokeQueueService.getQueueContent();
        assertThat(songs).hasSize(2);
        assertThat(songs.get(0)).isEqualTo(songRequest1);
        assertThat(songs.get(1)).isEqualTo(songRequest2);
    }

    @Test
    public void whenKaraokeEnqueuesAndDequeuesThenGetsContentInRightOrder() {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        SongRequest songRequest2 = new SongRequest("Song 2", "Requester 2", LocalDateTime.now());
        SongRequest songRequest3 = new SongRequest("Song 3", "Requester 3", LocalDateTime.now());
        KaraokeQueueService karaokeQueueService = new KaraokeQueueService();
        karaokeQueueService.enqueue(songRequest1);
        karaokeQueueService.enqueue(songRequest2);
        karaokeQueueService.enqueue(songRequest3);
        Optional<SongRequest> dequeuedOpt = karaokeQueueService.dequeue();
        assertThat(dequeuedOpt).isPresent();
        assertThat(dequeuedOpt.get()).isEqualTo(songRequest1);
        List<SongRequest> songs = karaokeQueueService.getQueueContent();
        assertThat(songs).hasSize(2);
        assertThat(songs.get(0)).isEqualTo(songRequest2);
        assertThat(songs.get(1)).isEqualTo(songRequest3);
    }

    @Test
    public void whenKaraokeIsEmptyThenDoesntDequeue() {
        KaraokeQueueService karaokeQueueService = new KaraokeQueueService();
        Optional<SongRequest> dequeuedOpt = karaokeQueueService.dequeue();
        assertThat(dequeuedOpt).isNotPresent();
    }

}