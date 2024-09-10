package fr.mossaab.security.service;

import fr.mossaab.security.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    boolean deleteUser(Long id);
}
