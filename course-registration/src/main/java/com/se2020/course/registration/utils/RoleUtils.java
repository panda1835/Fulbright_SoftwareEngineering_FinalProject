package com.se2020.course.registration.utils;


import com.se2020.course.registration.enums.PermissionsEnum;
import com.se2020.course.registration.enums.RolesEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.se2020.course.registration.enums.RolesEnum.ADMIN;
import static com.se2020.course.registration.enums.RolesEnum.STUDENT;

public class RoleUtils {
    public static final List<PermissionsEnum> ADMIN_PERMISSION = new ArrayList<>(
            Arrays.asList(PermissionsEnum.values()));

    public static final List<PermissionsEnum> STUDENT_PERMISSION = new ArrayList<>(Arrays.asList(
            PermissionsEnum.LOG_IN,PermissionsEnum.CHANGE_PASSWORD,PermissionsEnum.MODIFY_PROFILE,
            PermissionsEnum.REGISTER_COURSE, PermissionsEnum.CANCEL_COURSE, PermissionsEnum.GET_COURSE,
            PermissionsEnum.GET_MY_COURSE));

    public static final List<PermissionsEnum> GUEST_PERMISSION = new ArrayList<>(
            Collections.singletonList(PermissionsEnum.GET_COURSE));

    public static List<PermissionsEnum> getPermissionFromRole(RolesEnum role){
        List<PermissionsEnum> permissions = null;
        switch (role){
            case ADMIN:
                permissions = ADMIN_PERMISSION;
                break;

            case STUDENT:
                permissions = STUDENT_PERMISSION;
                break;

            case GUEST:
                permissions = GUEST_PERMISSION;
                break;
        }
        return permissions;
    }
}
