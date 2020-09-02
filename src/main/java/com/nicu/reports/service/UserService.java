package com.nicu.reports.service;

import static com.nicu.reports.converter.UserConverter.toUserDTO;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.nicu.reports.api.UserCredentials;
import com.nicu.reports.dao.UserDao;
import com.nicu.reports.dto.UserDTO;
import com.nicu.reports.exception.DuplicatedNameException;
import com.nicu.reports.exception.InvalidCredentialsException;
import com.nicu.reports.exception.NotFoundEntityException;
import com.nicu.reports.model.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private static final String ALREADY_EXISTING_USERNAME = "Already existing username";

    private static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";

    private final UserDao userDao;

    private final TokenService tokenService;

    public UserDTO register(UserCredentials userCredentials) {
        if (userDao.countByUsername(userCredentials.getUsername()) > 0) {
            log.error("Username {} is already existing", userCredentials.getPassword());
            throw new DuplicatedNameException(ALREADY_EXISTING_USERNAME);
        }
        userDao.save(new User(userCredentials.getUsername(), tokenService.encodeBase64(userCredentials.getPassword()), tokenService.computeToken(userCredentials)));
        return toUserDTO(findByUsername(userCredentials.getUsername()));
    }

    public UserDTO login(UserCredentials userCredentials) {
        try {
            User user = findByUsername(userCredentials.getUsername());
            if (!passwordMatches(userCredentials.getPassword(), user.getPassword())) {
                log.error("Password not matching for user {}", userCredentials.getUsername());
                throw new InvalidCredentialsException(INVALID_USERNAME_OR_PASSWORD);
            }
            updateUserToken(user);
            return toUserDTO(user);
        } catch (EmptyResultDataAccessException e) {
            log.error("User with name {} is not existing", userCredentials.getUsername());
            throw new InvalidCredentialsException(INVALID_USERNAME_OR_PASSWORD);
        }
    }

    private User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    private boolean passwordMatches(String password, String existingPassword) {
        return existingPassword.equals(tokenService.encodeBase64(password));
    }

    private void updateUserToken(User user) {
        user.setToken(tokenService.computeToken(user.getUsername(), user.getPassword()));
        userDao.update(user);
    }
}
