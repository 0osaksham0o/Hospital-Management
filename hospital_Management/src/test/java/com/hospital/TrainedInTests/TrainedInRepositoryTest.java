package com.hospital.TrainedInTests;

import com.hospital.entity.Physician;
import com.hospital.entity.Procedure;
import com.hospital.entity.TrainedIn;
import com.hospital.entity.TrainedInId;
import com.hospital.repository.PhysicianRepository;
import com.hospital.repository.ProcedureRepository;
import com.hospital.repository.TrainedInRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class TrainedInRepositoryTest {

    @Autowired private TrainedInRepository trainedInRepository;
    @Autowired private PhysicianRepository physicianRepository;
    @Autowired private ProcedureRepository procedureRepository;

    private static final int PHYSICIAN_ID   = 1;
    private static final int TREATMENT_CODE = 100;

    @BeforeEach
    void setup() {
        // Seed parent rows required by TrainedIn FKs
        Physician physician = physicianRepository.save(
                new Physician(PHYSICIAN_ID, "Dr. Seed", "General", 900001));
        Procedure procedure = procedureRepository.save(
                new Procedure(TREATMENT_CODE, "Seed Procedure", 500.0));

        LocalDateTime certified = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime expires   = LocalDateTime.of(2027, 1, 1, 0, 0);

        trainedInRepository.save(new TrainedIn(
                new TrainedInId(PHYSICIAN_ID, TREATMENT_CODE),
                physician, procedure,
                certified, expires
        ));
        trainedInRepository.flush();
    }

    @Test
    void testFindByPhysicianId_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        Integer physicianId = allRecords.get(0).getId().getPhysicianId();
        List<TrainedIn> result = trainedInRepository.findById_PhysicianId(physicianId);

        assertFalse(result.isEmpty());
        result.forEach(record ->
                assertEquals(physicianId, record.getId().getPhysicianId()));
    }

    @Test
    void testCountByPhysicianId_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        Integer physicianId = allRecords.get(0).getId().getPhysicianId();
        long count = trainedInRepository.countById_PhysicianId(physicianId);
        assertTrue(count > 0);
    }

    @Test
    void testFindByTreatmentCode_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        Integer treatmentCode = allRecords.get(0).getId().getTreatmentCode();
        List<TrainedIn> result = trainedInRepository.findById_TreatmentCode(treatmentCode);

        assertFalse(result.isEmpty());
        result.forEach(record ->
                assertEquals(treatmentCode, record.getId().getTreatmentCode()));
    }

    @Test
    void testCountByTreatmentCode_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        Integer treatmentCode = allRecords.get(0).getId().getTreatmentCode();
        long count = trainedInRepository.countById_TreatmentCode(treatmentCode);
        assertTrue(count > 0);
    }

    @Test
    void testFindByCertificationDateBetween_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        LocalDateTime from = allRecords.get(0).getCertificationDate().minusDays(1);
        LocalDateTime to   = allRecords.get(0).getCertificationDate().plusDays(1);

        List<TrainedIn> result = trainedInRepository.findByCertificationDateBetween(from, to);
        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCertificationExpiresBefore_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        LocalDateTime expiryDate = allRecords.get(0).getCertificationExpires().plusDays(1);
        List<TrainedIn> result = trainedInRepository.findByCertificationExpiresBefore(expiryDate);
        assertFalse(result.isEmpty());
    }

    @Test
    void testExistsByPhysicianIdAndTreatmentCode_FromDatabase() {
        List<TrainedIn> allRecords = trainedInRepository.findAll();
        assertFalse(allRecords.isEmpty());

        Integer physicianId   = allRecords.get(0).getId().getPhysicianId();
        Integer treatmentCode = allRecords.get(0).getId().getTreatmentCode();

        boolean exists = trainedInRepository.existsById_PhysicianIdAndId_TreatmentCode(
                physicianId, treatmentCode);
        assertTrue(exists);
    }

    @Test
    void testExistsByPhysicianIdAndTreatmentCode_Negative() {
        boolean exists = trainedInRepository.existsById_PhysicianIdAndId_TreatmentCode(-1, -1);
        assertFalse(exists);
    }
}