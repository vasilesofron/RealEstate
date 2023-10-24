package com.iic.lab.RealEstate.service;

import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserRegistration(){
        // Arrange

        User user = User.builder()
                .firstName("Vasile")
                .lastName("Sofron")
                .dateOfBirth(LocalDate.of(2001, 4, 4))
                .email("vasilesofron1@gmail.com")
                .password("Mypassword1!")
                .build();

        // Act

        User savedUser  = userRepository.save(user);

        // Assert

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);


    }


}
