package fr.mossaab.security.controller;

import fr.mossaab.security.entities.Room;
import fr.mossaab.security.entities.User;
import fr.mossaab.security.enums.Role;
import fr.mossaab.security.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class RoomController {
    private RoomService roomService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Room> getAllRooms() {
        if (isCurrentUserVip() || isCurrentUserAdmin()) {
            return roomService.getAllRooms();
        } else {
            return roomService.getNonVipRooms();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Room createRoom(@RequestBody Room room) {
        return roomService.createRoom(room);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDetails) {
        Room updatedRoom = roomService.updateRoom(id, roomDetails);
        if (updatedRoom != null) {
            return ResponseEntity.ok(updatedRoom);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (roomService.deleteRoom(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private boolean isCurrentUserVip() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();
            return currentUser.isVip();
        }
        return false;
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();
            return Role.ADMIN == currentUser.getRole();
        }
        return false;
    }
}