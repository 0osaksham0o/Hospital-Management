package com.hospital.NurseTests;

import com.hospital.entity.Nurse;
import com.hospital.repository.NurseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class NurseRepoTest {

    @Autowired
    private NurseRepository nurseRepository;

    // =========================================================
    // findByName()
    // =========================================================

    @Test
    void testFindByName_WhenNameExists() {

        Nurse nurse = new Nurse();

        nurse.setEmployeeId(101);
        nurse.setName("Carla Espinosa");
        nurse.setPosition("Head Nurse");
        nurse.setRegistered(true);
        nurse.setSsn(111111110);

        nurseRepository.save(nurse);

        Optional<Nurse> result =
                nurseRepository.findByName("Carla Espinosa");

        assertTrue(result.isPresent());

        assertEquals("Head Nurse",
                result.get().getPosition());
    }

    @Test
    void testFindByName_WhenNameDoesNotExist() {

        Optional<Nurse> result =
                nurseRepository.findByName("Khushi");

        assertFalse(result.isPresent());
    }

    // =========================================================
    // findByNameContainingIgnoreCase()
    // =========================================================

    @Test
    void testFindByNameContainingIgnoreCase_WhenMatchExists() {

        Nurse nurse = new Nurse();

        nurse.setEmployeeId(102);
        nurse.setName("Laverne Roberts");
        nurse.setPosition("Nurse");
        nurse.setRegistered(true);
        nurse.setSsn(222222220);

        nurseRepository.save(nurse);

        List<Nurse> result =
                nurseRepository
                        .findByNameContainingIgnoreCase("laverne");

        assertFalse(result.isEmpty());

        assertEquals("Laverne Roberts",
                result.get(0).getName());
    }

    @Test
    void testFindByNameContainingIgnoreCase_WhenNoMatchExists() {

        List<Nurse> result =
                nurseRepository
                        .findByNameContainingIgnoreCase("xyz");

        assertTrue(result.isEmpty());
    }

    // =========================================================
    // findByPosition()
    // =========================================================

    @Test
    void testFindByPosition_WhenPositionExists() {

        Nurse nurse1 = new Nurse();

        nurse1.setEmployeeId(103);
        nurse1.setName("Nurse One");
        nurse1.setPosition("Nurse");
        nurse1.setRegistered(true);
        nurse1.setSsn(333333330);

        Nurse nurse2 = new Nurse();

        nurse2.setEmployeeId(104);
        nurse2.setName("Nurse Two");
        nurse2.setPosition("Nurse");
        nurse2.setRegistered(false);
        nurse2.setSsn(444444440);

        nurseRepository.save(nurse1);
        nurseRepository.save(nurse2);

        List<Nurse> result =
                nurseRepository.findByPosition("Nurse");

        assertEquals(2, result.size());
    }

    @Test
    void testFindByPosition_WhenPositionDoesNotExist() {

        List<Nurse> result =
                nurseRepository.findByPosition("ICU Specialist");

        assertTrue(result.isEmpty());
    }
}