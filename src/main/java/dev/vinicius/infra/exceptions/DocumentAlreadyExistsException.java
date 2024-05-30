package dev.vinicius.infra.exceptions;

public class DocumentAlreadyExistsException extends RuntimeException {

    public DocumentAlreadyExistsException(String message) {
        super(message);
    }
}