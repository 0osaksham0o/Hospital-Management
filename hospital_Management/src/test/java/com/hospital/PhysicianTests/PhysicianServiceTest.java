package com.hospital.PhysicianTests;

import com.hospital.entity.Physician;
import com.hospital.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PhysicianServiceTest {

    @Mock
    private PhysicianRepository physicianRepository;

    @InjectMocks
    private PhysicianServiceImpl physicianService;

    private Physician physician;

    @BeforeEach
    void setUp() {
        physician = new Physician();
        physician.setEmployeeId(1);
        physician.setName("Dr. Strange");
        physician.setPosition("Cardiologist");
        physician.setSsn(123456789);
    }

    @Test
    void testGetAll() {

        List<Physician> physicians = Arrays.asList(physician);

        when(physicianRepository.findAll()).thenReturn(physicians);

        List<Physician> result = physicianService.getAll();

        assertEquals(1, result.size());
        assertEquals("Dr. Strange", result.get(0).getName());

        verify(physicianRepository, times(1)).findAll();
    }

    @Test
    void testGetAllWithPageable() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Physician> physicianPage =
                new PageImpl<>(Arrays.asList(physician));

        when(physicianRepository.findAll(pageable))
                .thenReturn(physicianPage);

        Page<Physician> result =
                physicianService.getAll(pageable);

        assertEquals(1, result.getTotalElements());

        verify(physicianRepository, times(1))
                .findAll(pageable);
    }

    @Test
    void testGetByIdSuccess() {

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        Physician result = physicianService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getEmployeeId());
        assertEquals("Dr. Strange", result.getName());

        verify(physicianRepository, times(1))
                .findById(1);
    }

    @Test
    void testGetByIdNotFound() {

        when(physicianRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> physicianService.getById(1));

        verify(physicianRepository, times(1))
                .findById(1);
    }

    @Test
    void testExistsById() {

        when(physicianRepository.existsById(1))
                .thenReturn(true);

        boolean exists = physicianService.existsById(1);

        assertTrue(exists);

        verify(physicianRepository, times(1))
                .existsById(1);
    }

    @Test
    void testSave() {

        when(physicianRepository.save(physician))
                .thenReturn(physician);

        Physician saved = physicianService.save(physician);

        assertNotNull(saved);
        assertEquals("Dr. Strange", saved.getName());
        assertEquals("Cardiologist", saved.getPosition());

        verify(physicianRepository, times(1))
                .save(physician);
    }

    @Test
    void testDeleteSuccess() {

        when(physicianRepository.findById(1))
                .thenReturn(Optional.of(physician));

        doNothing().when(physicianRepository)
                .deleteById(1);

        physicianService.delete(1);

        verify(physicianRepository, times(1))
                .findById(1);

        verify(physicianRepository, times(1))
                .deleteById(1);
    }

    @Test
    void testDeleteNotFound() {

        when(physicianRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> physicianService.delete(1));

        verify(physicianRepository, times(1))
                .findById(1);

        verify(physicianRepository, never())
                .deleteById(anyInt());
    }
}