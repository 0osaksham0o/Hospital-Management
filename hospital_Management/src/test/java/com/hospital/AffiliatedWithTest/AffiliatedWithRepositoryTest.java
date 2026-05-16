package com.hospital.AffiliatedWithTest;

import com.hospital.entity.*;
import com.hospital.repository.AffiliatedWithRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AffiliatedWithRepositoryTest {

    @Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private AffiliatedWith affiliation1;
    private AffiliatedWith affiliation2;

    // -------------------------------------------------------
    // Test Data Setup
    // -------------------------------------------------------

    @BeforeEach
    void setUp() {

        // ===== Physicians =====
        Physician physician1 = new Physician();
        physician1.setEmployeeId(1);
        physician1.setName("John Doe");
        physician1.setPosition("Cardiologist");
        physician1.setSsn(111111111);

        Physician physician2 = new Physician();
        physician2.setEmployeeId(2);
        physician2.setName("Jane Smith");
        physician2.setPosition("Neurologist");
        physician2.setSsn(222222222);

        physicianRepository.saveAll(List.of(physician1, physician2));

        // ===== Departments =====
        Department department1 = new Department();
        department1.setDepartmentId(10);
        department1.setName("Emergency");
        department1.setHead(physician1);   // NOT NULL relation

        Department department2 = new Department();
        department2.setDepartmentId(20);
        department2.setName("Surgery");
        department2.setHead(physician2);

        departmentRepository.saveAll(List.of(department1, department2));

        // ===== Composite IDs =====
        AffiliatedWithId id1 = new AffiliatedWithId(1, 10);
        AffiliatedWithId id2 = new AffiliatedWithId(2, 20);

        affiliation1 = new AffiliatedWith(id1, physician1, department1, true);
        affiliation2 = new AffiliatedWith(id2, physician2, department2, false);

        affiliatedWithRepository.saveAll(List.of(affiliation1, affiliation2));
    }

    // -------------------------------------------------------
    // Tests
    // -------------------------------------------------------

    @Test
    @DisplayName("Find by Physician ID")
    void findById_PhysicianId_returnsAffiliation() {

        List<AffiliatedWith> result =
                affiliatedWithRepository.findById_PhysicianId(1);

        assertThat(result)
                .hasSize(1)
                .extracting(a -> a.getId().getPhysicianId())
                .contains(1);
    }

    @Test
    @DisplayName("Find by Department ID")
    void findById_DepartmentId_returnsAffiliation() {

        List<AffiliatedWith> result =
                affiliatedWithRepository.findById_DepartmentId(10);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Find Primary Affiliations")
    void findByPrimaryAffiliation_returnsTrueAffiliations() {

        List<AffiliatedWith> result =
                affiliatedWithRepository.findByPrimaryAffiliation(true);

        assertThat(result)
                .hasSize(1)
                .allMatch(AffiliatedWith::getPrimaryAffiliation);
    }

    @Test
    @DisplayName("Count by Physician")
    void countById_PhysicianId_returnsCount() {

        long count =
                affiliatedWithRepository.countById_PhysicianId(1);

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Count by Department")
    void countById_DepartmentId_returnsCount() {

        long count =
                affiliatedWithRepository.countById_DepartmentId(10);

        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("Exists Affiliation TRUE")
    void existsById_PhysicianIdAndId_DepartmentId_returnsTrue() {

        boolean exists =
                affiliatedWithRepository
                        .existsById_PhysicianIdAndId_DepartmentId(1, 10);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Exists Affiliation FALSE")
    void existsById_PhysicianIdAndId_DepartmentId_returnsFalse() {

        boolean exists =
                affiliatedWithRepository
                        .existsById_PhysicianIdAndId_DepartmentId(99, 99);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Find by Physician + Primary")
    void findById_PhysicianIdAndPrimaryAffiliation_returnsResult() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findById_PhysicianIdAndPrimaryAffiliation(1, true);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Find by Department + Primary")
    void findById_DepartmentIdAndPrimaryAffiliation_returnsResult() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findById_DepartmentIdAndPrimaryAffiliation(10, true);

        assertThat(result).hasSize(1);
    }
}