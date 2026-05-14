package com.hospital.TrainedInTests;

import com.hospital.entity.TrainedIn;
import com.hospital.repository.TrainedInRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TrainedInRepositoryTest {

    @Autowired
    private TrainedInRepository trainedInRepository;

    @Test
    void testFindByPhysicianId_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        Integer physicianId =
                allRecords.get(0).getId().getPhysicianId();

        List<TrainedIn> result =
                trainedInRepository.findById_PhysicianId(physicianId);

        assertFalse(result.isEmpty());

        result.forEach(record ->
                assertEquals(
                        physicianId,
                        record.getId().getPhysicianId()
                )
        );
    }

    @Test
    void testCountByPhysicianId_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        Integer physicianId =
                allRecords.get(0).getId().getPhysicianId();

        long count =
                trainedInRepository.countById_PhysicianId(physicianId);

        assertTrue(count > 0);
    }

    @Test
    void testFindByTreatmentCode_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        Integer treatmentCode =
                allRecords.get(0).getId().getTreatmentCode();

        List<TrainedIn> result =
                trainedInRepository.findById_TreatmentCode(treatmentCode);

        assertFalse(result.isEmpty());

        result.forEach(record ->
                assertEquals(
                        treatmentCode,
                        record.getId().getTreatmentCode()
                )
        );
    }

    @Test
    void testCountByTreatmentCode_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        Integer treatmentCode =
                allRecords.get(0).getId().getTreatmentCode();

        long count =
                trainedInRepository.countById_TreatmentCode(treatmentCode);

        assertTrue(count > 0);
    }

    @Test
    void testFindByCertificationDateBetween_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        LocalDateTime from =
                allRecords.get(0).getCertificationDate().minusDays(1);

        LocalDateTime to =
                allRecords.get(0).getCertificationDate().plusDays(1);

        List<TrainedIn> result =
                trainedInRepository.findByCertificationDateBetween(from, to);

        assertFalse(result.isEmpty());
    }

    @Test
    void testFindByCertificationExpiresBefore_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        LocalDateTime expiryDate =
                allRecords.get(0).getCertificationExpires().plusDays(1);

        List<TrainedIn> result =
                trainedInRepository
                        .findByCertificationExpiresBefore(expiryDate);

        assertFalse(result.isEmpty());
    }

    @Test
    void testExistsByPhysicianIdAndTreatmentCode_FromDatabase() {

        List<TrainedIn> allRecords = trainedInRepository.findAll();

        assertFalse(allRecords.isEmpty());

        Integer physicianId =
                allRecords.get(0).getId().getPhysicianId();

        Integer treatmentCode =
                allRecords.get(0).getId().getTreatmentCode();

        boolean exists =
                trainedInRepository
                        .existsById_PhysicianIdAndId_TreatmentCode(
                                physicianId,
                                treatmentCode
                        );

        assertTrue(exists);
    }

    @Test
    void testExistsByPhysicianIdAndTreatmentCode_Negative() {

        boolean exists =
                trainedInRepository
                        .existsById_PhysicianIdAndId_TreatmentCode(
                                -1,
                                -1
                        );

        assertFalse(exists);
    }
}