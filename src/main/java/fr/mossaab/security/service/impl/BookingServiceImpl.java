package fr.mossaab.security.service.impl;

import fr.mossaab.security.entities.Booking;
import fr.mossaab.security.entities.Room;
import fr.mossaab.security.exception.RoomAlreadyBookedException;
import fr.mossaab.security.exception.RoomNotFoundException;
import fr.mossaab.security.exception.VipAccessException;
import fr.mossaab.security.repository.BookingRepository;
import fr.mossaab.security.repository.RoomRepository;
import fr.mossaab.security.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private RoomRepository roomRepository;

    @Override
    public Booking createBooking(Long roomId, boolean isCurrentUserVip) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId));

        if (room.isBooking()) {
            throw new RoomAlreadyBookedException(roomId);
        }

        if (room.isVip() && !isCurrentUserVip) {
            throw new VipAccessException(roomId);
        }

        room.setBooking(true);
        roomRepository.save(room);

        Booking booking = new Booking();
        booking.setRoom(room);

        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public Booking updateBooking(Long id, Booking bookingDetails) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if (optionalBooking.isPresent()) {
            Booking bookingToUpdate = optionalBooking.get();
            bookingToUpdate.setRoom(bookingDetails.getRoom());
            bookingToUpdate.setUser(bookingDetails.getUser());
            return bookingRepository.save(bookingToUpdate);
        }
        return null;
    }

    @Override
    public Booking getBookingById(Long id){
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
        }
    }
}
