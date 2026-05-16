package com.hospital.MedicationTest;



import com.hospital.entity.Medication;
import com.hospital.projection.MedicationProjection;
import com.hospital.repository.MedicationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;

        import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MedicationRepositoryTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    @DisplayName("Should save and fetch medication by code")
    void testSaveAndFindByCode() {

        Medication medication = new Medication(
                1001,
                "Paracetamol",
                "Cipla",
                "Pain relief medicine"
        );

        medicationRepository.save(medication);

        Optional<Medication> saved =
                medicationRepository.findById(1001);

        assertThat(saved).isPresent();
        assertThat(saved.get().getName())
                .isEqualTo("Paracetamol");
    }

    @Test
    @DisplayName("Should find medication by exact name")
    void testFindByName() {

        Medication medication = new Medication(
                1002,
                "Ibuprofen",
                "Sun Pharma",
                "Anti inflammatory"
        );

        medicationRepository.save(medication);

        Optional<Medication> result =
                medicationRepository.findByName("Ibuprofen");

        assertThat(result).isPresent();
        assertThat(result.get().getBrand())
                .isEqualTo("Sun Pharma");
    }

    @Test
    @DisplayName("Should find medications by name containing keyword")
    void testFindByNameContainingIgnoreCase() {

        medicationRepository.save(
                new Medication(
                        1003,
                        "Paracetamol",
                        "Cipla",
                        "Fever reducer"
                )
        );

        medicationRepository.save(
                new Medication(
                        1004,
                        "Paracip",
                        "Apollo",
                        "Cold medicine"
                )
        );

        List<Medication> results =
                medicationRepository
                        .findByNameContainingIgnoreCase("para");

        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("Should check if medication exists by name")
    void testExistsByName() {

        Medication medication = new Medication(
                1005,
                "Aspirin",
                "Pfizer",
                "Blood thinner"
        );

        medicationRepository.save(medication);

        boolean exists =
                medicationRepository.existsByName("Aspirin");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find medications by brand")
    void testFindByBrand() {

        medicationRepository.save(
                new Medication(
                        1006,
                        "Crocin",
                        "GSK",
                        "Painkiller"
                )
        );

        List<Medication> results =
                medicationRepository.findByBrand("GSK");

        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Should find medications by brand keyword")
    void testFindByBrandContainingIgnoreCase() {

        medicationRepository.save(
                new Medication(
                        1007,
                        "Dolo",
                        "Micro Labs",
                        "Fever medicine"
                )
        );

        List<Medication> results =
                medicationRepository
                        .findByBrandContainingIgnoreCase("micro");

        assertThat(results).isNotEmpty();
    }

    @Test
    @DisplayName("Should find medications by description keyword")
    void testFindByDescriptionContainingIgnoreCase() {

        medicationRepository.save(
                new Medication(
                        1008,
                        "Azithromycin",
                        "Cipla",
                        "Antibiotic medicine"
                )
        );

        List<Medication> results =
                medicationRepository
                        .findByDescriptionContainingIgnoreCase("antibiotic");

        assertThat(results).hasSize(1);
    }

    @Test
    @DisplayName("Should return medications ordered by name ascending")
    void testFindAllByOrderByNameAsc() {

        medicationRepository.save(
                new Medication(
                        1009,
                        "Zincovit",
                        "Apex",
                        "Vitamin supplement"
                )
        );

        medicationRepository.save(
                new Medication(
                        1010,
                        "Aciloc",
                        "Cadila",
                        "Acidity medicine"
                )
        );

        List<Medication> results =
                medicationRepository.findAllByOrderByNameAsc();

        assertThat(results).hasSizeGreaterThanOrEqualTo(2);

        assertThat(results.get(0).getName())
                .isEqualTo("Aciloc");
    }

    @Test
    @DisplayName("Should return projected medications")
    void testFindAllProjectedBy() {

        medicationRepository.save(
                new Medication(
                        1011,
                        "Cetirizine",
                        "Dr Reddy",
                        "Allergy medicine"
                )
        );

        Pageable pageable = PageRequest.of(0, 5);

        Page<MedicationProjection> page =
                medicationRepository.findAllProjectedBy(pageable);

        assertThat(page.getContent()).isNotEmpty();
    }

    @Test
    @DisplayName("Should return projected medication by code")
    void testFindProjectedByCode() {

        medicationRepository.save(
                new Medication(
                        1012,
                        "Metformin",
                        "Sun Pharma",
                        "Diabetes medicine"
                )
        );

        Optional<MedicationProjection> result =
                medicationRepository.findProjectedByCode(1012);

        assertThat(result).isPresent();
    }
}