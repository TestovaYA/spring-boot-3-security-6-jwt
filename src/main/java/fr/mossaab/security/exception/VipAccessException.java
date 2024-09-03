package fr.mossaab.security.exception;

public class VipAccessException extends RuntimeException {
    public VipAccessException(Long roomId) {
        super("Only VIP users or admins can book VIP rooms (Room ID: " + roomId + ").");
    }
}
