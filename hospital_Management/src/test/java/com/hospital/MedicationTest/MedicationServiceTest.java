package com.hospital.MedicationTest;

import com.hospital.entity.Medication;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.MedicationRepository;
import com.hospital.service.impl.MedicationServiceImpl;

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
public class MedicationServiceTest {

    @Mock
    private MedicationRepository repo;

    @InjectMocks
    private MedicationServiceImpl service;

    private Medication medication;

    @BeforeEach
    void setUp() {
        medication = new Medication(
                1,
                "Paracetamol",
                "Cipla",
                "Pain Relief"
        );
    }

    // ✅ getAll
    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(medication));

        List<Medication> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    // ✅ getAll pageable
    @Test
    void testGetAllPageable() {
        Page<Medication> page = new PageImpl<>(List.of(medication));
        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Medication> result =
                service.getAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    // ✅ getById success
    @Test
    void testGetByIdSuccess() {
        when(repo.findById(1)).thenReturn(Optional.of(medication));

        Medication result = service.getById(1);

        assertNotNull(result);
        assertEquals("Paracetamol", result.getName());
    }

    // ✅ getById exception
    @Test
    void testGetByIdNotFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1));
    }

    // ✅ existsById
    @Test
    void testExistsById() {
        when(repo.existsById(1)).thenReturn(true);

        assertTrue(service.existsById(1));
    }

    // ✅ save
    @Test
    void testSave() {
        when(repo.save(medication)).thenReturn(medication);

        Medication saved = service.save(medication);

        assertEquals(medication, saved);
        verify(repo).save(medication);
    }

    // ✅ delete
    @Test
    void testDelete() {
        when(repo.findById(1)).thenReturn(Optional.of(medication));

        service.delete(1);

        verify(repo).deleteById(1);
    }

    // ✅ getByName
    @Test
    void testGetByName() {
        when(repo.findByName("Paracetamol"))
                .thenReturn(Optional.of(medication));

        Optional<Medication> result =
                service.getByName("Paracetamol");

        assertTrue(result.isPresent());
    }

    // ✅ searchByName
    @Test
    void testSearchByName() {
        when(repo.findByNameContainingIgnoreCase("para"))
                .thenReturn(List.of(medication));

        List<Medication> result =
                service.searchByName("para");

        assertEquals(1, result.size());
    }

    // ✅ existsByName
    @Test
    void testExistsByName() {
        when(repo.existsByName("Paracetamol")).thenReturn(true);

        assertTrue(service.existsByName("Paracetamol"));
    }

    // ✅ getByBrand
    @Test
    void testGetByBrand() {
        when(repo.findByBrand("Cipla"))
                .thenReturn(List.of(medication));

        List<Medication> result =
                service.getByBrand("Cipla");

        assertEquals(1, result.size());
    }

    // ✅ searchByBrand
    @Test
    void testSearchByBrand() {
        when(repo.findByBrandContainingIgnoreCase("cip"))
                .thenReturn(List.of(medication));

        List<Medication> result =
                service.searchByBrand("cip");

        assertEquals(1, result.size());
    }

    // ✅ distinct brands
    @Test
    void testGetAllDistinctBrands() {
        Medication m2 = new Medication(2,"Ibuprofen","Sun","Pain");
        Medication m3 = new Medication(3,"Crocin","Cipla","Fever");

        when(repo.findAll()).thenReturn(List.of(medication, m2, m3));

        List<String> brands = service.getAllDistinctBrands();

        assertEquals(2, brands.size());
        assertTrue(brands.contains("Cipla"));
        assertTrue(brands.contains("Sun"));
    }

    // ✅ searchByDescription
    @Test
    void testSearchByDescription() {
        when(repo.findByDescriptionContainingIgnoreCase("pain"))
                .thenReturn(List.of(medication));

        List<Medication> result =
                service.searchByDescription("pain");

        assertEquals(1, result.size());
    }

    // ✅ ordered by name
    @Test
    void testGetAllOrderedByName() {
        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(medication));

        List<Medication> result =
                service.getAllOrderedByName();

        assertEquals(1, result.size());
    }
}