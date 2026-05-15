package com.hospital.UndergoesTest;

import com.hospital.entity.Nurse;
import com.hospital.entity.Patient;
import com.hospital.entity.Physician;
import com.hospital.entity.Procedure;
import com.hospital.entity.Room;
import com.hospital.entity.Stay;
import com.hospital.entity.Undergoes;
import com.hospital.entity.UndergoesId;
import com.hospital.repository.NurseRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.PhysicianRepository;
import com.hospital.repository.ProcedureRepository;
import com.hospital.repository.RoomRepository;
import com.hospital.repository.StayRepository;
import com.hospital.repository.UndergoesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UndergoesRepositoryTest {

    @Autowired
    private UndergoesRepository undergoesRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private StayRepository stayRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("Save and find Undergoes by composite id")
    void saveAndFindByIdTest() {
        Undergoes undergoes = createAndSaveUndergoes();

        Undergoes found = undergoesRepository.findById(undergoes.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(undergoes.getId());
        assertThat(found.getPatient().getSsn()).isEqualTo(10001);
        assertThat(found.getProcedure().getCode()).isEqualTo(20001);
        assertThat(found.getStay().getStayId()).isEqualTo(30001);
    }

    @Test
    @DisplayName("Find Undergoes by patient, procedure, and stay ids")
    void findByEmbeddedIdFieldsTest() {
        Undergoes undergoes = createAndSaveUndergoes();

        List<Undergoes> byPatient =
                undergoesRepository.findById_PatientSsn(undergoes.getId().getPatientSsn());
        List<Undergoes> byProcedure =
                undergoesRepository.findById_ProcedureCode(undergoes.getId().getProcedureCode());
        List<Undergoes> byStay =
                undergoesRepository.findById_StayId(undergoes.getId().getStayId());

        assertThat(byPatient).extracting(record -> record.getId().getPatientSsn())
                .contains(10001);
        assertThat(byProcedure).extracting(record -> record.getId().getProcedureCode())
                .contains(20001);
        assertThat(byStay).extracting(record -> record.getId().getStayId())
                .contains(30001);
        assertThat(undergoesRepository.countById_PatientSsn(10001)).isEqualTo(1);
    }

    @Test
    @DisplayName("Find Undergoes by physician and assisting nurse")
    void findByPhysicianAndNurseTest() {
        createAndSaveUndergoes();

        List<Undergoes> byPhysician =
                undergoesRepository.findByPhysician_EmployeeId(40001);
        List<Undergoes> byNurse =
                undergoesRepository.findByAssistingNurse_EmployeeId(50001);
        List<Undergoes> byPatientAndPhysician =
                undergoesRepository.findById_PatientSsnAndPhysician_EmployeeId(10001, 40001);

        assertThat(byPhysician).hasSize(1);
        assertThat(byPhysician.get(0).getPhysician().getEmployeeId()).isEqualTo(40001);
        assertThat(undergoesRepository.countByPhysician_EmployeeId(40001)).isEqualTo(1);
        assertThat(byNurse).hasSize(1);
        assertThat(byNurse.get(0).getAssistingNurse().getEmployeeId()).isEqualTo(50001);
        assertThat(byPatientAndPhysician).hasSize(1);
    }

    @Test
    @DisplayName("Find Undergoes by date range")
    void findByDateUndergoesTest() {
        Undergoes undergoes = createAndSaveUndergoes();
        LocalDateTime dateUndergoes = undergoes.getDateUndergoes();

        List<Undergoes> between =
                undergoesRepository.findByDateUndergoesBetween(
                        dateUndergoes.minusHours(1),
                        dateUndergoes.plusHours(1)
                );
        List<Undergoes> after =
                undergoesRepository.findByDateUndergoesAfter(dateUndergoes.minusMinutes(1));

        assertThat(between).hasSize(1);
        assertThat(after).hasSize(1);
    }

    private Undergoes createAndSaveUndergoes() {
        Physician primaryCarePhysician =
                physicianRepository.save(new Physician(41001, "Dr Primary", "General", 91001));
        Physician physician =
                physicianRepository.save(new Physician(40001, "Dr Surgeon", "Surgeon", 90001));
        Nurse nurse =
                nurseRepository.save(new Nurse(50001, "Nurse Assist", "Assistant", true, 80001));
        Patient patient =
                patientRepository.save(new Patient(
                        10001,
                        "Test Patient",
                        "Test Address",
                        "9999990001",
                        70001,
                        primaryCarePhysician
                ));
        Procedure procedure =
                procedureRepository.save(new Procedure(20001, "Test Procedure", 2500.00));
        Room room =
                roomRepository.save(new Room(60001, "Single", 1, 1, false));
        Stay stay =
                stayRepository.save(new Stay(
                        30001,
                        patient,
                        room,
                        LocalDateTime.of(2026, 5, 15, 9, 0),
                        LocalDateTime.of(2026, 5, 16, 9, 0)
                ));

        UndergoesId id = new UndergoesId(patient.getSsn(), procedure.getCode(), stay.getStayId());
        Undergoes undergoes = new Undergoes(
                id,
                patient,
                procedure,
                stay,
                LocalDateTime.of(2026, 5, 15, 10, 30),
                physician,
                nurse
        );

        return undergoesRepository.saveAndFlush(undergoes);
    }
}
