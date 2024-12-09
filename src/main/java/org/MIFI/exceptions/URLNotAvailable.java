package org.MIFI.exceptions;

public class URLNotAvailable extends RuntimeException {
    public URLNotAvailable(String message) {
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
