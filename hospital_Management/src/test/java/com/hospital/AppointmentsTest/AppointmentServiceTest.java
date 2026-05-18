package com.hospital.AppointmentsTest;
  



import com.hospital.entity.*;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.service.impl.AppointmentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
  




;



    @Mock
    private AppointmentRepository repo;

    @InjectMocks
    private AppointmentServiceImpl service;

    private Appointment appointment;

    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setup() {

        Patient patient = new Patient();
        patient.setSsn(1);

        Physician physician = new Physician();
        physician.setEmployeeId(10);

        Nurse nurse = new Nurse();
        nurse.setEmployeeId(20);

        start = LocalDateTime.now();
        end = start.plusHours(1);

        appointment = new Appointment(
                100,
                patient,
                nurse,
                physician,
                start,
                end,
                "Room-1"
        );
    }

    // ================= BASIC =================

    @Test
    void getAll_shouldReturnAppointments() {
        when(repo.findAll()).thenReturn(List.of(appointment));

        List<Appointment> result = service.getAll();

        assertEquals(1, result.size());
        verify(repo).findAll();
    }

    @Test
    void getAllPageable_shouldReturnPage() {
        Page<Appointment> page = new PageImpl<>(List.of(appointment));

        when(repo.findAll(any(Pageable.class))).thenReturn(page);

        Page<Appointment> result =
                service.getAll(PageRequest.of(0,5));

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getById_shouldReturnAppointment() {
        when(repo.findById(100)).thenReturn(Optional.of(appointment));

        Appointment result = service.getById(100);

        assertNotNull(result);
    }

    @Test
    void getById_shouldThrowException() {
        when(repo.findById(100)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getById(100));
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(repo.existsById(100)).thenReturn(true);

        assertTrue(service.existsById(100));
    }

    @Test
    void save_shouldPersistAppointment() {
        when(repo.save(appointment)).thenReturn(appointment);

        Appointment saved = service.save(appointment);

        assertEquals(100, saved.getAppointmentId());
    }

    @Test
    void delete_shouldRemoveAppointment() {
        when(repo.findById(100)).thenReturn(Optional.of(appointment));

        service.delete(100);

        verify(repo).deleteById(100);
    }

    // ================= PATIENT =================

    @Test
    void getByPatient_shouldWork() {
        when(repo.findByPatient_Ssn(1))
                .thenReturn(List.of(appointment));

        assertEquals(1, service.getByPatient(1).size());
    }

    @Test
    void countByPatient_shouldWork() {
        when(repo.countByPatient_Ssn(1)).thenReturn(3L);

        assertEquals(3, service.countByPatient(1));
    }

    // ================= PHYSICIAN =================

    @Test
    void getByPhysician_shouldWork() {
        when(repo.findByPhysician_EmployeeId(10))
                .thenReturn(List.of(appointment));

        assertEquals(1, service.getByPhysician(10).size());
    }

    @Test
    void countByPhysician_shouldWork() {
        when(repo.countByPhysician_EmployeeId(10)).thenReturn(2L);

        assertEquals(2, service.countByPhysician(10));
    }

    // ================= NURSE =================

    @Test
    void getByPrepNurse_shouldWork() {
        when(repo.findByPrepNurse_EmployeeId(20))
                .thenReturn(List.of(appointment));

        assertEquals(1, service.getByPrepNurse(20).size());
    }

    @Test
    void getWithNoPrepNurse_shouldWork() {
        when(repo.findByPrepNurseIsNull())
                .thenReturn(List.of(appointment));

        assertEquals(1, service.getWithNoPrepNurse().size());
    }

    // ================= ROOM =================

    @Test
    void getByExaminationRoom_shouldWork() {
        when(repo.findByExaminationRoom("Room-1"))
                .thenReturn(List.of(appointment));

        assertEquals(1,
                service.getByExaminationRoom("Room-1").size());
    }

    // ================= TIME =================

    @Test
    void getByStartBetween_shouldWork() {
        when(repo.findByStartBetween(start,end))
                .thenReturn(List.of(appointment));

        assertEquals(1,
                service.getByStartBetween(start,end).size());
    }

    @Test
    void getUpcomingForPatient_shouldWork() {
        when(repo.findByPatient_SsnAndStartAfterOrderByStartAsc(1,start))
                .thenReturn(List.of(appointment));

        assertEquals(1,
                service.getUpcomingForPatient(1,start).size());
    }

    @Test
    void getAllOrderedByStart_shouldWork() {
        when(repo.findAllByOrderByStartAsc())
                .thenReturn(List.of(appointment));

        assertEquals(1,
                service.getAllOrderedByStart().size());
    }

    // ================= COMBINED =================

    @Test
    void getByPatientAndPhysician_shouldWork() {
        when(repo.findByPatient_SsnAndPhysician_EmployeeId(1,10))
                .thenReturn(List.of(appointment));

        assertEquals(1,
                service.getByPatientAndPhysician(1,10).size());
    }

    @Test
    void getByPhysicianInPeriod_shouldWork() {
        when(repo.findByPhysician_EmployeeIdAndStartBetween(10,start,end))
                .thenReturn(List.of(appointment));

        assertEquals(1,
                service.getByPhysicianInPeriod(10,start,end).size());
    }
  }