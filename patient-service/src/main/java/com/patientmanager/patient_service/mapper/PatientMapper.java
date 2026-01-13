package com.patientmanager.patient_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.patientmanager.patient_service.dto.CreatePatientRequestDto;
import com.patientmanager.patient_service.dto.PatientResponseDto;
import com.patientmanager.patient_service.dto.UpdatePatientRequestDto;
import com.patientmanager.patient_service.model.Patient;

@Mapper(componentModel="spring")
public interface PatientMapper {

    PatientResponseDto toDto(Patient patient);

    @Mapping(target = "id", ignore = true)
    Patient toModel(CreatePatientRequestDto patientRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registeredDate", ignore = true)
    Patient toModel(UpdatePatientRequestDto patientRequestDto);

}
