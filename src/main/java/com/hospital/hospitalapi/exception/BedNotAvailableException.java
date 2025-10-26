package com.hospital.hospitalapi.exception;

public class BedNotAvailableException extends RuntimeException {
    public BedNotAvailableException(String message) {
        super(message);
    }
}
