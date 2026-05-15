package com.hospital.OnCall;

import com.hospital.entity.OnCall;
import com.hospital.entity.OnCallId;
import com.hospital.repository.OnCallRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class OnCallRepositoryTest {

    @Autowired
    private OnCallRepository onCallRepository;

    @BeforeEach
    void setup() {

        onCallRepository.deleteAll();

        onCallRepository.save(
                new OnCall(
                        new OnCallId(101, 2, 1),
                        null,
                        null,
                        LocalDateTime.of(2026,5,15,8,0),
                        LocalDateTime.of(2026,5,15,16,0)
                )
        );

        onCallRepository.save(
                new OnCall(
                        new OnCallId(102, 2, 1),
                        null,
                        null,
                        LocalDateTime.of(2026,5,16,8,0),
                        LocalDateTime.of(2026,5,16,16,0)
                )
        );

        onCallRepository.save(
                new OnCall(
                        new OnCallId(101, 3, 2),
                        null,
                        null,
                        LocalDateTime.of(2026,5,17,8,0),
                        LocalDateTime.of(2026,5,17,16,0)
                )
        );
    }

    // 1. findById_NurseId()

    @Test
    @DisplayName("Should return schedules for existing nurse")
    void shouldReturnSchedulesForExistingNurse() {

        List<OnCall> schedules =
                onCallRepository.findById_NurseId(101);

        assertEquals(2, schedules.size());
    }

    @Test
    @DisplayName("Should return empty list for invalid nurse")
    void shouldReturnEmptyListForInvalidNurse() {

        List<OnCall> schedules =
                onCallRepository.findById_NurseId(999);

        assertTrue(schedules.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list for null nurse")
    void shouldReturnEmptyListForNullNurse() {

        List<OnCall> schedules =
                onCallRepository.findById_NurseId(null);

        assertTrue(schedules.isEmpty());
    }

    // 2. countById_NurseId()

    @Test
    @DisplayName("Should count schedules for existing nurse")
    void shouldCountSchedulesForExistingNurse() {

        long count =
                onCallRepository.countById_NurseId(101);

        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should return zero for invalid nurse")
    void shouldReturnZeroForInvalidNurse() {

        long count =
                onCallRepository.countById_NurseId(999);

        assertEquals(0, count);
    }

    // 3. findById_BlockFloorAndId_BlockCode()

    @Test
    @DisplayName("Should return records for valid block")
    void shouldReturnRecordsForValidBlock() {

        List<OnCall> records =
                onCallRepository
                        .findById_BlockFloorAndId_BlockCode(2,1);

        assertEquals(2, records.size());
    }

    @Test
    @DisplayName("Should return empty list for invalid block")
    void shouldReturnEmptyListForInvalidBlock() {

        List<OnCall> records =
                onCallRepository
                        .findById_BlockFloorAndId_BlockCode(99,99);

        assertTrue(records.isEmpty());
    }

    // 4. findById_BlockFloor()

    @Test
    @DisplayName("Should return records for existing block floor")
    void shouldReturnRecordsForExistingBlockFloor() {

        List<OnCall> records =
                onCallRepository.findById_BlockFloor(2);

        assertEquals(2, records.size());
    }

    @Test
    @DisplayName("Should return empty list for invalid block floor")
    void shouldReturnEmptyListForInvalidBlockFloor() {

        List<OnCall> records =
                onCallRepository.findById_BlockFloor(99);

        assertTrue(records.isEmpty());
    }

    // 5. findById_BlockCode()

    @Test
    @DisplayName("Should return records for existing block code")
    void shouldReturnRecordsForExistingBlockCode() {

        List<OnCall> records =
                onCallRepository.findById_BlockCode(1);

        assertEquals(2, records.size());
    }

    @Test
    @DisplayName("Should return empty list for invalid block code")
    void shouldReturnEmptyListForInvalidBlockCode() {

        List<OnCall> records =
                onCallRepository.findById_BlockCode(99);

        assertTrue(records.isEmpty());
    }

    // 6. findByOnCallStartBetween()

    @Test
    @DisplayName("Should return records within date range")
    void shouldReturnRecordsWithinDateRange() {

        LocalDateTime from =
                LocalDateTime.of(2026,5,15,0,0);

        LocalDateTime to =
                LocalDateTime.of(2026,5,17,23,59);

        List<OnCall> records =
                onCallRepository.findByOnCallStartBetween(from,to);

        assertEquals(3, records.size());
    }

    @Test
    @DisplayName("Should return empty list outside date range")
    void shouldReturnEmptyListOutsideDateRange() {

        LocalDateTime from =
                LocalDateTime.of(2030,1,1,0,0);

        LocalDateTime to =
                LocalDateTime.of(2030,12,31,23,59);

        List<OnCall> records =
                onCallRepository.findByOnCallStartBetween(from,to);

        assertTrue(records.isEmpty());
    }

    // 7. findById_NurseIdAndId_BlockFloorAndId_BlockCode()

    @Test
    @DisplayName("Should return matching combined record")
    void shouldReturnMatchingCombinedRecord() {

        List<OnCall> records =
                onCallRepository
                        .findById_NurseIdAndId_BlockFloorAndId_BlockCode(
                                101,
                                2,
                                1
                        );

        assertEquals(1, records.size());
    }

    @Test
    @DisplayName("Should return empty for invalid combined values")
    void shouldReturnEmptyForInvalidCombinedValues() {

        List<OnCall> records =
                onCallRepository
                        .findById_NurseIdAndId_BlockFloorAndId_BlockCode(
                                999,
                                99,
                                99
                        );

        assertTrue(records.isEmpty());
    }

    // 8. findById()

    @Test
    @DisplayName("Should return record for valid composite key")
    void shouldReturnRecordForValidCompositeKey() {

        OnCallId id =
                new OnCallId(101,2,1);

        Optional<OnCall> record =
                onCallRepository.findById(id);

        assertTrue(record.isPresent());
    }

    @Test
    @DisplayName("Should return empty for invalid composite key")
    void shouldReturnEmptyForInvalidCompositeKey() {

        OnCallId id =
                new OnCallId(999,99,99);

        Optional<OnCall> record =
                onCallRepository.findById(id);

        assertTrue(record.isEmpty());
    }
}