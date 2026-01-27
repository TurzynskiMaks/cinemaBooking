package pl.maksturzynski.cinemabooking.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MovieExternalDto (
    @JsonProperty("Title") String title,
    @JsonProperty("Genre") String genre,
    @JsonProperty("Rated") String rated,
    @JsonProperty("Director") String director,
    @JsonProperty("Actors") String actors,
    @JsonProperty("Poster") String posterUrl,
    @JsonProperty("Response") String response
) {}
