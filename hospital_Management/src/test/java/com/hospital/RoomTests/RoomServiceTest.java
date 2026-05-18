package com.hospital.RoomTests;

import com.hospital.entity.Room;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.RoomRepository;
import com.hospital.service.impl.RoomServiceImpl;
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
class RoomServiceTest {

    @Mock
    private RoomRepository repo;

    @InjectMocks
    private RoomServiceImpl service;

    // ---------- helper ----------
    private Room createRoom() {
        return new Room(101, "ICU", 1, 10, false);
    }

    // ---------- BASIC CRUD ----------

    @Test
    void getAll_shouldReturnRooms() {
        when(repo.findAll()).thenReturn(List.of(createRoom()));

        List<Room> result = service.getAll();

        assertThat(result).hasSize(1);
        verify(repo).findAll();
    }

    @Test
    void getAllPageable_shouldReturnPage() {
        Pageable pageable = mock(Pageable.class);
        Page<Room> page = mock(Page.class);

        when(repo.findAll(pageable)).thenReturn(page);

        Page<Room> result = service.getAll(pageable);

        assertThat(result).isEqualTo(page);
    }

    @Test
    void getById_shouldReturnRoom() {
        Room room = createRoom();

        when(repo.findById(101)).thenReturn(Optional.of(room));

        Room result = service.getById(101);

        assertThat(result.getRoomType()).isEqualTo("ICU");
    }

    @Test
    void getById_shouldThrowException() {
        when(repo.findById(101)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(101))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void save_shouldSaveRoom() {
        Room room = createRoom();

        when(repo.save(room)).thenReturn(room);

        Room result = service.save(room);

        assertThat(result).isNotNull();
        verify(repo).save(room);
    }

    @Test
    void delete_shouldDeleteRoom() {
        when(repo.findById(101)).thenReturn(Optional.of(createRoom()));

        service.delete(101);

        verify(repo).deleteById(101);
    }

    @Test
    void existsById_shouldReturnTrue() {
        when(repo.existsById(101)).thenReturn(true);

        assertThat(service.existsById(101)).isTrue();
    }

    // ---------- TYPE ----------

    @Test
    void getByType_shouldWork() {
        when(repo.findByRoomType("ICU"))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByType("ICU")).hasSize(1);
    }

    @Test
    void searchByType_shouldWork() {
        when(repo.findByRoomTypeContainingIgnoreCase("ic"))
                .thenReturn(List.of(createRoom()));

        assertThat(service.searchByType("ic")).hasSize(1);
    }

    @Test
    void getAllDistinctRoomTypes_shouldReturnDistinctSortedTypes() {

        Room r1 = new Room(101,"ICU",1,10,false);
        Room r2 = new Room(102,"General",1,10,false);
        Room r3 = new Room(103,"ICU",2,20,false);

        when(repo.findAll()).thenReturn(List.of(r1,r2,r3));

        List<String> result = service.getAllDistinctRoomTypes();

        assertThat(result).containsExactly("General","ICU");
    }

    // ---------- AVAILABILITY ----------

    @Test
    void getByAvailability_shouldWork() {
        when(repo.findByUnavailable(false))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByAvailability(false)).hasSize(1);
    }

    @Test
    void countByAvailability_shouldWork() {
        when(repo.countByUnavailable(false)).thenReturn(5L);

        assertThat(service.countByAvailability(false)).isEqualTo(5);
    }

    // ---------- BLOCK / FLOOR ----------

    @Test
    void getByFloor_shouldWork() {
        when(repo.findByBlockFloor(1))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByFloor(1)).hasSize(1);
    }

    @Test
    void getByBlockCode_shouldWork() {
        when(repo.findByBlockCode(10))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByBlockCode(10)).hasSize(1);
    }

    @Test
    void getByBlock_shouldWork() {
        when(repo.findByBlockFloorAndBlockCode(1,10))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByBlock(1,10)).hasSize(1);
    }

    // ---------- COMBINED FILTERS ----------

    @Test
    void getByTypeAndAvailability_shouldWork() {
        when(repo.findByRoomTypeAndUnavailable("ICU",false))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByTypeAndAvailability("ICU",false)).hasSize(1);
    }

    @Test
    void getByFloorAndAvailability_shouldWork() {
        when(repo.findByBlockFloorAndUnavailable(1,false))
                .thenReturn(List.of(createRoom()));

        assertThat(service.getByFloorAndAvailability(1,false)).hasSize(1);
    }

    // ---------- ORDERING ----------

    @Test
    void getAllOrderedByRoomNumber_shouldWork() {
        when(repo.findAllByOrderByRoomNumberAsc())
                .thenReturn(List.of(createRoom()));

        assertThat(service.getAllOrderedByRoomNumber()).hasSize(1);
    }
}