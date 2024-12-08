package org.MIFI.exceptions;

public class NotFoundEntityException extends RuntimeException  {
    public NotFoundEntityException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }
}
