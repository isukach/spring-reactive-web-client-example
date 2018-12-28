package com.springgears.reactivewebclient.service;

import com.springgears.reactivewebclient.domain.Person;
import javax.annotation.PostConstruct;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonWebClientService {

    @Value("${server.port}")
    private String port;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.create("http://localhost:" + port + "/api/v1/person");
    }

    public Flux<Person> retrieveAll() {
        return webClient.get()
                .uri("/all")
                .retrieve()
                .bodyToFlux(Person.class);
    }

    public Mono<Person> retrieveById(Long id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Person.class);
    }

    public Mono<Person> save(Publisher<Person> person) {
        return webClient.post()
                .body(person, Person.class)
                .retrieve()
                .bodyToMono(Person.class);
    }

    public Mono<ClientResponse> getFullClientResponse() {
        return webClient.get()
                .uri("/all")
                .exchange();
    }
}
