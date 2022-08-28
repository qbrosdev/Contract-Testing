package com.qbros.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonResource {

    private final PersonService personService;

    @GetMapping
    public PersonDto getAll() {
        return personService.getAll().get(0);
    }
}
