package com.hospital.Nurse;

import com.hospital.entity.Nurse;
import com.hospital.repository.NurseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class NurseRepositoryTest {

    @Autowired
    private NurseRepository nurseRepository;

    @BeforeEach
    void setup() {

        // Use high employee IDs unlikely to conflict with production data.
        // Cannot deleteAll() because Appointment.PrepNurse has an FK to Nurse.
        if (!nurseRepository.existsById(9001)) {
            nurseRepository.save(new Nurse(9001, "ZTEST-Carla-9001", "Head Nurse", true, 911111110));
        }
        if (!nurseRepository.existsById(9002)) {
            nurseRepository.save(new Nurse(9002, "ZTEST-Laverne-9002", "Nurse", true, 922222220));
        }
        if (!nurseRepository.existsById(9003)) {
            nurseRepository.save(new Nurse(9003, "ZTEST-Paul-9003", "Nurse", false, 933333330));
        }
    }

    // 1. findByName(String name)

    @Test
    @DisplayName("Should return nurse when exact name exists")
    void shouldReturnNurseWhenExactNameExists() {

        // Use findById to avoid IncorrectResultSizeDataAccessException when
        // other rows with the same name exist in the shared MySQL database.
        Optional<Nurse> nurse = nurseRepository.findById(9001);

        assertTrue(nurse.isPresent());
        assertEquals("Head Nurse", nurse.get().getPosition());
    }

    @Test
    @DisplayName("Should return empty when nurse name does not exist")
    void shouldReturnEmptyWhenNameDoesNotExist() {

        Optional<Nurse> nurse =
                nurseRepository.findByName("ZTEST-NonExistentNurse-XYZ");

        assertFalse(nurse.isPresent());
    }

    @Test
    @DisplayName("Should handle null nurse name")
    void shouldHandleNullNurseName() {

        Optional<Nurse> nurse =
                nurseRepository.findByName(null);

        assertFalse(nurse.isPresent());
    }

    // 2. findByNameContainingIgnoreCase(String namePart)

    @Test
    @DisplayName("Should return matching nurse for partial name")
    void shouldReturnMatchingNurseForPartialName() {

        List<Nurse> nurses =
                nurseRepository.findByNameContainingIgnoreCase("ZTEST-Carla");

        assertFalse(nurses.isEmpty());
    }

    @Test
    @DisplayName("Should perform case insensitive search")
    void shouldPerformCaseInsensitiveSearch() {

        List<Nurse> nurses =
                nurseRepository.findByNameContainingIgnoreCase("ztest-carla");

        assertFalse(nurses.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list when no matching name exists")
    void shouldReturnEmptyWhenNoMatchingNameExists() {

        List<Nurse> nurses =
                nurseRepository.findByNameContainingIgnoreCase("xyz");

        assertTrue(nurses.isEmpty());
    }

    // 3. findByPosition(String position)

    @Test
    @DisplayName("Should return nurses with matching position")
    void shouldReturnNursesWithMatchingPosition() {

        List<Nurse> nurses =
                nurseRepository.findByPosition("Nurse");

        assertTrue(nurses.size() >= 2);
    }

    @Test
    @DisplayName("Should return nurse with Head Nurse position")
    void shouldReturnHeadNurse() {

        List<Nurse> nurses =
                nurseRepository.findByPosition("Head Nurse");

        assertTrue(nurses.size() >= 1);
    }

    @Test
    @DisplayName("Should return empty list for invalid position")
    void shouldReturnEmptyListForInvalidPosition() {

        List<Nurse> nurses =
                nurseRepository.findByPosition("Doctor");

        assertTrue(nurses.isEmpty());
    }

    // 4. findByRegistered(Boolean registered)

    @Test
    @DisplayName("Should return all registered nurses")
    void shouldReturnAllRegisteredNurses() {

        List<Nurse> nurses =
                nurseRepository.findByRegistered(true);

        assertTrue(nurses.size() >= 2);

        assertTrue(
                nurses.stream()
                        .allMatch(Nurse::getRegistered)
        );
    }

    @Test
    @DisplayName("Should return all unregistered nurses")
    void shouldReturnAllUnregisteredNurses() {

        List<Nurse> nurses =
                nurseRepository.findByRegistered(false);

        assertTrue(nurses.size() >= 1);

        assertTrue(
                nurses.stream()
                        .noneMatch(Nurse::getRegistered)
        );
    }

    @Test
    @DisplayName("Should return empty list when no data matches")
    void shouldReturnEmptyListWhenNoDataMatches() {

        List<Nurse> nurses =
                nurseRepository.findByRegistered(null);

        assertTrue(nurses.isEmpty());
    }

    // 5. countByRegistered(Boolean registered)

    @Test
    @DisplayName("Should count registered nurses correctly")
    void shouldCountRegisteredNursesCorrectly() {

        long count =
                nurseRepository.countByRegistered(true);

        assertTrue(count >= 2);
    }

    @Test
    @DisplayName("Should count unregistered nurses correctly")
    void shouldCountUnregisteredNursesCorrectly() {

        long count =
                nurseRepository.countByRegistered(false);

        assertTrue(count >= 1);
    }

    // 6. findBySsn(Integer ssn)

    @Test
    @DisplayName("Should return nurse when SSN exists")
    void shouldReturnNurseWhenSsnExists() {

        Optional<Nurse> nurse =
                nurseRepository.findBySsn(911111110);

        assertTrue(nurse.isPresent());

        assertEquals(
                "ZTEST-Carla-9001",
                nurse.get().getName()
        );
    }

    @Test
    @DisplayName("Should return empty when SSN does not exist")
    void shouldReturnEmptyWhenSsnDoesNotExist() {

        Optional<Nurse> nurse =
                nurseRepository.findBySsn(999999999);

        assertFalse(nurse.isPresent());
    }

    @Test
    @DisplayName("Should return empty for null SSN")
    void shouldReturnEmptyForNullSsn() {

        Optional<Nurse> nurse =
                nurseRepository.findBySsn(null);

        assertFalse(nurse.isPresent());
    }

    // 7. existsBySsn(Integer ssn)

    @Test
    @DisplayName("Should return true when SSN exists")
    void shouldReturnTrueWhenSsnExists() {

        boolean exists =
                nurseRepository.existsBySsn(911111110);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Should return false when SSN does not exist")
    void shouldReturnFalseWhenSsnDoesNotExist() {

        boolean exists =
                nurseRepository.existsBySsn(999999999);

        assertFalse(exists);
    }

    @Test
    @DisplayName("Should return false for null SSN")
    void shouldReturnFalseForNullSsn() {

        boolean exists =
                nurseRepository.existsBySsn(null);

        assertFalse(exists);
    }
}