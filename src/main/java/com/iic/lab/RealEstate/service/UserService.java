package com.iic.lab.RealEstate.service;

import com.iic.lab.RealEstate.dto.LoginDto;
import com.iic.lab.RealEstate.dto.UserDto;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;


    // Register a user to our API.
    public User registerUser(UserDto userDto){

        // Encoding the user's password.

        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // Receiving the user's information and saving it in the repository.
        User user = new User(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail(), userDto.getDateOfBirth(), encodedPassword);
        return userRepository.save(user);
    }

    // Login the user to the API.
    public User loginUser(LoginDto loginDto){

        // Searching the user in our DataBase via his email.
        User user = userRepository.findByEmail(loginDto.getEmail());

        // If the user exists in the DataBase and has entered the correct password, we log the user in.
        if(user != null && passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            return user;
        }
        else return null;
    }

    public User getLoggedInUser(HttpSession session){
        return (User) session.getAttribute("user");
    }




}
