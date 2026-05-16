package com.hospital.StayTests;



import com.hospital.entity.Patient;
import com.hospital.entity.Room;
import com.hospital.entity.Stay;
import com.hospital.projection.StayProjection;

import com.hospital.repository.PatientRepository;
import com.hospital.repository.RoomRepository;
import com.hospital.repository.StayRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StayRepositoryTest {

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Should save and fetch stay by id")
    void testSaveAndFindById() {

        Patient patient = new Patient();
        patient.setSsn(101);
        patient.setName("Tony Stark");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(201);
        room.setRoomType("ICU");
        room.setBlockCode(1);
        room.setUnavailable(false);
        roomRepository.save(room);

        Stay stay = new Stay(
                1,
                patient,
                room,
                LocalDateTime.of(2026, 5, 16, 10, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0)
        );

        stayRepository.save(stay);

        Optional<Stay> saved =
                stayRepository.findById(1);

        assertThat(saved).isPresent();
        assertThat(saved.get().getStayId())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("Should find stay by patient ssn")
    void testFindByPatientSsn() {

        Patient patient = new Patient();
        patient.setSsn(102);
        patient.setName("Bruce Wayne");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(202);
        room.setRoomType("General");
        room.setBlockCode(2);
        room.setUnavailable(false);
        roomRepository.save(room);

        Stay stay = new Stay(
                2,
                patient,
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
        );

        stayRepository.save(stay);

        List<Stay> stays =
                stayRepository.findByPatient_Ssn(102);

        assertThat(stays).hasSize(1);
    }

    @Test
    @DisplayName("Should count stays by patient ssn")
    void testCountByPatientSsn() {

        Patient patient = new Patient();
        patient.setSsn(103);
        patient.setName("Peter Parker");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(203);
        room.setRoomType("Private");
        room.setBlockCode(3);
        room.setUnavailable(false);
        roomRepository.save(room);

        stayRepository.save(
                new Stay(
                        3,
                        patient,
                        room,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(1)
                )
        );

        stayRepository.save(
                new Stay(
                        4,
                        patient,
                        room,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(3)
                )
        );

        long count =
                stayRepository.countByPatient_Ssn(103);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find stay by room number")
    void testFindByRoomNumber() {

        Patient patient = new Patient();
        patient.setSsn(104);
        patient.setName("Clark Kent");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(204);
        room.setRoomType("Emergency");
        room.setBlockCode(4);
        room.setUnavailable(false);
        roomRepository.save(room);

        Stay stay = new Stay(
                5,
                patient,
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5)
        );

        stayRepository.save(stay);

        List<Stay> stays =
                stayRepository.findByRoom_RoomNumber(204);

        assertThat(stays).hasSize(1);
    }

    @Test
    @DisplayName("Should count stays by room number")
    void testCountByRoomNumber() {

        Patient patient = new Patient();
        patient.setSsn(105);
        patient.setName("Steve Rogers");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(205);
        room.setRoomType("ICU");
        room.setBlockCode(5);
        room.setUnavailable(false);
        roomRepository.save(room);

        stayRepository.save(
                new Stay(
                        6,
                        patient,
                        room,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusDays(2)
                )
        );

        long count =
                stayRepository.countByRoom_RoomNumber(205);

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find stays between stay start dates")
    void testFindByStayStartBetween() {

        Patient patient = new Patient();
        patient.setSsn(106);
        patient.setName("Natasha");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(206);
        room.setRoomType("General");
        room.setBlockCode(6);
        room.setUnavailable(false);
        roomRepository.save(room);

        Stay stay = new Stay(
                7,
                patient,
                room,
                LocalDateTime.of(2026, 5, 10, 10, 0),
                LocalDateTime.of(2026, 5, 15, 10, 0)
        );

        stayRepository.save(stay);

        List<Stay> stays =
                stayRepository.findByStayStartBetween(
                        LocalDateTime.of(2026, 5, 1, 0, 0),
                        LocalDateTime.of(2026, 5, 20, 0, 0)
                );

        assertThat(stays).isNotEmpty();
    }

    @Test
    @DisplayName("Should find active stays ordered by stay start asc")
    void testFindByStayEndAfterOrderByStayStartAsc() {

        Patient patient = new Patient();
        patient.setSsn(107);
        patient.setName("Wanda");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(207);
        room.setRoomType("VIP");
        room.setBlockCode(7);
        room.setUnavailable(false);
        roomRepository.save(room);

        Stay stay = new Stay(
                8,
                patient,
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
        );

        stayRepository.save(stay);

        List<Stay> stays =
                stayRepository.findByStayEndAfterOrderByStayStartAsc(
                        LocalDateTime.now().minusHours(1)
                );

        assertThat(stays).isNotEmpty();
    }

    @Test
    @DisplayName("Should return all stays ordered by stay start desc")
    void testFindAllByOrderByStayStartDesc() {

        List<Stay> stays =
                stayRepository.findAllByOrderByStayStartDesc();

        assertThat(stays).isNotNull();
    }

    @Test
    @DisplayName("Should return projected stays")
    void testFindAllProjectedBy() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<StayProjection> page =
                stayRepository.findAllProjectedBy(pageable);

        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Should return projected stay by id")
    void testFindProjectedByStayId() {

        Patient patient = new Patient();
        patient.setSsn(108);
        patient.setName("Thor");
        patientRepository.save(patient);

        Room room = new Room();
        room.setRoomNumber(208);
        room.setRoomType("ICU");
        room.setBlockCode(8);
        room.setUnavailable(false);
        roomRepository.save(room);

        Stay stay = new Stay(
                9,
                patient,
                room,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        stayRepository.save(stay);

        Optional<StayProjection> projection =
                stayRepository.findProjectedByStayId(9);

        assertThat(projection).isPresent();
    }
}