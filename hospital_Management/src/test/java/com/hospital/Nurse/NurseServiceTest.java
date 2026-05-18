package com.hospital.Nurse;

import com.hospital.entity.Nurse;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.NurseRepository;
import com.hospital.service.impl.NurseServiceImpl;

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
public class NurseServiceTest {

    @Mock
    private NurseRepository repo;

    @InjectMocks
    private NurseServiceImpl service;

    private Nurse nurse;

    @BeforeEach
    void setup() {
        nurse = new Nurse(
                1,
                "Anita",
                "Senior Nurse",
                true,
                12345
        );
    }

    // ================= GET ALL =================

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(nurse));

        List<Nurse> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testGetAllPaginated() {
        Page<Nurse> page = new PageImpl<>(List.of(nurse));

        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Nurse> result = service.getAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    // ================= GET BY ID =================

    @Test
    void testGetByIdSuccess() {
        when(repo.findById(1)).thenReturn(Optional.of(nurse));

        Nurse result = service.getById(1);

        assertEquals("Anita", result.getName());
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(1));
    }

    // ================= EXISTS =================

    @Test
    void testExistsById() {
        when(repo.existsById(1)).thenReturn(true);

        assertTrue(service.existsById(1));
    }

    // ================= SAVE =================

    @Test
    void testSave() {
        when(repo.save(nurse)).thenReturn(nurse);

        Nurse saved = service.save(nurse);

        assertEquals("Anita", saved.getName());
        verify(repo).save(nurse);
    }

    // ================= DELETE =================

    @Test
    void testDelete() {
        when(repo.findById(1)).thenReturn(Optional.of(nurse));

        service.delete(1);

        verify(repo).deleteById(1);
    }

    // ================= NAME =================

    @Test
    void testGetByName() {
        when(repo.findByName("Anita")).thenReturn(Optional.of(nurse));

        Optional<Nurse> result = service.getByName("Anita");

        assertTrue(result.isPresent());
    }

    @Test
    void testSearchByName() {
        when(repo.findByNameContainingIgnoreCase("ani"))
                .thenReturn(List.of(nurse));

        List<Nurse> result = service.searchByName("ani");

        assertEquals(1, result.size());
    }

    // ================= POSITION =================

    @Test
    void testGetByPosition() {
        when(repo.findByPosition("Senior Nurse"))
                .thenReturn(List.of(nurse));

        List<Nurse> result = service.getByPosition("Senior Nurse");

        assertEquals(1, result.size());
    }

    // ================= REGISTERED =================

    @Test
    void testGetByRegistered() {
        when(repo.findByRegistered(true))
                .thenReturn(List.of(nurse));

        List<Nurse> result = service.getByRegistered(true);

        assertTrue(result.get(0).getRegistered());
    }

    @Test
    void testCountByRegistered() {
        when(repo.countByRegistered(true)).thenReturn(5L);

        long count = service.countByRegistered(true);

        assertEquals(5, count);
    }

    // ================= SSN =================

    @Test
    void testGetBySsn() {
        when(repo.findBySsn(12345)).thenReturn(Optional.of(nurse));

        Optional<Nurse> result = service.getBySsn(12345);

        assertTrue(result.isPresent());
    }

    @Test
    void testExistsBySsn() {
        when(repo.existsBySsn(12345)).thenReturn(true);

        assertTrue(service.existsBySsn(12345));
    }

    // ================= ORDERED =================

    @Test
    void testGetAllRegisteredOrderedByName() {
        when(repo.findByRegisteredTrueOrderByNameAsc())
                .thenReturn(List.of(nurse));

        List<Nurse> result =
                service.getAllRegisteredOrderedByName();

        assertEquals(1, result.size());
    }
}