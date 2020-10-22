package com.se2020.course.registration.utils;


import com.se2020.course.registration.enums.PermissionsEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RoleUtils {
    public static final List<PermissionsEnum> ADMIN_PERMISSION = new ArrayList<>(Arrays.asList(
            PermissionsEnum.ALL_LOG_IN, PermissionsEnum.CHANGE_PASSWORD, 
            PermissionsEnum.ADMIN_ADD_USER, PermissionsEnum.ADMIN_MODIFY_USER_ACCOUNT, PermissionsEnum.ADMIN_DELETE_USER,PermissionsEnum.ADMIN_GET_USER,
            PermissionsEnum.ADMIN_ADD_COURSE, PermissionsEnum.ADMIN_MODIFY_COURSE, PermissionsEnum.ADMIN_DELETE_COURSE, PermissionsEnum.ALL_GET_COURSE,
            PermissionsEnum.ADMIN_EDIT_STUDENT_INFO
            ));

    public static final List<PermissionsEnum> STUDENT_PERMISSION = new ArrayList<>(Arrays.asList(
            PermissionsEnum.ALL_LOG_IN,PermissionsEnum.CHANGE_PASSWORD,
            PermissionsEnum.STUDENT_EDIT_PROFILE,
            PermissionsEnum.STUDENT_REGISTER_COURSE, PermissionsEnum.STUDENT_CANCEL_COURSE, PermissionsEnum.ALL_GET_COURSE,
            PermissionsEnum.STUDENT_GET_MY_COURSE));

    public static final List<PermissionsEnum> GUEST_PERMISSION = new ArrayList<>(
            Collections.singletonList(PermissionsEnum.ALL_GET_COURSE));

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
