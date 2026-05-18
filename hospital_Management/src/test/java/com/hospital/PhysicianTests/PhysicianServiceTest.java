package com.hospital.PhysicianTests;

import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.service.impl.PhysicianServiceImpl;

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
public class PhysicianServiceTest {

    @Mock
    private PhysicianRepository repo;

    @Mock
    private DepartmentRepository departmentRepo;

    @InjectMocks
    private PhysicianServiceImpl service;

    private Physician physician;

    @BeforeEach
    void setup() {
        physician = new Physician(
                10,
                "Dr Smith",
                "Cardiologist",
                12345
        );
    }

    // ================= GET ALL =================

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(physician));

        List<Physician> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testGetAllPaginated() {
        Page<Physician> page = new PageImpl<>(List.of(physician));

        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Physician> result = service.getAll(PageRequest.of(0,5));

        assertEquals(1, result.getTotalElements());
    }

    // ================= GET BY ID =================

    @Test
    void testGetByIdSuccess() {
        when(repo.findById(10)).thenReturn(Optional.of(physician));

        Physician result = service.getById(10);

        assertNotNull(result);
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(10)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(10));
    }

    // ================= EXISTS =================

    @Test
    void testExistsById() {
        when(repo.existsById(10)).thenReturn(true);

        assertTrue(service.existsById(10));
    }

    // ================= SAVE =================

    @Test
    void testSave() {
        when(repo.save(physician)).thenReturn(physician);

        Physician saved = service.save(physician);

        assertNotNull(saved);
        verify(repo).save(physician);
    }

    // ================= DELETE =================

    @Test
    void testDelete() {
        when(repo.findById(10)).thenReturn(Optional.of(physician));

        service.delete(10);

        verify(repo).deleteById(10);
    }

    // ================= NAME =================

    @Test
    void testGetByName() {
        when(repo.findByName("Dr Smith")).thenReturn(Optional.of(physician));

        Optional<Physician> result = service.getByName("Dr Smith");

        assertTrue(result.isPresent());
    }

    @Test
    void testSearchByName() {
        when(repo.findByNameContainingIgnoreCase("smith"))
                .thenReturn(List.of(physician));

        assertEquals(1, service.searchByName("smith").size());
    }

    // ================= POSITION =================

    @Test
    void testGetByPosition() {
        when(repo.findByPosition("Cardiologist"))
                .thenReturn(List.of(physician));

        assertEquals(1, service.getByPosition("Cardiologist").size());
    }

    @Test
    void testSearchByPosition() {
        when(repo.findByPositionContainingIgnoreCase("card"))
                .thenReturn(List.of(physician));

        assertEquals(1, service.searchByPosition("card").size());
    }

    // ================= SSN =================

    @Test
    void testGetBySsn() {
        when(repo.findBySsn(12345)).thenReturn(Optional.of(physician));

        assertTrue(service.getBySsn(12345).isPresent());
    }

    @Test
    void testExistsBySsn() {
        when(repo.existsBySsn(12345)).thenReturn(true);

        assertTrue(service.existsBySsn(12345));
    }

    // ================= ORDER =================

    @Test
    void testGetAllOrderedByName() {
        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(physician));

        assertEquals(1, service.getAllOrderedByName().size());
    }

    // ================= DEPARTMENT HEADS =================

    @Test
    void testGetDepartmentHeads() {

        Department dept = new Department();
        dept.setHead(physician);

        when(departmentRepo.findAll()).thenReturn(List.of(dept));

        List<Physician> heads = service.getDepartmentHeads();

        assertEquals(1, heads.size());
        assertEquals("Dr Smith", heads.get(0).getName());
    }
}