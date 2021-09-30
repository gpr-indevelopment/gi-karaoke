package com.gprindevelopment.gikaraoke;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class KaraokeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KaraokeQueueService karaokeQueueService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void whenAddSongRequestThenEnqueues() throws Exception {
        SongRequest songRequest = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI)
                .content(objectMapper.writeValueAsString(songRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        Optional<SongRequest> enqueuedRequestOpt = karaokeQueueService.playNextSong();
        assertThat(enqueuedRequestOpt).isPresent();
        assertThat(enqueuedRequestOpt.get()).isEqualTo(songRequest);
    }

    @Test
    public void whenAddSongTwoSongRequestsThenGetsInRightOrder() throws Exception {
        SongRequest songRequest1 = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        SongRequest songRequest2 = new SongRequest("Song 2", "Requester 2", LocalDateTime.now());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI)
                .content(objectMapper.writeValueAsString(songRequest1))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI)
                .content(objectMapper.writeValueAsString(songRequest2))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        String resultString = this.mockMvc.perform(get(KaraokeController.ROOT_URI))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        JavaType type = objectMapper.getTypeFactory().
                constructCollectionType(List.class, SongRequest.class);
        List<SongRequest> songRequests = objectMapper.readValue(resultString, type);
        assertThat(songRequests).hasSize(2);
        assertThat(songRequests.get(0)).isEqualTo(songRequest1);
        assertThat(songRequests.get(1)).isEqualTo(songRequest2);
    }

    @Test
    public void whenAddSongAndPlayNextSongThenThatsCurrentlyPlaying() throws Exception {
        SongRequest songRequest = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI)
                .content(objectMapper.writeValueAsString(songRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        Optional<SongRequest> enqueuedRequestOpt = karaokeQueueService.playNextSong();
        assertThat(enqueuedRequestOpt).isPresent();
        assertThat(enqueuedRequestOpt.get()).isEqualTo(songRequest);
        String resultString = this.mockMvc.perform(get(KaraokeController.ROOT_URI + "/currently-playing"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SongRequest returnedRequest = objectMapper.readValue(resultString, SongRequest.class);
        assertThat(returnedRequest).isEqualTo(songRequest);
    }

    @Test
    public void whenAddSongAndPlayNextSongViaAPIThenThatsCurrentlyPlaying() throws Exception {
        SongRequest songRequest = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI)
                .content(objectMapper.writeValueAsString(songRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI + "/next-song")).andExpect(status().isOk());
        String resultString = this.mockMvc.perform(get(KaraokeController.ROOT_URI + "/currently-playing"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SongRequest returnedRequest = objectMapper.readValue(resultString, SongRequest.class);
        assertThat(returnedRequest).isEqualTo(songRequest);
    }

    @Test
    public void whenGetCurrentlyPlayingAndIsEmptyThen404() throws Exception {
        this.mockMvc.perform(get(KaraokeController.ROOT_URI + "/currently-playing"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenAddSongAndPlaySongAndRevertThenCurrentlyPlaying404() throws Exception {
        SongRequest songRequest = new SongRequest("Song 1", "Requester 1", LocalDateTime.now());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI)
                .content(objectMapper.writeValueAsString(songRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
        this.mockMvc.perform(post(KaraokeController.ROOT_URI + "/next-song")).andExpect(status().isOk());
        String resultString = this.mockMvc.perform(get(KaraokeController.ROOT_URI + "/currently-playing"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SongRequest returnedRequest = objectMapper.readValue(resultString, SongRequest.class);
        assertThat(returnedRequest).isEqualTo(songRequest);
        this.mockMvc.perform(post(KaraokeController.ROOT_URI + "/revert")).andExpect(status().isOk());
        this.mockMvc.perform(get(KaraokeController.ROOT_URI + "/currently-playing"))
                .andExpect(status().isNotFound());
    }
}
