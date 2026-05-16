package com.hospital.service.impl;

import com.hospital.entity.Appointment;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository repo;

    @Override public List<Appointment> getAll()                          { return repo.findAll(); }
    @Override public Page<Appointment> getAll(Pageable p)                { return repo.findAll(p); }
    @Override public Appointment getById(Integer id)                     { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id)); }
    @Override public boolean existsById(Integer id)                      { return repo.existsById(id); }
    @Override public Appointment save(Appointment a)                     { return repo.save(a); }
    @Override public void delete(Integer id)                             { getById(id); repo.deleteById(id); }

    @Override public List<Appointment> getByPatient(Integer ssn)         { return repo.findByPatient_Ssn(ssn); }
    @Override public long countByPatient(Integer ssn)                    { return repo.countByPatient_Ssn(ssn); }

    @Override public List<Appointment> getByPhysician(Integer id)        { return repo.findByPhysician_EmployeeId(id); }
    @Override public long countByPhysician(Integer id)                   { return repo.countByPhysician_EmployeeId(id); }

    @Override public List<Appointment> getByPrepNurse(Integer id)        { return repo.findByPrepNurse_EmployeeId(id); }
    @Override public List<Appointment> getWithNoPrepNurse()              { return repo.findByPrepNurseIsNull(); }

    @Override public List<Appointment> getByExaminationRoom(String r)    { return repo.findByExaminationRoom(r); }

    @Override public List<Appointment> getByStartBetween(LocalDateTime f, LocalDateTime t)          { return repo.findByStartBetween(f, t); }
    @Override public List<Appointment> getUpcomingForPatient(Integer ssn, LocalDateTime now)        { return repo.findByPatient_SsnAndStartAfterOrderByStartAsc(ssn, now); }
    @Override public List<Appointment> getAllOrderedByStart()                                        { return repo.findAllByOrderByStartAsc(); }

    @Override public List<Appointment> getByPatientAndPhysician(Integer ssn, Integer pid)           { return repo.findByPatient_SsnAndPhysician_EmployeeId(ssn, pid); }
    @Override public List<Appointment> getByPhysicianInPeriod(Integer pid, LocalDateTime f, LocalDateTime t) { return repo.findByPhysician_EmployeeIdAndStartBetween(pid, f, t); }
}
