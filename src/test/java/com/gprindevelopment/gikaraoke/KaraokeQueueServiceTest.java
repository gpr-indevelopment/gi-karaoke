package com.gprindevelopment.gikaraoke;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class KaraokeQueueServiceTest {

    private final CurrentlyPlayingService currentlyPlayingService = new CurrentlyPlayingService();

    private final KaraokeQueueService karaokeQueueService = new KaraokeQueueService(currentlyPlayingService);

    @Test
    public void whenKaraokeEnqueuesThenGetsContentInTheRightOrder() {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        SongRequest songRequest2 = new SongRequest("Song 2", "Requester 2", LocalDateTime.now());
        karaokeQueueService.addSong(songRequest1);
        karaokeQueueService.addSong(songRequest2);
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
        karaokeQueueService.addSong(songRequest1);
        karaokeQueueService.addSong(songRequest2);
        karaokeQueueService.addSong(songRequest3);
        Optional<SongRequest> dequeuedOpt = karaokeQueueService.playNextSong();
        assertThat(dequeuedOpt).isPresent();
        assertThat(dequeuedOpt.get()).isEqualTo(songRequest1);
        List<SongRequest> songs = karaokeQueueService.getQueueContent();
        assertThat(songs).hasSize(2);
        assertThat(songs.get(0)).isEqualTo(songRequest2);
        assertThat(songs.get(1)).isEqualTo(songRequest3);
    }

    @Test
    public void whenKaraokeIsEmptyThenDoesntDequeue() {
        Optional<SongRequest> dequeuedOpt = karaokeQueueService.playNextSong();
        assertThat(dequeuedOpt).isNotPresent();
    }

    @Test
    public void whenDequeueAndRevertQueueStaysTheSame() {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        SongRequest songRequest2 = new SongRequest("Song 2", "Requester 2", LocalDateTime.now());
        SongRequest songRequest3 = new SongRequest("Song 3", "Requester 3", LocalDateTime.now());
        karaokeQueueService.addSong(songRequest1);
        karaokeQueueService.addSong(songRequest2);
        karaokeQueueService.addSong(songRequest3);
        List<SongRequest> songs = karaokeQueueService.getQueueContent();
        assertThat(songs).hasSize(3);
        assertThat(songs.get(0)).isEqualTo(songRequest1);
        assertThat(songs.get(1)).isEqualTo(songRequest2);
        assertThat(songs.get(2)).isEqualTo(songRequest3);
        Optional<SongRequest> dequeuedOpt = karaokeQueueService.playNextSong();
        assertThat(dequeuedOpt).isPresent();
        assertThat(dequeuedOpt.get()).isEqualTo(songRequest1);
        karaokeQueueService.revert();
        assertThat(songs).hasSize(3);
        assertThat(songs.get(0)).isEqualTo(songRequest1);
        assertThat(songs.get(1)).isEqualTo(songRequest2);
        assertThat(songs.get(2)).isEqualTo(songRequest3);
    }

    @Test
    public void whenDequeueThenThatsCurrentlyPlaying() {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        karaokeQueueService.addSong(songRequest1);
        assertThat(karaokeQueueService.playNextSong()).isPresent();
        assertThat(currentlyPlayingService.getCurrentlyPlaying().get()).isEqualTo(songRequest1);
    }

    @Test
    public void whenDequeueTwiceAndRevertThenFirstSongIsPlaying() {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        SongRequest songRequest2 = new SongRequest("Song 2", "Requester 2", LocalDateTime.now());
        karaokeQueueService.addSong(songRequest1);
        karaokeQueueService.addSong(songRequest2);
        assertThat(karaokeQueueService.playNextSong()).isPresent();
        assertThat(currentlyPlayingService.getCurrentlyPlaying().get()).isEqualTo(songRequest1);
        assertThat(karaokeQueueService.playNextSong()).isPresent();
        assertThat(currentlyPlayingService.getCurrentlyPlaying().get()).isEqualTo(songRequest2);
        karaokeQueueService.revert();
        assertThat(currentlyPlayingService.getCurrentlyPlaying().get()).isEqualTo(songRequest1);
    }

}