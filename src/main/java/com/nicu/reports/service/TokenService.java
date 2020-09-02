package com.nicu.reports.service;

import static java.lang.String.format;
import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nicu.reports.api.UserCredentials;
import com.nicu.reports.dao.UserDao;
import com.nicu.reports.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {

    private static final String SEPARATOR = ";";

    private final int tokenValidityMinutes;

    private final UserDao userDao;

    public TokenService(@Value("${security.token.validity.minutes:30}") int tokenValidityMinutes,
                        UserDao userDao) {
        this.tokenValidityMinutes = tokenValidityMinutes;
        this.userDao = userDao;
    }

    String computeToken(UserCredentials userCredentials) {
        return this.computeToken(userCredentials.getUsername(), userCredentials.getPassword());
    }

    String computeToken(String username, String password) {
        TokenComponents tokenComponents = new TokenComponents(username, password, now());
        return encodeBase64(tokenComponents.toString());
    }

    String encodeBase64(String input) {
        return Base64.getEncoder().encodeToString(input.getBytes());
    }

    public boolean isValidAuthorizationToken(String token) {
        TokenComponents tokenComponents = decodeToken(token);
        User user = findUserFromToken(tokenComponents);
        return user != null;
    }

    public Long getUserId(String token) {
        TokenComponents tokenComponents = decodeToken(token);
        User user = findUserFromToken(tokenComponents);
        return user != null ? user.getId() : null;
    }

    private TokenComponents decodeToken(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        try {
            String[] parts = decodedToken.split(SEPARATOR);
            return new TokenComponents(parts[0], parts[1], LocalDateTime.parse(parts[2]));
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("Could not extract data from token");
            return null;
        } catch (DateTimeParseException e) {
            log.error("Could not parse token validity period");
            return null;
        }
    }

    private User findUserFromToken(TokenComponents tokenComponents) {
        return Optional.ofNullable(tokenComponents)
            .filter(components -> isValidDateToken(components.getDateToken()))
            .map(components -> userDao.findByUsername(components.getUsername()))
            .orElse(null);
    }

    private boolean isValidDateToken(LocalDateTime dateTimeToken) {
        return now().isBefore(dateTimeToken.plusMinutes(tokenValidityMinutes));
    }

    @Getter
    @AllArgsConstructor
    private class TokenComponents {

        private String username;

        private String password;

        private LocalDateTime dateToken;

        @Override
        public String toString() {
            return username + SEPARATOR + password + SEPARATOR + dateToken;
        }
    }
}
