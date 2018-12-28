package com.springgears.reactivewebclient.listener;

import com.springgears.reactivewebclient.domain.Person;
import com.springgears.reactivewebclient.service.PersonWebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonRetrieveListener implements ApplicationListener<ContextRefreshedEvent> {

    private final PersonWebClientService personWebClientService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        personWebClientService
                .retrieveAll()
                .subscribe(person -> log.info("Retrieved as part of a batch: " + person.toString()));

        personWebClientService
                .retrieveById(1L)
                .subscribe(person -> log.info("Retrieved by id: " + person.toString()));

        personWebClientService
                .save(Mono.just(new Person(5L, "Jack", "Black")))
                .subscribe(person -> log.info("Saved person: " + person.toString()));

        personWebClientService
                .getFullClientResponse()
                .subscribe(clientResponse -> {
                    log.info("Status code: " + clientResponse.statusCode());
                    log.info("Content type header: " + clientResponse.headers().header("Content-Type"));
                });
    }
}
