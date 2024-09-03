package fr.mossaab.security.service;

import fr.mossaab.security.entities.Room;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms();
    List<Room> getNonVipRooms();
    Room createRoom(Room room);
    Room updateRoom(Long id, Room roomDetails);
    boolean deleteRoom(Long id);
}
