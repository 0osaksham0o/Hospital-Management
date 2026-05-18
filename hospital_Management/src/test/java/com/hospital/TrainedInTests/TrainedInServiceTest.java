package com.hospital.TrainedInTests;

import com.hospital.entity.TrainedIn;
import com.hospital.entity.TrainedInId;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.TrainedInRepository;
import com.hospital.service.impl.TrainedInServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainedInServiceTest {

    @Mock
    private TrainedInRepository trainedInRepository;

    @InjectMocks
    private TrainedInServiceImpl trainedInService;

    private TrainedIn trainedIn;
    private TrainedInId id;

    @BeforeEach
    void setup() {
        id = new TrainedInId(1, 100);

        trainedIn = new TrainedIn();
        trainedIn.setId(id);
        trainedIn.setCertificationDate(LocalDateTime.now().minusDays(10));
        trainedIn.setCertificationExpires(LocalDateTime.now().plusDays(30));
    }

    // ================= BASIC CRUD =================

    @Test
    void testGetAll() {
        when(trainedInRepository.findAll())
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result = trainedInService.getAll();

        assertEquals(1, result.size());
        verify(trainedInRepository).findAll();
    }

    @Test
    void testGetAllWithPagination() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<TrainedIn> page = new PageImpl<>(List.of(trainedIn));

        when(trainedInRepository.findAll(pageable)).thenReturn(page);

        Page<TrainedIn> result = trainedInService.getAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testGetById() {
        when(trainedInRepository.findById(id))
                .thenReturn(Optional.of(trainedIn));

        TrainedIn result = trainedInService.getById(id);

        assertNotNull(result);
    }

    @Test
    void testGetById_NotFound() {
        when(trainedInRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> trainedInService.getById(id));
    }

    @Test
    void testExistsById() {
        when(trainedInRepository.existsById(id)).thenReturn(true);

        assertTrue(trainedInService.existsById(id));
    }

    @Test
    void testSave() {
        when(trainedInRepository.save(trainedIn))
                .thenReturn(trainedIn);

        TrainedIn saved = trainedInService.save(trainedIn);

        assertNotNull(saved);
        verify(trainedInRepository).save(trainedIn);
    }

    @Test
    void testDelete() {

        // IMPORTANT FIX
        when(trainedInRepository.findById(id))
                .thenReturn(Optional.of(trainedIn));

        doNothing().when(trainedInRepository).deleteById(id);

        trainedInService.delete(id);

        verify(trainedInRepository).deleteById(id);
    }

    // ================= PHYSICIAN =================

    @Test
    void testGetByPhysician() {
        when(trainedInRepository.findById_PhysicianId(1))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result = trainedInService.getByPhysician(1);

        assertEquals(1, result.size());
    }

    @Test
    void testCountByPhysician() {
        when(trainedInRepository.countById_PhysicianId(1))
                .thenReturn(2L);

        assertEquals(2, trainedInService.countByPhysician(1));
    }

    // ================= PROCEDURE =================

    @Test
    void testGetByProcedure() {
        when(trainedInRepository.findById_TreatmentCode(100))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result =
                trainedInService.getByProcedure(100);

        assertEquals(1, result.size());
    }

    @Test
    void testCountByProcedure() {
        when(trainedInRepository.countById_TreatmentCode(100))
                .thenReturn(5L);

        assertEquals(5,
                trainedInService.countByProcedure(100));
    }

    // ================= DATE FILTER =================

    @Test
    void testGetByCertificationDateBetween() {

        LocalDateTime from = LocalDateTime.now().minusDays(20);
        LocalDateTime to = LocalDateTime.now();

        when(trainedInRepository
                .findByCertificationDateBetween(from, to))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result =
                trainedInService.getByCertificationDateBetween(from, to);

        assertEquals(1, result.size());
    }

    @Test
    void testGetExpiredBefore() {

        LocalDateTime now = LocalDateTime.now();

        when(trainedInRepository
                .findByCertificationExpiresBefore(now))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result =
                trainedInService.getExpiredBefore(now);

        assertEquals(1, result.size());
    }

    @Test
    void testGetValidCertifications() {

        LocalDateTime now = LocalDateTime.now();

        when(trainedInRepository
                .findByCertificationExpiresAfterOrderByCertificationExpiresAsc(now))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result =
                trainedInService.getValidCertifications(now);

        assertEquals(1, result.size());
    }

    @Test
    void testGetExpiredByPhysician() {

        LocalDateTime now = LocalDateTime.now();

        when(trainedInRepository
                .findById_PhysicianIdAndCertificationExpiresBefore(1, now))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result =
                trainedInService.getExpiredByPhysician(1, now);

        assertEquals(1, result.size());
    }

    @Test
    void testGetExpiringSoon() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(10);

        when(trainedInRepository
                .findByCertificationExpiresBetweenOrderByCertificationExpiresAsc(now, deadline))
                .thenReturn(List.of(trainedIn));

        List<TrainedIn> result =
                trainedInService.getExpiringSoon(now, deadline);

        assertEquals(1, result.size());
    }

    // ================= CERTIFICATION CHECK =================

    @Test
    void testIsCertified() {

        when(trainedInRepository
                .existsById_PhysicianIdAndId_TreatmentCode(1, 100))
                .thenReturn(true);

        assertTrue(trainedInService.isCertified(1, 100));
    }
}