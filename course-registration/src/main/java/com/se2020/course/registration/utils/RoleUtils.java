package com.se2020.course.registration.utils;


import java.util.ArrayList;
import java.util.List;
public class RoleUtils {
    public static final List<String> ADMIN_PERMISSION =null;

    public static final List<String> STUDENT_PERMISSION = null;
    public static final List<String> GUEST_PERMISSION =null;

    public static List<String> getPermissionFromRole(String role){
        List<String> permissions = new ArrayList<>();
        switch (role){
            case "admin":
                permissions = ADMIN_PERMISSION;
                break;

            case "student":
                permissions = STUDENT_PERMISSION;
                break;
            default:
                permissions = GUEST_PERMISSION;
                break;
        }
        return permissions;
    }
}
