package com.example.service;

import com.example.model.MovieInfo;
import com.example.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureWebTestClient(timeout = "3600000")
@WebFluxTest
class MovieInfoServiceImplTest {

    @MockBean
    private MovieInfoService movieInfoService;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        /*var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository
                .deleteAll()
                .thenMany(movieInfoRepository.saveAll(movieinfos))
                .blockLast();*/
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addMovieInfoService() {

        var movie = new MovieInfo("133", "Movie1", 2020,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-08-01"));

        when(movieInfoService.addMovieInfoService(movie)).thenReturn(Mono.just(movie));

        webTestClient
                .post()
                .uri("/v1/movieinfo")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult->{
                    var savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(savedMovieInfo).getMovieInfoId() != null;
                });

       Mockito.verify(movieInfoService, times(1)).addMovieInfoService(movie);
    }

    @Test
    void getAllMovieInfo() {

        var movie = new MovieInfo("133", "Dr. Strange3", 2020,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-08-01"));

        when(movieInfoService.getAllMovieInfo()).thenReturn(Flux.just(movie));

        webTestClient
                .get()
                .uri("/v1/movieinfo")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
    }

    @Test
    void testGetByID(){

        var movie = new MovieInfo("133", "Dr. Strange3", 2020,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-08-01"));

        when(movieInfoService.getMovieById("133")).thenReturn(Mono.just(movie));
        webTestClient
                .get()
                .uri("/v1/movieinfo/133")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(response->{
                   assert response.getResponseBody().getMovieInfoId().equals("133");
                });
    }

    @Test
    void testUpdate(){

        var movie = new MovieInfo("133", "Dr. Strange3", 2020,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-08-02"));

        when(movieInfoService.updateMovieById("133", movie)).thenReturn(Mono.just(movie));
        webTestClient
                .put()
                .uri("/v1/movieinfo/133")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(response->{
                    assert response.getResponseBody().getMovieInfoId().equals("133");
                    assert response.getResponseBody().getRelease_date().equals(LocalDate.parse("2020-08-02"));
                });
    }

    @Test
    void testDelete(){

        when(movieInfoService.deleteMovieById("133")).thenReturn(null);
        webTestClient
                .delete()
                .uri("/v1/movieinfo/133")
                .exchange()
                .expectStatus()
                .isNoContent()
                .returnResult(Void.class);
    }

    @Test
    void addMovieInfoServiceWhenYearIsNull() {

        var movie = new MovieInfo("133", "", null,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-08-01"));

        webTestClient
                .post()
                .uri("/v1/movieinfo")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(response->{
                    var body = response.getResponseBody();
                    System.out.println(body);
                    assertEquals("Name cannot be empty,Year cannot be null", body);
                });

    }


}