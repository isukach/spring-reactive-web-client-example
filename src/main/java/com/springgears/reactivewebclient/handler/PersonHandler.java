package com.springgears.reactivewebclient.handler;

import com.springgears.reactivewebclient.domain.Person;
import com.springgears.reactivewebclient.service.PersonStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class PersonHandler {

    private final PersonStorageService personStorageService;

    @Autowired
    public PersonHandler(PersonStorageService personStorageService) {
        this.personStorageService = personStorageService;
    }

    @Bean
    public RouterFunction<ServerResponse> personRouterFunction() {
        return nest(path("/api/v1/person"),
                route(GET("/all"), this::getAllPeople)
                .andRoute(GET("/{id}"), this::getPersonById)
                .andRoute(POST("").and(contentType(APPLICATION_JSON)), this::savePerson)
        );
    }

    private Mono<ServerResponse> savePerson(ServerRequest request) {
        Mono<Person> body = request.body(BodyExtractors.toMono(Person.class));
        return ServerResponse.ok().body(personStorageService.savePerson(body), Person.class);
    }

    private Mono<ServerResponse> getPersonById(ServerRequest request) {
        Long id = Long.valueOf(request.pathVariable("id"));
        return ServerResponse.ok().body(personStorageService.getById(id), Person.class);
    }

    private Mono<ServerResponse> getAllPeople(ServerRequest ignored) {
        return ServerResponse.ok().body(personStorageService.getPeople(), Person.class);
    }
}
