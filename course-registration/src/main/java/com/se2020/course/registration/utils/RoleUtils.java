package com.se2020.course.registration.utils;


import com.se2020.course.registration.enums.PermissionsEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RoleUtils {
    public static final List<PermissionsEnum> ADMIN_PERMISSION = new ArrayList<>(Arrays.asList(
            PermissionsEnum.LOG_IN, PermissionsEnum.CHANGE_PASSWORD, PermissionsEnum.MODIFY_PROFILE,
            PermissionsEnum.ADD_USER, PermissionsEnum.MODIFY_USER, PermissionsEnum.DELETE_USER, PermissionsEnum.GET_USER,
            PermissionsEnum.ADD_COURSE, PermissionsEnum.MODIFY_COURSE, PermissionsEnum.DELETE_COURSE,
            PermissionsEnum.ADD_STUDENT_COURSE, PermissionsEnum.DELETE_STUDENT_COURSE,
            PermissionsEnum.MODIFY_STUDENT_COURSE, PermissionsEnum.GET_STUDENT_COURSE));

    public static final List<PermissionsEnum> STUDENT_PERMISSION = new ArrayList<>(Arrays.asList(
            PermissionsEnum.LOG_IN,PermissionsEnum.CHANGE_PASSWORD,PermissionsEnum.MODIFY_PROFILE,
            PermissionsEnum.REGISTER_COURSE, PermissionsEnum.CANCEL_COURSE, PermissionsEnum.GET_COURSE,
            PermissionsEnum.GET_MY_COURSE));

    public static final List<PermissionsEnum> GUEST_PERMISSION = new ArrayList<>(
            Collections.singletonList(PermissionsEnum.GET_COURSE));

    public static List<PermissionsEnum> getPermissionFromRole(String role){
        List<PermissionsEnum> permissions;
        switch (role){
            case "ADMIN":
                permissions = ADMIN_PERMISSION;
                break;

            case "STUDENT":
                permissions = STUDENT_PERMISSION;
                break;

            default:
                permissions = GUEST_PERMISSION;
                break;
        }
        return permissions;
    }
}
