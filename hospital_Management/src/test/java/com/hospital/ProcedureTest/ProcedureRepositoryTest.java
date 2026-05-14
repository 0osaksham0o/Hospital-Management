package com.hospital.ProcedureTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.hospital.entity.Procedure;
import com.hospital.repository.ProcedureRepository;

@DataJpaTest
class rocedureRepositoryTest {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Test
    @DisplayName("Test findByNameContainingIgnoreCase")
    void testFindByNameContainingIgnoreCase() {

        procedureRepository.save(
                new Procedure(1,"Heart Surgery",50000.0));

        procedureRepository.save(
                new Procedure(2,"Dental Cleaning",3000.0));

        List<Procedure> result =
                procedureRepository.findByNameContainingIgnoreCase("heart");

        assertEquals(1,result.size());
        assertEquals("Heart Surgery",result.get(0).getName());
    }

    @Test
    @DisplayName("Test findByCostLessThan")
    void testFindByCostLessThan() {

        procedureRepository.save(
                new Procedure(3,"X-Ray",2000.0));

        procedureRepository.save(
                new Procedure(4,"MRI Scan",10000.0));

        List<Procedure> result =
                procedureRepository.findByCostLessThan(5000.0);

        assertEquals(1,result.size());
        assertEquals("X-Ray",result.get(0).getName());
    }

    @Test
    @DisplayName("Test findByCostBetween")
    void testFindByCostBetween() {

        procedureRepository.save(
                new Procedure(5,"Blood Test",1000.0));

        procedureRepository.save(
                new Procedure(6,"CT Scan",7000.0));

        procedureRepository.save(
                new Procedure(7,"Heart Surgery",50000.0));

        List<Procedure> result =
                procedureRepository.findByCostBetween(2000.0,10000.0);

        assertEquals(1,result.size());
        assertEquals("CT Scan",result.get(0).getName());
    }

    @Test
    @DisplayName("Test no result found")
    void testNoResultFound() {

        procedureRepository.save(
                new Procedure(8,"Eye Checkup",1500.0));

        List<Procedure> result =
                procedureRepository.findByNameContainingIgnoreCase("Cancer");

        assertTrue(result.isEmpty());
    }
}