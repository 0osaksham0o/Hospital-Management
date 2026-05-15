package com.hospital.Nurse;

import com.hospital.entity.Nurse;
import com.hospital.repository.NurseRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class NurseRepositoryTest {

    @Autowired
    private NurseRepository nurseRepository;

    // 1. findByName(String name)

    @Test
    @DisplayName("Should return nurse when exact name exists")
    void shouldReturnNurseWhenExactNameExists() {

        Optional<Nurse> nurse =
                nurseRepository.findByName("Carla Espinosa");

        assertTrue(nurse.isPresent());
        assertEquals("Head Nurse", nurse.get().getPosition());
    }

    @Test
    @DisplayName("Should return empty when nurse name does not exist")
    void shouldReturnEmptyWhenNameDoesNotExist() {

        Optional<Nurse> nurse =
                nurseRepository.findByName("Khushi");

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
                nurseRepository.findByNameContainingIgnoreCase("arla");

        assertFalse(nurses.isEmpty());
    }

    @Test
    @DisplayName("Should perform case insensitive search")
    void shouldPerformCaseInsensitiveSearch() {

        List<Nurse> nurses =
                nurseRepository.findByNameContainingIgnoreCase("CARLA");

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

        assertEquals(2, nurses.size());
    }

    @Test
    @DisplayName("Should return nurse with Head Nurse position")
    void shouldReturnHeadNurse() {

        List<Nurse> nurses =
                nurseRepository.findByPosition("Head Nurse");

        assertEquals(1, nurses.size());
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

        assertEquals(2, nurses.size());

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

        assertEquals(1, nurses.size());

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

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should count unregistered nurses correctly")
    void shouldCountUnregisteredNursesCorrectly() {

        long count =
                nurseRepository.countByRegistered(false);

        assertEquals(1, count);
    }

    // 6. findBySsn(Integer ssn)

    @Test
    @DisplayName("Should return nurse when SSN exists")
    void shouldReturnNurseWhenSsnExists() {

        Optional<Nurse> nurse =
                nurseRepository.findBySsn(111111110);

        assertTrue(nurse.isPresent());

        assertEquals(
                "Carla Espinosa",
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
                nurseRepository.existsBySsn(111111110);

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