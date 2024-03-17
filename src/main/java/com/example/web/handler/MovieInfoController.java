package com.example.web.handler;

import com.example.model.MovieInfo;
import com.example.service.MovieInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MovieInfoController {

    private MovieInfoService movieInfoService;

    public MovieInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }

    @PostMapping("/movieinfo")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> saveMovieInfo(@RequestBody @Valid MovieInfo movieInfo){
        return movieInfoService.addMovieInfoService(movieInfo);
    }

    @GetMapping("/movieinfo")
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> getAllMovieInfo(){
        return movieInfoService.getAllMovieInfo();
    }

    @GetMapping("/movieinfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<MovieInfo>> getMovieById(@PathVariable String id){
        return movieInfoService.getMovieById(id)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PutMapping("/movieinfo/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> updateMovieById(@PathVariable String id, @RequestBody MovieInfo movieInfo){
        return movieInfoService.updateMovieById(id, movieInfo);
    }

    @DeleteMapping("/movieinfo/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteMovieById(@PathVariable String id){
        return movieInfoService.deleteMovieById(id);
    }
}
