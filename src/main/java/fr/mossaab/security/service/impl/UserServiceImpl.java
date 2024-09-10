package fr.mossaab.security.service.impl;

import fr.mossaab.security.entities.User;
import fr.mossaab.security.repository.UserRepository;
import fr.mossaab.security.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User userToUpdate = optionalUser.get();
            userToUpdate.setFirstname(userDetails.getFirstname());
            userToUpdate.setLastname(userDetails.getLastname());
            userToUpdate.setEmail(userDetails.getEmail());
            userToUpdate.setPassword(userDetails.getPassword());
            userToUpdate.setRole(userDetails.getRole());
            userToUpdate.setVip(userDetails.isVip());
            userToUpdate.setEnabled(userDetails.isEnabled());
            return userRepository.save(userToUpdate);
        }
        return null;
    }

    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
