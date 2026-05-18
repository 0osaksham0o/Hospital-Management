package com.hospital.OnCall;

import com.hospital.entity.OnCall;
import com.hospital.entity.OnCallId;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.OnCallRepository;
import com.hospital.service.impl.OnCallServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OnCallServiceTest {

    @Mock
    private OnCallRepository repo;

    @InjectMocks
    private OnCallServiceImpl service;

    private OnCall onCall;
    private OnCallId id;

    @BeforeEach
    void setup() {

        id = new OnCallId();
        id.setNurseId(1);
        id.setBlockFloor(2);
        id.setBlockCode(101);

        onCall = new OnCall();
        onCall.setId(id);
        onCall.setOnCallStart(LocalDateTime.now().minusHours(2));
        onCall.setOnCallEnd(LocalDateTime.now().plusHours(2));
    }

    // ================= GET ALL =================

    @Test
    void testGetAll() {
        when(repo.findAll()).thenReturn(List.of(onCall));

        List<OnCall> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void testGetAllPaginated() {
        Page<OnCall> page = new PageImpl<>(List.of(onCall));

        when(repo.findAll(any(PageRequest.class))).thenReturn(page);

        Page<OnCall> result = service.getAll(PageRequest.of(0, 5));

        assertEquals(1, result.getTotalElements());
    }

    // ================= GET BY ID =================

    @Test
    void testGetByIdSuccess() {
        when(repo.findById(id)).thenReturn(Optional.of(onCall));

        OnCall result = service.getById(id);

        assertNotNull(result);
    }

    @Test
    void testGetByIdNotFound() {
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(id));
    }

    // ================= EXISTS =================

    @Test
    void testExistsById() {
        when(repo.existsById(id)).thenReturn(true);

        assertTrue(service.existsById(id));
    }

    // ================= SAVE =================

    @Test
    void testSave() {
        when(repo.save(onCall)).thenReturn(onCall);

        OnCall saved = service.save(onCall);

        assertNotNull(saved);
        verify(repo).save(onCall);
    }

    // ================= DELETE =================

    @Test
    void testDelete() {
        when(repo.findById(id)).thenReturn(Optional.of(onCall));

        service.delete(id);

        verify(repo).deleteById(id);
    }

    // ================= NURSE =================

    @Test
    void testGetByNurse() {
        when(repo.findById_NurseId(1)).thenReturn(List.of(onCall));

        List<OnCall> result = service.getByNurse(1);

        assertEquals(1, result.size());
    }

    @Test
    void testCountByNurse() {
        when(repo.countById_NurseId(1)).thenReturn(3L);

        long count = service.countByNurse(1);

        assertEquals(3, count);
    }

    // ================= BLOCK =================

    @Test
    void testGetByBlock() {
        when(repo.findById_BlockFloorAndId_BlockCode(2,101))
                .thenReturn(List.of(onCall));

        List<OnCall> result = service.getByBlock(2,101);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByFloor() {
        when(repo.findById_BlockFloor(2))
                .thenReturn(List.of(onCall));

        assertEquals(1, service.getByFloor(2).size());
    }

    @Test
    void testGetByBlockCode() {
        when(repo.findById_BlockCode(101))
                .thenReturn(List.of(onCall));

        assertEquals(1, service.getByBlockCode(101).size());
    }

    // ================= TIME =================

    @Test
    void testGetByStartBetween() {

        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);

        when(repo.findByOnCallStartBetween(from, to))
                .thenReturn(List.of(onCall));

        assertEquals(1, service.getByStartBetween(from, to).size());
    }

    @Test
    void testGetCurrentlyOnCall() {

        LocalDateTime now = LocalDateTime.now();

        when(repo.findByOnCallStartLessThanEqualAndOnCallEndGreaterThanEqual(now, now))
                .thenReturn(List.of(onCall));

        assertEquals(1, service.getCurrentlyOnCall(now).size());
    }

    @Test
    void testGetNurseShiftsInWindow() {

        LocalDateTime from = LocalDateTime.now().minusDays(1);
        LocalDateTime to = LocalDateTime.now().plusDays(1);

        when(repo.findById_NurseIdAndOnCallStartBeforeAndOnCallEndAfter(1, to, from))
                .thenReturn(List.of(onCall));

        assertEquals(1,
                service.getNurseShiftsInWindow(1, from, to).size());
    }

    // ================= NURSE + BLOCK =================

    @Test
    void testGetByNurseAndBlock() {
        when(repo.findById_NurseIdAndId_BlockFloorAndId_BlockCode(1,2,101))
                .thenReturn(List.of(onCall));

        assertEquals(1,
                service.getByNurseAndBlock(1,2,101).size());
    }

    // ================= ORDER =================

    @Test
    void testGetAllOrderedByStart() {
        when(repo.findAllByOrderByOnCallStartAsc())
                .thenReturn(List.of(onCall));

        assertEquals(1, service.getAllOrderedByStart().size());
    }
}