package com.hospital.AffiliatedWithTest;

import com.hospital.entity.AffiliatedWith;
import com.hospital.entity.AffiliatedWithId;
import com.hospital.entity.Department;
import com.hospital.entity.Physician;
import com.hospital.repository.AffiliatedWithRepository;
import com.hospital.repository.DepartmentRepository;
import com.hospital.repository.PhysicianRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AffiliatedWithTest {

    @Autowired
    private AffiliatedWithRepository affiliatedWithRepository;

    @Autowired
    private PhysicianRepository physicianRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private AffiliatedWith affiliation1;
    private AffiliatedWith affiliation2;

    @BeforeEach
    void setUp() {

        // =========================
        // Physicians
        // =========================

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

        physicianRepository.saveAll(
                List.of(physician1, physician2)
        );

        // =========================
        // Departments
        // =========================

        Department department1 = new Department();
        department1.setDepartmentId(10);
        department1.setName("Emergency");

        // IMPORTANT:
        // head column is NOT NULL
        // assuming head is mapped to Physician
        department1.setHead(physician1);

        Department department2 = new Department();
        department2.setDepartmentId(20);
        department2.setName("Surgery");
        department2.setHead(physician2);

        departmentRepository.saveAll(
                List.of(department1, department2)
        );

        // =========================
        // Affiliations
        // =========================

        affiliation1 = new AffiliatedWith(
                new AffiliatedWithId(1, 10),
                physician1,
                department1,
                true
        );

        affiliation2 = new AffiliatedWith(
                new AffiliatedWithId(2, 20),
                physician2,
                department2,
                false
        );

        affiliatedWithRepository.saveAll(
                List.of(affiliation1, affiliation2)
        );
    }

    @Test
    void findById_PhysicianId_returnsAffiliation() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findById_PhysicianId(1);

        assertThat(result)
                .hasSize(1)
                .contains(affiliation1);
    }

    @Test
    void findById_DepartmentId_returnsAffiliation() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findById_DepartmentId(10);

        assertThat(result)
                .hasSize(1)
                .contains(affiliation1);
    }

    @Test
    void findByPrimaryAffiliation_returnsTrueAffiliations() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findByPrimaryAffiliation(true);

        assertThat(result)
                .hasSize(1)
                .contains(affiliation1);
    }

    @Test
    void countById_PhysicianId_returnsCount() {

        long count =
                affiliatedWithRepository
                        .countById_PhysicianId(1);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void countById_DepartmentId_returnsCount() {

        long count =
                affiliatedWithRepository
                        .countById_DepartmentId(10);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void existsById_PhysicianIdAndId_DepartmentId_returnsTrue() {

        boolean exists =
                affiliatedWithRepository
                        .existsById_PhysicianIdAndId_DepartmentId(
                                1,
                                10
                        );

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_PhysicianIdAndId_DepartmentId_returnsFalse() {

        boolean exists =
                affiliatedWithRepository
                        .existsById_PhysicianIdAndId_DepartmentId(
                                99,
                                99
                        );

        assertThat(exists).isFalse();
    }

    @Test
    void findById_PhysicianIdAndPrimaryAffiliation_returnsResult() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findById_PhysicianIdAndPrimaryAffiliation(
                                1,
                                true
                        );

        assertThat(result)
                .hasSize(1)
                .contains(affiliation1);
    }

    @Test
    void findById_DepartmentIdAndPrimaryAffiliation_returnsResult() {

        List<AffiliatedWith> result =
                affiliatedWithRepository
                        .findById_DepartmentIdAndPrimaryAffiliation(
                                10,
                                true
                        );

        assertThat(result)
                .hasSize(1)
                .contains(affiliation1);
    }
}