package com.se2020.course.registration.utils;

import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.enums.RolesEnum;
import com.se2020.course.registration.repository.UserRepository;

import java.util.List;

public class PermissionUtils {
    public static boolean hasPermission(String email, String password, UserRepository userRepository) {
        String hashed = SecurityUtils.hashPassword(password);
        List<User> requesters = userRepository.findByEmailAndPassword(email, hashed);
        if (requesters.size() > 0) {
            User requester = requesters.get(0);
            String role = requester.getRole().toUpperCase();
            return RolesEnum.valueOf(role).equals(RolesEnum.ADMIN);
        }
        return false;
    }
}
