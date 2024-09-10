package fr.mossaab.security.service.impl;

import fr.mossaab.security.enums.TokenType;
import fr.mossaab.security.payload.request.AuthenticationRequest;
import fr.mossaab.security.payload.request.RegisterRequest;
import fr.mossaab.security.payload.response.AuthenticationResponse;
import fr.mossaab.security.service.AuthenticationService;
import fr.mossaab.security.service.JwtService;
import fr.mossaab.security.entities.User;
import fr.mossaab.security.repository.UserRepository;
import fr.mossaab.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.mail.SimpleMailMessage;
import java.util.UUID;

@Service @Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists.");
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isVip(request.isVip())
                .isEnabled(false)
                .build();

        String confirmationMailToken = UUID.randomUUID().toString();
        user.setConfirmationToken(confirmationMailToken);
        user = userRepository.save(user);

        sendConfirmationEmail(user.getEmail(), confirmationMailToken);

        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .email(user.getEmail())
                .id(user.getId())
                .roles(roles)
                .isEnable(false)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var roles = user.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(user.getEmail())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType( TokenType.BEARER.name())
                .isEnable(user.isEnabled())
                .build();
    }

    @Override
    public void sendConfirmationEmail(String email, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Пожалуйста, подтвердите вашу регистрацию");
            message.setTo(email);
            message.setFrom("***@yandex.ru");
            message.setText("Перейдите по следующей ссылке для подтверждения вашей регистрации: " + "http://localhost:8086/api/v1/auth/confirm?token=" + token);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean confirmUser(String token) {
        Optional<User> userOptional = userRepository.findByConfirmationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEnabled(true);
            user.setConfirmationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
