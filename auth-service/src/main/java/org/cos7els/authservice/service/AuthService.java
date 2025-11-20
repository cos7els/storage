package org.cos7els.authservice.service;

import org.cos7els.authservice.dto.AuthRequest;
import org.cos7els.authservice.dto.AuthResponse;
import org.cos7els.authservice.dto.RegisterRequest;
import org.cos7els.authservice.model.Token;
import org.cos7els.authservice.model.TokenType;
import org.cos7els.authservice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url:http://localhost:8081}")
    private String userServiceUrl;

    public AuthResponse register(RegisterRequest registerRequest) {
        // Отправляем запрос на user-service для создания пользователя
        String url = userServiceUrl + "/api/users";
        var response = restTemplate.postForObject(url, registerRequest, Object.class);

        // Генерируем токены
        String accessToken = generateToken();
        String refreshToken = generateToken();

        // Сохраняем токены (предполагаем, что userId можно извлечь из ответа)
        // В реальном приложении нужно извлекать userId из ответа от user-service
        Long userId = 1L; // Заглушка - в реальном приложении нужно извлекать из ответа

        Token access = new Token();
        access.setToken(accessToken);
        access.setUserId(userId);
        access.setTokenType(TokenType.BEARER);
        access.setRevoked(false);
        access.setExpired(false);
        access.setExpiresAt(LocalDateTime.now().plusHours(1)); // 1 час
        tokenRepository.save(access);

        Token refresh = new Token();
        refresh.setToken(refreshToken);
        refresh.setUserId(userId);
        refresh.setTokenType(TokenType.REFRESH);
        refresh.setRevoked(false);
        refresh.setExpired(false);
        refresh.setExpiresAt(LocalDateTime.now().plusDays(7)); // 7 дней
        tokenRepository.save(refresh);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setMessage("User registered successfully");
        return authResponse;
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        // Отправляем запрос на user-service для проверки аутентификации
        // В реальном приложении нужно реализовать проверку учетных данных
        String url = userServiceUrl + "/api/users/username/" + authRequest.getUsername();
        try {
            var response = restTemplate.getForObject(url, Object.class);
            // Если пользователь существует, генерируем токены
            String accessToken = generateToken();
            String refreshToken = generateToken();

            // Заглушка - в реальном приложении нужно извлекать userId из ответа
            Long userId = 1L;

            Token access = new Token();
            access.setToken(accessToken);
            access.setUserId(userId);
            access.setTokenType(TokenType.BEARER);
            access.setRevoked(false);
            access.setExpired(false);
            access.setExpiresAt(LocalDateTime.now().plusHours(1)); // 1 час
            tokenRepository.save(access);

            Token refresh = new Token();
            refresh.setToken(refreshToken);
            refresh.setUserId(userId);
            refresh.setTokenType(TokenType.REFRESH);
            refresh.setRevoked(false);
            refresh.setExpired(false);
            refresh.setExpiresAt(LocalDateTime.now().plusDays(7)); // 7 дней
            tokenRepository.save(refresh);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(accessToken);
            authResponse.setRefreshToken(refreshToken);
            authResponse.setMessage("Authentication successful");
            return authResponse;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed");
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}