package com.example.lab789.domain.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}