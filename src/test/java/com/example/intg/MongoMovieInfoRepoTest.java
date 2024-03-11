package com.example.intg;

import com.example.model.MovieInfo;
import com.example.repository.MovieInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
@ActiveProfiles("test")
public class MongoMovieInfoRepoTest {

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setup(){

        var movies = List.of(new MovieInfo("1", "Dr. Strange", 2019,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2017-08-01")),
                new MovieInfo("2", "Dr. Strange2", 2020,
                        List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-06-01")));
        movieInfoRepository.saveAll(movies)
                .blockLast();
    }

    //@AfterEach
    void cleanup(){
        movieInfoRepository.deleteAll();
    }

    @Test
    void testFindAll(){

        var movieInfos = movieInfoRepository.findAll()
                .log();

        StepVerifier.create(movieInfos)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testFindById(){

        var movieInfo = movieInfoRepository.findById("1")
                .log();

        StepVerifier.create(movieInfo)
                .assertNext(movie->{
                    assertEquals("Dr. Strange", movie.getName());
                })
                .verifyComplete();
    }

    @Test
    void testSave(){

        var movie = new MovieInfo(null, "Dr. Strange3", 2020,
                List.of("Benedict Cumberbatch", "Rachel McAdams"), LocalDate.parse("2020-08-01"));
        var movieInfo = movieInfoRepository.save(movie)
                .log();

        StepVerifier.create(movieInfo)
                .assertNext(mv->{
                    assertNotNull(mv.getMovieInfoId());
                    System.out.println(mv.getMovieInfoId());
                    assertEquals("Dr. Strange3", mv.getName());
                })
                .verifyComplete();
    }

    @Test
    void testUpdateMovie(){

        var movieInfo = movieInfoRepository.findById("1").block();
        movieInfo.setYear(2017);

        var updateMovie = movieInfoRepository.save(movieInfo).log();

        StepVerifier.create(updateMovie)
                .assertNext(mv->{
                    assertNotNull(mv.getMovieInfoId());
                    System.out.println(mv.getMovieInfoId());
                    assertEquals(2017, mv.getYear());
                })
                .verifyComplete();
    }

    @Test
    void testDeleteMovie(){

        movieInfoRepository.deleteById("2").block();

        var flux = movieInfoRepository.findAll().log();

        /*StepVerifier.create(flux)
                .expectNextCount(1)
                .verifyComplete();*/
    }
}
