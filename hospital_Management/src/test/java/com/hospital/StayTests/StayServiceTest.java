package com.hospital.StayTests;

import com.hospital.entity.Patient;
import com.hospital.entity.Room;
import com.hospital.entity.Stay;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.StayRepository;
import com.hospital.service.impl.StayServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StayServiceTest {

    @Mock
    private StayRepository repo;

    @InjectMocks
    private StayServiceImpl service;

    // ---------- helper ----------
    private Stay createStay() {

        Patient patient = new Patient();
        patient.setSsn(1);

        Room room = new Room();
        room.setRoomNumber(101);

        return new Stay(
                1,
                patient,
                room,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
    }

    // ---------- BASIC CRUD ----------

    @Test
    void getAll_shouldReturnList() {
        when(repo.findAll()).thenReturn(List.of(createStay()));

        List<Stay> result = service.getAll();

        assertThat(result).hasSize(1);
        verify(repo).findAll();
    }

    @Test
    void getAllPageable_shouldReturnPage() {
        Pageable pageable = mock(Pageable.class);
        Page<Stay> page = mock(Page.class);

        when(repo.findAll(pageable)).thenReturn(page);

        Page<Stay> result = service.getAll(pageable);

        assertThat(result).isEqualTo(page);
    }

    @Test
    void getById_shouldReturnStay() {
        Stay stay = createStay();

        when(repo.findById(1)).thenReturn(Optional.of(stay));

        Stay result = service.getById(1);

        assertThat(result.getStayId()).isEqualTo(1);
    }

    @Test
    void getById_shouldThrowException() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void save_shouldSaveStay() {
        Stay stay = createStay();

        when(repo.save(stay)).thenReturn(stay);

        Stay result = service.save(stay);

        assertThat(result).isNotNull();
        verify(repo).save(stay);
    }

    @Test
    void delete_shouldDeleteStay() {
        when(repo.findById(1)).thenReturn(Optional.of(createStay()));

        service.delete(1);

        verify(repo).deleteById(1);
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(repo.existsById(1)).thenReturn(true);

        assertThat(service.existsById(1)).isTrue();
    }

    // ---------- PATIENT ----------

    @Test
    void getByPatient_shouldWork() {
        when(repo.findByPatient_Ssn(1))
                .thenReturn(List.of(createStay()));

        assertThat(service.getByPatient(1)).hasSize(1);
    }

    @Test
    void countByPatient_shouldWork() {
        when(repo.countByPatient_Ssn(1)).thenReturn(2L);

        assertThat(service.countByPatient(1)).isEqualTo(2);
    }

    // ---------- ROOM ----------

    @Test
    void getByRoom_shouldWork() {
        when(repo.findByRoom_RoomNumber(101))
                .thenReturn(List.of(createStay()));

        assertThat(service.getByRoom(101)).hasSize(1);
    }

    @Test
    void countByRoom_shouldWork() {
        when(repo.countByRoom_RoomNumber(101)).thenReturn(3L);

        assertThat(service.countByRoom(101)).isEqualTo(3);
    }

    // ---------- DATE FILTERS ----------

    @Test
    void getByStartBetween_shouldWork() {

        LocalDateTime from = LocalDateTime.now().minusDays(2);
        LocalDateTime to   = LocalDateTime.now();

        when(repo.findByStayStartBetween(from, to))
                .thenReturn(List.of(createStay()));

        assertThat(service.getByStartBetween(from, to)).hasSize(1);
    }

    @Test
    void getByEndBetween_shouldWork() {

        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to   = LocalDateTime.now().plusDays(2);

        when(repo.findByStayEndBetween(from, to))
                .thenReturn(List.of(createStay()));

        assertThat(service.getByEndBetween(from, to)).hasSize(1);
    }

    @Test
    void getCurrentStays_shouldWork() {

        LocalDateTime now = LocalDateTime.now();

        when(repo.findByStayEndAfterOrderByStayStartAsc(now))
                .thenReturn(List.of(createStay()));

        assertThat(service.getCurrentStays(now)).hasSize(1);
    }

    // ---------- COMBINED FILTER ----------

    @Test
    void getByPatientAndRoom_shouldWork() {
        when(repo.findByPatient_SsnAndRoom_RoomNumber(1,101))
                .thenReturn(List.of(createStay()));

        assertThat(service.getByPatientAndRoom(1,101)).hasSize(1);
    }

    // ---------- ORDERING ----------

    @Test
    void getAllOrderedByStartDesc_shouldWork() {
        when(repo.findAllByOrderByStayStartDesc())
                .thenReturn(List.of(createStay()));

        assertThat(service.getAllOrderedByStartDesc()).hasSize(1);
    }
}