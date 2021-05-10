package dev.alexandrevieira.sales.domain.entities;

import org.springframework.stereotype.Repository;

@Repository
public interface GenericEntity<ID> {
    boolean allFieldsAreNullOrEmpty();

    ID getId();

    void setId(ID id);

    Object toDTO();
}
