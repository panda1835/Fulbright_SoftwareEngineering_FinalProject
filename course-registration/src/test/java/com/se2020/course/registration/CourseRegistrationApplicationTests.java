package com.se2020.course.registration;

import com.se2020.course.registration.entity.Student;
import com.se2020.course.registration.entity.User;
import com.se2020.course.registration.repository.StudentRepository;
import com.se2020.course.registration.repository.UserRepository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CourseRegistrationApplicationTests {

	final String TEST_EMAIL = "test@email.com";
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	UserRepository userRepository;

	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
		assertThat(studentRepository).isNotNull();
	}

	@Test
	@Order(1) // ensure this one is run before the below test
	void testSaveUser(){
		final User user = User.builder()
					.email(TEST_EMAIL)
					.password("1234")
					.build();
		User user1 = userRepository.save(user);
		assertThat(user1).isNotNull();
	}

	@Test
	@Order(2)
	void findByEmai(){
		User user = userRepository.findByEmail(TEST_EMAIL).orElse(null);
		assertThat(user).isNotNull();
	}

	@Test
	@Order(3)
	void testUserLogin(){
		final User loginUser = User.builder()
					.email(TEST_EMAIL)
					.password("1234")
					.build();

		User loginResult = restTemplate.postForObject("http://localhost:8080/login", loginUser, User.class);
		assertThat(loginResult).extracting(User::getEmail).isEqualTo(TEST_EMAIL);
	}
}
