package fr.mossaab.security.service.impl;

import fr.mossaab.security.entities.Room;
import fr.mossaab.security.repository.RoomRepository;
import fr.mossaab.security.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getNonVipRooms() {
        return roomRepository.findByIsVipFalse();
    }

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room updateRoom(Long id, Room roomDetails) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            Room roomToUpdate = optionalRoom.get();
            roomToUpdate.setName(roomDetails.getName());
            roomToUpdate.setVip(roomDetails.isVip());
            return roomRepository.save(roomToUpdate);
        }
        return null;
    }

    @Override
    public boolean deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
