package com.se2020.course.registration.unit;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.se2020.course.registration.utils.SecurityUtils;

public class UtilsTest {
    
    @Test
    public void testMD5Hash(){
        final String password = "Goodpass11";
        final String hashCodeExpected = "e7a549ef63b71db79567e0d682dd1f47";

        assertThat(SecurityUtils.hashPassword(password)).isEqualTo(hashCodeExpected.toUpperCase());
    }
}
