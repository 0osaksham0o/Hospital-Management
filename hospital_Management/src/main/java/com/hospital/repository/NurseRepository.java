package com.hospital.repository;

import com.hospital.entity.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource(exported = false)
public interface NurseRepository extends JpaRepository<Nurse, Integer> {


    Optional<Nurse> findByName(String name);

    List<Nurse> findByNameContainingIgnoreCase(String namePart);

    List<Nurse> findByPosition(String position);

    List<Nurse> findByRegistered(Boolean registered);

    long countByRegistered(Boolean registered);

    Optional<Nurse> findBySsn(Integer ssn);

    boolean existsBySsn(Integer ssn);

//    @Query("SELECT n FROM Nurse n WHERE n.registered = true ORDER BY n.name ASC")
//    List<Nurse> findAllRegisteredOrderedByName();


}
