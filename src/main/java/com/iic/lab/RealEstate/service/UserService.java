package com.iic.lab.RealEstate.service;

import com.iic.lab.RealEstate.dto.LoginDto;
import com.iic.lab.RealEstate.dto.UserDto;
import com.iic.lab.RealEstate.exception.ResourceNotFoundException;
import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.repository.RealEstateListingRepository;
import com.iic.lab.RealEstate.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;


@Service
@Validated
public class UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RealEstateListingRepository realEstateListingRepository;


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

    // Return the user based on his session.
    public User getLoggedInUser(HttpSession session){
        return (User) session.getAttribute("user");
    }

    // Deleting the user and all his Real Estate Listings.
    public void deleteUserAndRealEstateListings(Long userId){

        // Retrieve the user from the repository and their Real Estate Listings too.
        User user = userRepository.findByIdWithRealEstateListing(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        // Delete user.
        userRepository.delete(user);
    }

    public List<RealEstateListing> getAllRealEstateListingByUser(Long userId){

        // Finding all Real Estate Listings by their creator id.
        return realEstateListingRepository.findAllByCreatorId(userId);
    }


    // Checking if the user exists in the database.
    public boolean doesUserExist(Long userId){
        return userRepository.existsById(userId);
    }


}
