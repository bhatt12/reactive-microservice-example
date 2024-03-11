package com.example.service;

import com.example.model.MovieInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface MovieInfoService {
    Mono<MovieInfo> addMovieInfoService(MovieInfo movieInfo);

    Flux<MovieInfo> getAllMovieInfo();

    Mono<MovieInfo> getMovieById(String id);

    Mono<MovieInfo> updateMovieById(String id, MovieInfo movieInfo);

    Mono<Void> deleteMovieById(String id);
}
