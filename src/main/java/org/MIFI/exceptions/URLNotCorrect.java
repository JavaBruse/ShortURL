package org.MIFI.exceptions;

public class URLNotCorrect extends RuntimeException {
    public URLNotCorrect(String message) {
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
