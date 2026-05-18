package com.hospital.ProcedureTest;

import com.hospital.entity.Procedure;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.ProcedureRepository;
import com.hospital.service.impl.ProcedureServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcedureServiceTest {

    @Mock
    private ProcedureRepository repo;

    @InjectMocks
    private ProcedureServiceImpl service;

    // ---------- helper ----------
    private Procedure createProcedure() {
        return new Procedure(1, "Blood Test", 500.0);
    }

    // ---------- TESTS ----------

    @Test
    void getAll_shouldReturnAllProcedures() {
        when(repo.findAll()).thenReturn(List.of(createProcedure()));

        List<Procedure> result = service.getAll();

        assertThat(result).hasSize(1);
        verify(repo).findAll();
    }

    @Test
    void getAllPageable_shouldReturnPage() {
        Pageable pageable = mock(Pageable.class);
        Page<Procedure> page = mock(Page.class);

        when(repo.findAll(pageable)).thenReturn(page);

        Page<Procedure> result = service.getAll(pageable);

        assertThat(result).isEqualTo(page);
    }

    @Test
    void getById_shouldReturnProcedure() {
        Procedure p = createProcedure();

        when(repo.findById(1)).thenReturn(Optional.of(p));

        Procedure result = service.getById(1);

        assertThat(result.getName()).isEqualTo("Blood Test");
    }

    @Test
    void getById_shouldThrowException() {
        when(repo.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void save_shouldSaveProcedure() {
        Procedure p = createProcedure();

        when(repo.save(p)).thenReturn(p);

        Procedure result = service.save(p);

        assertThat(result).isNotNull();
        verify(repo).save(p);
    }

    @Test
    void delete_shouldDeleteProcedure() {
        Procedure p = createProcedure();

        when(repo.findById(1)).thenReturn(Optional.of(p));

        service.delete(1);

        verify(repo).deleteById(1);
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(repo.existsById(1)).thenReturn(true);

        assertThat(service.existsById(1)).isTrue();
    }

    @Test
    void getByName_shouldReturnProcedure() {
        Procedure p = createProcedure();

        when(repo.findByName("Blood Test")).thenReturn(Optional.of(p));

        Optional<Procedure> result = service.getByName("Blood Test");

        assertThat(result).isPresent();
    }

    @Test
    void searchByName_shouldReturnList() {
        when(repo.findByNameContainingIgnoreCase("blood"))
                .thenReturn(List.of(createProcedure()));

        List<Procedure> result = service.searchByName("blood");

        assertThat(result).hasSize(1);
    }

    @Test
    void existsByName_shouldReturnTrue() {
        when(repo.existsByName("Blood Test")).thenReturn(true);

        assertThat(service.existsByName("Blood Test")).isTrue();
    }

    @Test
    void getByCostLessThan_shouldWork() {
        when(repo.findByCostLessThan(600.0))
                .thenReturn(List.of(createProcedure()));

        assertThat(service.getByCostLessThan(600.0)).hasSize(1);
    }

    @Test
    void getByCostBetween_shouldWork() {
        when(repo.findByCostBetween(100.0, 700.0))
                .thenReturn(List.of(createProcedure()));

        assertThat(service.getByCostBetween(100.0, 700.0)).hasSize(1);
    }

    @Test
    void getAllOrderedByCost_shouldWork() {
        when(repo.findAllByOrderByCostAsc())
                .thenReturn(List.of(createProcedure()));

        assertThat(service.getAllOrderedByCost()).hasSize(1);
    }

    @Test
    void getAllOrderedByName_shouldWork() {
        when(repo.findAllByOrderByNameAsc())
                .thenReturn(List.of(createProcedure()));

        assertThat(service.getAllOrderedByName()).hasSize(1);
    }
}