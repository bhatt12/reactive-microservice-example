package com.example.service;

import com.example.model.MovieInfo;
import com.example.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MovieInfoServiceImpl implements MovieInfoService{

    private MovieInfoRepository movieInfoRepository;

    public MovieInfoServiceImpl(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    @Override
    public Mono<MovieInfo> addMovieInfoService(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo)
                .log();
    }

    @Override
    public Flux<MovieInfo> getAllMovieInfo() {
        return movieInfoRepository.findAll()
                .log();
    }

    @Override
    public Mono<MovieInfo> getMovieById(String id) {
        return movieInfoRepository.findById(id)
                .log();
    }

    @Override
    public Mono<MovieInfo> updateMovieById(String id, MovieInfo movieInfo) {
        Mono<MovieInfo> movie = movieInfoRepository.findById(id)
                .flatMap(movieInfo1 -> {
                    movieInfo1.setYear(movieInfo.getYear());
                    movieInfo1.setCast(movieInfo.getCast());
                    movieInfo1.setName(movieInfo.getName());
                    movieInfo1.setRelease_date(movieInfo.getRelease_date());

                    return movieInfoRepository.save(movieInfo1);
                });
        return movie;
    }

    @Override
    public Mono<Void> deleteMovieById(String id) {
        return movieInfoRepository.deleteById(id).log();
    }
}
