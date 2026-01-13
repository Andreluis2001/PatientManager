package com.patientmanager.patient_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patientmanager.patient_service.dto.CreatePatientRequestDto;
import com.patientmanager.patient_service.dto.PatientResponseDto;
import com.patientmanager.patient_service.dto.UpdatePatientRequestDto;
import com.patientmanager.patient_service.service.PatientService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDto>> getAllPatients(){
        List<PatientResponseDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    };

    @PostMapping
    public ResponseEntity<PatientResponseDto> createPatient(
        @Valid @RequestBody CreatePatientRequestDto patientRequestDto
    ) {
        PatientResponseDto createdResponseDto = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok(createdResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(
        @PathVariable UUID id,
        @Valid @RequestBody UpdatePatientRequestDto patientRequestDto
    ){
        PatientResponseDto updatedPatient = patientService.updatePatient(
            id,
            patientRequestDto
        );
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
    
}
