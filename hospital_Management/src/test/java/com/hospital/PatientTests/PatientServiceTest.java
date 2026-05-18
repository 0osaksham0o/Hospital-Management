package com.hospital.PatientTests;

import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PatientRepository;
import com.hospital.service.impl.PatientServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository repo;

    @InjectMocks
    private PatientServiceImpl service;

    private Patient patient;
    private Physician physician;

    @BeforeEach
    void setup() {

        physician = new Physician();
        physician.setEmployeeId(10);

        patient = new Patient(
                101,
                "John",
                "Delhi",
                "9999999999",
                5001,
                physician
        );
    }

    // ================= GET ALL =================

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(patient));

        List<Patient> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testGetAllPaginated() {
        Page<Patient> page = new PageImpl<>(List.of(patient));

        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Patient> result = service.getAll(PageRequest.of(0,5));

        assertEquals(1, result.getTotalElements());
    }

    // ================= GET BY ID =================

    @Test
    void testGetByIdSuccess() {
        when(repo.findById(101)).thenReturn(Optional.of(patient));

        Patient result = service.getById(101);

        assertNotNull(result);
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(101)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(101));
    }

    // ================= EXISTS =================

    @Test
    void testExistsById() {
        when(repo.existsById(101)).thenReturn(true);

        assertTrue(service.existsById(101));
    }

    // ================= SAVE =================

    @Test
    void testSave() {
        when(repo.save(patient)).thenReturn(patient);

        Patient saved = service.save(patient);

        assertNotNull(saved);
        verify(repo).save(patient);
    }

    // ================= DELETE =================

    @Test
    void testDelete() {
        when(repo.findById(101)).thenReturn(Optional.of(patient));

        service.delete(101);

        verify(repo).deleteById(101);
    }

    // ================= NAME =================

    @Test
    void testGetByName() {
        when(repo.findByName("John")).thenReturn(Optional.of(patient));

        Optional<Patient> result = service.getByName("John");

        assertTrue(result.isPresent());
    }

    @Test
    void testSearchByName() {
        when(repo.findByNameContainingIgnoreCase("jo"))
                .thenReturn(List.of(patient));

        assertEquals(1, service.searchByName("jo").size());
    }

    @Test
    void testExistsByName() {
        when(repo.existsByName("John")).thenReturn(true);

        assertTrue(service.existsByName("John"));
    }

    // ================= PHONE =================

    @Test
    void testGetByPhone() {
        when(repo.findByPhone("9999999999"))
                .thenReturn(Optional.of(patient));

        assertTrue(service.getByPhone("9999999999").isPresent());
    }

    // ================= ADDRESS =================

    @Test
    void testGetByAddress() {
        when(repo.findByAddress("Delhi"))
                .thenReturn(List.of(patient));

        assertEquals(1, service.getByAddress("Delhi").size());
    }

    // ================= INSURANCE =================

    @Test
    void testGetByInsuranceId() {
        when(repo.findByInsuranceId(5001))
                .thenReturn(Optional.of(patient));

        assertTrue(service.getByInsuranceId(5001).isPresent());
    }

    @Test
    void testExistsByInsuranceId() {
        when(repo.existsByInsuranceId(5001)).thenReturn(true);

        assertTrue(service.existsByInsuranceId(5001));
    }

    // ================= PCP =================

    @Test
    void testGetByPcp() {
        when(repo.findByPrimaryCarePhysician_EmployeeId(10))
                .thenReturn(List.of(patient));

        assertEquals(1, service.getByPcp(10).size());
    }

    @Test
    void testCountByPcp() {
        when(repo.countByPrimaryCarePhysician_EmployeeId(10))
                .thenReturn(2L);

        assertEquals(2, service.countByPcp(10));
    }

    // ================= ORDER =================

    @Test
    void testGetAllOrderedByName() {
        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(patient));

        assertEquals(1, service.getAllOrderedByName().size());
    }
}