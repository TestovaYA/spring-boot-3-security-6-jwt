package fr.mossaab.security.exception;

public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException(Long roomId) {
        super("Room with ID " + roomId + " not found.");
    }
}
