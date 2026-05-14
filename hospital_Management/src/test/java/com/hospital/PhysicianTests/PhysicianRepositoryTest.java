package com.hospital.PhysicianTests;

import com.hospital.entity.Physician;
import com.hospital.repository.PhysicianRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PhysicianRepositoryTest {

    @Autowired
    private PhysicianRepository physicianRepository;

    private Physician createPhysician(
            Integer employeeId,
            String name,
            String position,
            Integer ssn
    ) {
        return new Physician(employeeId, name, position, ssn);
    }

    @Test
    void testSavePhysician() {
        Physician physician =
                createPhysician(101, "Dr John Smith", "Cardiologist", 111111111);

        Physician savedPhysician =
                physicianRepository.save(physician);

        assertEquals(101, savedPhysician.getEmployeeId());
        assertEquals("Dr John Smith", savedPhysician.getName());
        assertEquals("Cardiologist", savedPhysician.getPosition());
        assertEquals(111111111, savedPhysician.getSsn());
    }

    @Test
    void testFindById_WhenPhysicianExists() {
        Physician physician =
                createPhysician(102, "Dr Sarah Jones", "Neurologist", 222222222);

        physicianRepository.save(physician);

        Optional<Physician> result =
                physicianRepository.findById(102);

        assertTrue(result.isPresent());
        assertEquals("Dr Sarah Jones", result.get().getName());
    }

    @Test
    void testFindById_WhenPhysicianDoesNotExist() {
        Optional<Physician> result =
                physicianRepository.findById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll() {
        physicianRepository.save(
                createPhysician(103, "Dr Alex Carter", "Surgeon", 333333333)
        );
        physicianRepository.save(
                createPhysician(104, "Dr Emily Stone", "Surgeon", 444444444)
        );

        List<Physician> result =
                physicianRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testFindAllWithPageable() {
        physicianRepository.save(
                createPhysician(105, "Dr One", "General Physician", 555555555)
        );
        physicianRepository.save(
                createPhysician(106, "Dr Two", "General Physician", 666666666)
        );
        physicianRepository.save(
                createPhysician(107, "Dr Three", "General Physician", 777777777)
        );

        Page<Physician> result =
                physicianRepository.findAll(PageRequest.of(0, 2));

        assertEquals(2, result.getContent().size());
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void testExistsById_WhenPhysicianExists() {
        physicianRepository.save(
                createPhysician(108, "Dr Exists", "Dermatologist", 888888888)
        );

        boolean result =
                physicianRepository.existsById(108);

        assertTrue(result);
    }

    @Test
    void testExistsById_WhenPhysicianDoesNotExist() {
        boolean result =
                physicianRepository.existsById(999);

        assertFalse(result);
    }

    @Test
    void testDeleteById() {
        physicianRepository.save(
                createPhysician(109, "Dr Delete", "Radiologist", 999999991)
        );

        physicianRepository.deleteById(109);

        assertFalse(physicianRepository.existsById(109));
    }

    @Test
    void testFindByName_WhenNameExists() {
        physicianRepository.save(
                createPhysician(110, "Dr Gregory House", "Diagnostician", 999999992)
        );

        Optional<Physician> result =
                physicianRepository.findByName("Dr Gregory House");

        assertTrue(result.isPresent());
        assertEquals("Diagnostician", result.get().getPosition());
    }

    @Test
    void testFindByName_WhenNameDoesNotExist() {
        Optional<Physician> result =
                physicianRepository.findByName("Unknown Doctor");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByNameContainingIgnoreCase_WhenMatchExists() {
        physicianRepository.save(
                createPhysician(111, "Dr Meredith Grey", "Surgeon", 999999993)
        );

        List<Physician> result =
                physicianRepository.findByNameContainingIgnoreCase("meredith");

        assertEquals(1, result.size());
        assertEquals("Dr Meredith Grey", result.get(0).getName());
    }

    @Test
    void testFindByNameContainingIgnoreCase_WhenNoMatchExists() {
        List<Physician> result =
                physicianRepository.findByNameContainingIgnoreCase("missing");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByPosition_WhenPositionExists() {
        physicianRepository.save(
                createPhysician(112, "Dr Leonard McCoy", "Chief Surgeon", 999999994)
        );
        physicianRepository.save(
                createPhysician(113, "Dr Cristina Yang", "Chief Surgeon", 999999995)
        );

        List<Physician> result =
                physicianRepository.findByPosition("Chief Surgeon");

        assertEquals(2, result.size());
    }

    @Test
    void testFindByPosition_WhenPositionDoesNotExist() {
        List<Physician> result =
                physicianRepository.findByPosition("Unknown Position");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByPositionContainingIgnoreCase_WhenMatchExists() {
        physicianRepository.save(
                createPhysician(114, "Dr Stephen Strange", "Neurosurgeon", 999999996)
        );

        List<Physician> result =
                physicianRepository.findByPositionContainingIgnoreCase("neuro");

        assertEquals(1, result.size());
        assertEquals("Neurosurgeon", result.get(0).getPosition());
    }

    @Test
    void testFindByPositionContainingIgnoreCase_WhenNoMatchExists() {
        List<Physician> result =
                physicianRepository.findByPositionContainingIgnoreCase("cardio");

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindBySsn_WhenSsnExists() {
        physicianRepository.save(
                createPhysician(115, "Dr Ssn Match", "Pathologist", 999999997)
        );

        Optional<Physician> result =
                physicianRepository.findBySsn(999999997);

        assertTrue(result.isPresent());
        assertEquals(115, result.get().getEmployeeId());
    }

    @Test
    void testFindBySsn_WhenSsnDoesNotExist() {
        Optional<Physician> result =
                physicianRepository.findBySsn(123123123);

        assertTrue(result.isEmpty());
    }

    @Test
    void testExistsBySsn_WhenSsnExists() {
        physicianRepository.save(
                createPhysician(116, "Dr Ssn Exists", "Oncologist", 999999998)
        );

        boolean result =
                physicianRepository.existsBySsn(999999998);

        assertTrue(result);
    }

    @Test
    void testExistsBySsn_WhenSsnDoesNotExist() {
        boolean result =
                physicianRepository.existsBySsn(321321321);

        assertFalse(result);
    }
}
