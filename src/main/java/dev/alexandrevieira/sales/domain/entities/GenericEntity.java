package dev.alexandrevieira.sales.domain.entities;

public interface GenericEntity<ID> {
    boolean allFieldsAreNullOrEmpty();

    ID getId();

    void setId(ID id);

    Object toDTO();
}
