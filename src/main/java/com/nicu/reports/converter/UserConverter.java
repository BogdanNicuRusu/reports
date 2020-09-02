package com.nicu.reports.converter;

import com.nicu.reports.dto.UserDTO;
import com.nicu.reports.model.User;

public class UserConverter {

    public static UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(user.getUsername(), user.getToken());
    }
}
