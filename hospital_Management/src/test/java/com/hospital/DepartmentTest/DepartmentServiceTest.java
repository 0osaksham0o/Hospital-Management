package com.hospital.DepartmentTest;

import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.DepartmentRepository;
import com.hospital.service.impl.DepartmentServiceImpl;

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
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repo;

    @InjectMocks
    private DepartmentServiceImpl service;

    private Department department;
    private Physician physician;

    @BeforeEach
    void setup() {
        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr Smith");

        department = new Department(101, "Cardiology", physician);
    }

    // ✅ getAll
    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(department));

        List<Department> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    // ✅ getAll pageable
    @Test
    void testGetAllPageable() {
        Page<Department> page = new PageImpl<>(List.of(department));

        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Department> result =
                service.getAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    // ✅ getById success
    @Test
    void testGetByIdSuccess() {
        when(repo.findById(101)).thenReturn(Optional.of(department));

        Department result = service.getById(101);

        assertNotNull(result);
        assertEquals("Cardiology", result.getName());
    }

    // ✅ getById not found
    @Test
    void testGetByIdNotFound() {
        when(repo.findById(101)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(101));
    }

    // ✅ existsById
    @Test
    void testExistsById() {
        when(repo.existsById(101)).thenReturn(true);

        assertTrue(service.existsById(101));
    }

    // ✅ save
    @Test
    void testSave() {
        when(repo.save(department)).thenReturn(department);

        Department saved = service.save(department);

        assertEquals(department, saved);
        verify(repo).save(department);
    }

    // ✅ delete
    @Test
    void testDelete() {
        when(repo.findById(101)).thenReturn(Optional.of(department));

        service.delete(101);

        verify(repo).deleteById(101);
    }

    // ✅ getByName
    @Test
    void testGetByName() {
        when(repo.findByName("Cardiology"))
                .thenReturn(Optional.of(department));

        Optional<Department> result =
                service.getByName("Cardiology");

        assertTrue(result.isPresent());
    }

    // ✅ searchByName
    @Test
    void testSearchByName() {
        when(repo.findByNameContainingIgnoreCase("card"))
                .thenReturn(List.of(department));

        List<Department> result =
                service.searchByName("card");

        assertEquals(1, result.size());
    }

    // ✅ existsByName
    @Test
    void testExistsByName() {
        when(repo.existsByName("Cardiology"))
                .thenReturn(true);

        assertTrue(service.existsByName("Cardiology"));
    }

    // ✅ getByHead
    @Test
    void testGetByHead() {
        when(repo.findByHead_EmployeeId(1))
                .thenReturn(Optional.of(department));

        Optional<Department> result =
                service.getByHead(1);

        assertTrue(result.isPresent());
    }

    // ✅ existsByHead
    @Test
    void testExistsByHead() {
        when(repo.existsByHead_EmployeeId(1))
                .thenReturn(true);

        assertTrue(service.existsByHead(1));
    }

    // ✅ ordered by name
    @Test
    void testGetAllOrderedByName() {
        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(department));

        List<Department> result =
                service.getAllOrderedByName();

        assertEquals(1, result.size());
    }

    // ✅ getByHeadName
    @Test
    void testGetByHeadName() {
        when(repo.findByHead_Name("Dr Smith"))
                .thenReturn(List.of(department));

        List<Department> result =
                service.getByHeadName("Dr Smith");

        assertEquals(1, result.size());
    }
}