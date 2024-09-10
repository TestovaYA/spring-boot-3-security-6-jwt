package fr.mossaab.security.service;

import fr.mossaab.security.entities.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(Long roomId, boolean isCurrentUserVip);
    List<Booking> getUserBookings(Long userId);
    void deleteBooking(Long id);
    Booking updateBooking(Long id, Booking bookingDetails);

    Booking getBookingById(Long id);
}
