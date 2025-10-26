package com.hospital.hospitalapi.exception.ai;

public class ImageAnalysisFailedException extends RuntimeException {
    public ImageAnalysisFailedException(String message) {
        super(message);
    }
    
    public ImageAnalysisFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}