package com.hospital.StayTests;

import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.entity.Room;
import com.hospital.entity.Stay;
import com.hospital.projection.StayProjection;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
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
    private PhysicianRepository physicianRepository;

    @Autowired
    private RoomRepository roomRepository;

    // ── Helpers ────────────────────────────────────────────────────────────────

    private Physician savePhysician(int id) {
        Physician p = new Physician();
        p.setEmployeeId(id);
        p.setName("Dr. Test-" + id);
        p.setPosition("General");
        p.setSsn(id * 1000);
        return physicianRepository.save(p);
    }

    private Patient savePatient(int ssn, String name, String address,
                                String phone, int insuranceId, Physician pcp) {
        Patient p = new Patient();
        p.setSsn(ssn);
        p.setName(name);
        p.setAddress(address);
        p.setPhone(phone);
        p.setInsuranceId(insuranceId);
        p.setPrimaryCarePhysician(pcp);
        return patientRepository.save(p);
    }

    private Room saveRoom(int number, String type, int floor, int code) {
        Room r = new Room();
        r.setRoomNumber(number);
        r.setRoomType(type);
        r.setBlockFloor(floor);
        r.setBlockCode(code);
        r.setUnavailable(false);
        return roomRepository.save(r);
    }

    // ── Tests ──────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should save and fetch stay by id")
    void testSaveAndFindById() {

        Physician pcp = savePhysician(8001);
        Patient patient = savePatient(8101, "Tony Stark", "Malibu Point", "555-0001", 10000001, pcp);
        Room room = saveRoom(8201, "ICU", 1, 1);

        Stay stay = new Stay(8001, patient, room,
                LocalDateTime.of(2026, 5, 16, 10, 0),
                LocalDateTime.of(2026, 5, 18, 10, 0));
        stayRepository.save(stay);

        Optional<Stay> saved = stayRepository.findById(8001);
        assertThat(saved).isPresent();
        assertThat(saved.get().getStayId()).isEqualTo(8001);
    }

    @Test
    @DisplayName("Should find stay by patient ssn")
    void testFindByPatientSsn() {

        Physician pcp = savePhysician(8002);
        Patient patient = savePatient(8102, "Bruce Wayne", "Wayne Manor", "555-0002", 10000002, pcp);
        Room room = saveRoom(8202, "General", 2, 2);

        stayRepository.save(new Stay(8002, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(2)));

        List<Stay> stays = stayRepository.findByPatient_Ssn(8102);
        assertThat(stays).hasSize(1);
    }

    @Test
    @DisplayName("Should count stays by patient ssn")
    void testCountByPatientSsn() {

        Physician pcp = savePhysician(8003);
        Patient patient = savePatient(8103, "Peter Parker", "Queens, NY", "555-0003", 10000003, pcp);
        Room room = saveRoom(8203, "Private", 3, 3);

        stayRepository.save(new Stay(8003, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
        stayRepository.save(new Stay(8004, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(3)));

        long count = stayRepository.countByPatient_Ssn(8103);
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should find stay by room number")
    void testFindByRoomNumber() {

        Physician pcp = savePhysician(8004);
        Patient patient = savePatient(8104, "Clark Kent", "Metropolis", "555-0004", 10000004, pcp);
        Room room = saveRoom(8204, "Emergency", 4, 4);

        stayRepository.save(new Stay(8005, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(5)));

        List<Stay> stays = stayRepository.findByRoom_RoomNumber(8204);
        assertThat(stays).hasSize(1);
    }

    @Test
    @DisplayName("Should count stays by room number")
    void testCountByRoomNumber() {

        Physician pcp = savePhysician(8005);
        Patient patient = savePatient(8105, "Steve Rogers", "Brooklyn, NY", "555-0005", 10000005, pcp);
        Room room = saveRoom(8205, "ICU", 5, 5);

        stayRepository.save(new Stay(8006, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(2)));

        long count = stayRepository.countByRoom_RoomNumber(8205);
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find stays between stay start dates")
    void testFindByStayStartBetween() {

        Physician pcp = savePhysician(8006);
        Patient patient = savePatient(8106, "Natasha", "Moscow", "555-0006", 10000006, pcp);
        Room room = saveRoom(8206, "General", 6, 6);

        stayRepository.save(new Stay(8007, patient, room,
                LocalDateTime.of(2026, 5, 10, 10, 0),
                LocalDateTime.of(2026, 5, 15, 10, 0)));

        List<Stay> stays = stayRepository.findByStayStartBetween(
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 20, 0, 0));

        assertThat(stays).isNotEmpty();
    }

    @Test
    @DisplayName("Should find active stays ordered by stay start asc")
    void testFindByStayEndAfterOrderByStayStartAsc() {

        Physician pcp = savePhysician(8007);
        Patient patient = savePatient(8107, "Wanda", "Westview, NJ", "555-0007", 10000007, pcp);
        Room room = saveRoom(8207, "VIP", 7, 7);

        stayRepository.save(new Stay(8008, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(2)));

        List<Stay> stays = stayRepository.findByStayEndAfterOrderByStayStartAsc(LocalDateTime.now().minusHours(1));
        assertThat(stays).isNotEmpty();
    }

    @Test
    @DisplayName("Should return all stays ordered by stay start desc")
    void testFindAllByOrderByStayStartDesc() {

        List<Stay> stays = stayRepository.findAllByOrderByStayStartDesc();
        assertThat(stays).isNotNull();
    }

    @Test
    @DisplayName("Should return projected stays")
    void testFindAllProjectedBy() {

        Pageable pageable = PageRequest.of(0, 5);
        Page<StayProjection> page = stayRepository.findAllProjectedBy(pageable);
        assertThat(page).isNotNull();
    }

    @Test
    @DisplayName("Should return projected stay by id")
    void testFindProjectedByStayId() {

        Physician pcp = savePhysician(8008);
        Patient patient = savePatient(8108, "Thor", "Asgard", "555-0008", 10000008, pcp);
        Room room = saveRoom(8208, "ICU", 8, 8);

        stayRepository.save(new Stay(8009, patient, room, LocalDateTime.now(), LocalDateTime.now().plusDays(1)));

        Optional<StayProjection> projection = stayRepository.findProjectedByStayId(8009);
        assertThat(projection).isPresent();
    }
}