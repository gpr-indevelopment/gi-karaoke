package com.gprindevelopment.gikaraoke;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class SongRequest {

    private String songName;

    private String requester;

    private LocalDateTime requestedAt;

    public SongRequest(
            @JsonProperty("songName") String songName,
            @JsonProperty("requester") String requester,
            @JsonProperty("requestedAt") LocalDateTime requestedAt
    ) {
        this.songName = songName;
        this.requester = requester;
        this.requestedAt = requestedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongRequest that = (SongRequest) o;
        return songName.equals(that.songName) && requester.equals(that.requester) && requestedAt.equals(that.requestedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songName, requester, requestedAt);
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }
}
