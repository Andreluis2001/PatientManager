package com.patientmanager.patient_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.patientmanager.patient_service.dto.CreatePatientRequestDto;
import com.patientmanager.patient_service.dto.PatientResponseDto;
import com.patientmanager.patient_service.dto.UpdatePatientRequestDto;
import com.patientmanager.patient_service.exception.EmailAlreadyExistsException;
import com.patientmanager.patient_service.exception.PatientNotFoundException;
import com.patientmanager.patient_service.grpc.BillingServiceGrpcClient;
import com.patientmanager.patient_service.kafka.KafkaProducer;
import com.patientmanager.patient_service.mapper.PatientMapper;
import com.patientmanager.patient_service.model.Patient;
import com.patientmanager.patient_service.repository.PatientRepository;
import patient.events.PatientEvent;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final BillingServiceGrpcClient billingServiceClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper,
            BillingServiceGrpcClient billingServiceClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.billingServiceClient = billingServiceClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDto> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(patientMapper::toDto)
                .toList();
    }

    public PatientResponseDto createPatient(CreatePatientRequestDto patientRequestDto){
        if (patientRepository.existsByEmail(patientRequestDto.email())){
            throw new EmailAlreadyExistsException(
                "A patient with this email already exists: " +  
                patientRequestDto.email()
            );
        }
        Patient newPatient = patientMapper.toModel(patientRequestDto);
        Patient savedPatient = patientRepository.save(newPatient);
        billingServiceClient.createBillingAccount(
            savedPatient.getId().toString(), 
            savedPatient.getName(), 
            savedPatient.getEmail()
        );
        kafkaProducer.sendEvent(savedPatient);
        return patientMapper.toDto(savedPatient);
    }

    public PatientResponseDto updatePatient(
        UUID id, 
        UpdatePatientRequestDto patientRequestDto
    ){
        Patient patient = patientRepository.findById(id).orElseThrow(
            () -> new PatientNotFoundException("Patient not found with id: " + id)
        );

        if (patientRepository.existsByEmailAndIdNot(patientRequestDto.email(), id)){
            throw new EmailAlreadyExistsException(
                "A patient with this email already exists: " +  
                patientRequestDto.email()
            );
        }

        patient.setName(patientRequestDto.name());
        patient.setEmail(patientRequestDto.email());
        patient.setAddress(patientRequestDto.address());
        patient.setDateOfBirth(patientRequestDto.dateOfBirth());

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toDto(updatedPatient); 
    }

    public void deletePatient(UUID id){
        if (!patientRepository.existsById(id)){
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}
