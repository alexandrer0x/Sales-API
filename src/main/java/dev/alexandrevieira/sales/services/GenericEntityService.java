package dev.alexandrevieira.sales.services;

import dev.alexandrevieira.sales.domain.entities.GenericEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@Service
public abstract class GenericEntityService<T, ID, REPOSITORY> {
    JpaRepository<T, ID> repository;

    public GenericEntityService(REPOSITORY repository) {
        this.repository = (JpaRepository<T, ID>) repository;
    }

    public T find(ID id) {
        log.info(this.getClass().getSimpleName() + ".find(ID id)");
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(NOT_FOUND));

    }

    public List<T> filter(T filter) {
        log.info(this.getClass().getSimpleName() + ".filter(T filter)");

        if (((GenericEntity) filter).allFieldsAreNullOrEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST);
        }

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<T> example = Example.of(filter, matcher);
        List<T> list = repository.findAll(example);
        return list;
    }

    public void delete(ID id) {
        log.info(this.getClass().getSimpleName() + ".delete(ID id)");
        repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        repository.deleteById(id);
    }

    public void update(ID id, T input) {
        log.info(this.getClass().getSimpleName() + ".update(ID id, T input)");
        GenericEntity existent = (GenericEntity) repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
        ((GenericEntity) input).setId(existent.getId());
        repository.save(input);
    }

    public List<T> findAllById(Iterable<ID> ids) {
        log.info(this.getClass().getSimpleName() + ".findAllById(Iterable<ID> ids)");
        List<T> results = repository.findAllById(ids);
        return results;
    }
}