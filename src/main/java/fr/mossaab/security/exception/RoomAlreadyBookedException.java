package fr.mossaab.security.exception;

public class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(Long roomId) {
        super("Room with ID " + roomId + " is already booked.");
    }
}