package com.hospital.AffiliatedWithTest;

import com.hospital.entity.*;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AffiliatedWithRepository;
import com.hospital.service.impl.AffiliatedWithServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AffiliatedWithServiceTest {

    @Mock
    private AffiliatedWithRepository repo;

    @InjectMocks
    private AffiliatedWithServiceImpl service;

    private AffiliatedWith affiliation;
    private AffiliatedWithId id;
    private Physician physician;
    private Department department;

    @BeforeEach
    void init() {

        physician = new Physician();
        physician.setEmployeeId(1);

        department = new Department();
        department.setDepartmentId(10);

        id = new AffiliatedWithId(1, 10);

        affiliation =
                new AffiliatedWith(id, physician, department, true);
    }

    // ================= BASIC CRUD =================

    @Test
    void getAll_shouldReturnList() {

        when(repo.findAll()).thenReturn(List.of(affiliation));

        List<AffiliatedWith> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void getAllPageable_shouldReturnPage() {

        Page<AffiliatedWith> page =
                new PageImpl<>(List.of(affiliation));

        when(repo.findAll(any(Pageable.class))).thenReturn(page);

        Page<AffiliatedWith> result =
                service.getAll(PageRequest.of(0,5));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getById_shouldReturnEntity() {

        when(repo.findById(id))
                .thenReturn(Optional.of(affiliation));

        AffiliatedWith result = service.getById(id);

        assertNotNull(result);
    }

    @Test
    void getById_shouldThrowException_whenNotFound() {

        when(repo.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));
    }

    @Test
    void existsById_shouldReturnTrue() {

        when(repo.existsById(id)).thenReturn(true);

        assertTrue(service.existsById(id));
    }

    @Test
    void save_shouldPersistEntity() {

        when(repo.save(affiliation)).thenReturn(affiliation);

        AffiliatedWith saved = service.save(affiliation);

        assertEquals(affiliation, saved);
        verify(repo).save(affiliation);
    }

    @Test
    void delete_shouldDeleteEntity() {

        when(repo.findById(id))
                .thenReturn(Optional.of(affiliation));

        service.delete(id);

        verify(repo).deleteById(id);
    }

    // ================= PHYSICIAN METHODS =================

    @Test
    void getByPhysician_shouldReturnAffiliations() {

        when(repo.findById_PhysicianId(1))
                .thenReturn(List.of(affiliation));

        List<AffiliatedWith> result =
                service.getByPhysician(1);

        assertEquals(1, result.size());
    }

    @Test
    void countByPhysician_shouldReturnCount() {

        when(repo.countById_PhysicianId(1)).thenReturn(2L);

        long count = service.countByPhysician(1);

        assertEquals(2, count);
    }

    // ================= DEPARTMENT METHODS =================

    @Test
    void getByDepartment_shouldReturnAffiliations() {

        when(repo.findById_DepartmentId(10))
                .thenReturn(List.of(affiliation));

        assertEquals(1,
                service.getByDepartment(10).size());
    }

    @Test
    void countByDepartment_shouldReturnCount() {

        when(repo.countById_DepartmentId(10)).thenReturn(3L);

        assertEquals(3,
                service.countByDepartment(10));
    }

    // ================= PRIMARY AFFILIATION =================

    @Test
    void getByDepartmentAndPrimary_shouldWork() {

        when(repo.findById_DepartmentIdAndPrimaryAffiliation(10,true))
                .thenReturn(List.of(affiliation));

        assertEquals(1,
                service.getByDepartmentAndPrimary(10,true).size());
    }

    @Test
    void getByPhysicianAndPrimary_shouldWork() {

        when(repo.findById_PhysicianIdAndPrimaryAffiliation(1,true))
                .thenReturn(List.of(affiliation));

        assertEquals(1,
                service.getByPhysicianAndPrimary(1,true).size());
    }

    @Test
    void getAllByPrimary_shouldReturnRecords() {

        when(repo.findByPrimaryAffiliation(true))
                .thenReturn(List.of(affiliation));

        assertEquals(1,
                service.getAllByPrimary(true).size());
    }

    // ================= EXISTENCE CHECK =================

    @Test
    void isAffiliated_shouldReturnTrue() {

        when(repo.existsById_PhysicianIdAndId_DepartmentId(1,10))
                .thenReturn(true);

        assertTrue(service.isAffiliated(1,10));
    }

    // ================= STREAM MAPPING =================

    @Test
    void getPhysiciansWithPrimaryAffiliation_shouldReturnPhysicians() {

        when(repo.findByPrimaryAffiliation(true))
                .thenReturn(List.of(affiliation));

        List<Physician> result =
                service.getPhysiciansWithPrimaryAffiliation();

        assertEquals(1, result.size());
    }

    @Test
    void getDepartmentsWithPrimaryAffiliations_shouldReturnDepartments() {

        when(repo.findByPrimaryAffiliation(true))
                .thenReturn(List.of(affiliation));

        List<Department> result =
                service.getDepartmentsWithPrimaryAffiliations();

        assertEquals(1, result.size());
    }
}