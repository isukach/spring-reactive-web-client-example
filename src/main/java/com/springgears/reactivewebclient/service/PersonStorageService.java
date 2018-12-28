package com.springgears.reactivewebclient.service;

import com.springgears.reactivewebclient.domain.Person;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PersonStorageService {

    private static List<Person> PEOPLE = Arrays.asList(
            new Person(1L, "Michael", "Jackson"),
            new Person(2L, "Chuck", "Norris"),
            new Person(3L, "Sarah", "Connor"),
            new Person(4L, "Les", "Grossman")
    );

    public Flux<Person> getPeople() {
        return Flux.fromIterable(PEOPLE);
    }

    public Mono<Person> getById(Long id) {
        if (id == null || id < 1 || id > PEOPLE.size()) {
            return Mono.empty();
        }
        return Mono.just(PEOPLE.get(id.intValue() - 1));
    }

    public Mono<Person> savePerson(Mono<Person> personMono) {
        return personMono.map(person -> {
            log.info("Saving person: " + person);
            return person;
        });
    }
}
