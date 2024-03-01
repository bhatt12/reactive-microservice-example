package com.example.web.handler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class ReactiveController {

    @GetMapping("/value")
    public Mono<String> getValue(){
        return Mono.just("TestValue")
                .log();
    }

    @GetMapping("/values")
    public Flux<String> getValues(){
        return Flux.fromIterable(List.of("A", "B", "C"))
                .log();
    }

    @GetMapping("/int-values")
    public Flux<Integer> getIntValues(){
        return Flux.fromIterable(List.of(1,2,3))
                .log();
    }

    @GetMapping("/infinite/values")
    public Flux<Long> getInfiniteValues(){
        return Flux.interval(Duration.ofSeconds(2))
                .log();
    }

}
