package com.patientmanager.patient_service.exception;

public class PatientNotFoundException extends RuntimeException {
    
    public PatientNotFoundException(String message) {
        super(message);
    }

}
