package fr.mossaab.security.repository;

import fr.mossaab.security.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsVipFalse();
}
