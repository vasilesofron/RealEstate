package com.iic.lab.RealEstate.service;

import com.iic.lab.RealEstate.dto.LoginDto;
import com.iic.lab.RealEstate.dto.UserDto;
import com.iic.lab.RealEstate.dto.UserProfileUpdateDto;
import com.iic.lab.RealEstate.exception.*;
import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.model.UserFavouriteRealEstateListing;
import com.iic.lab.RealEstate.repository.RealEstateListingRepository;
import com.iic.lab.RealEstate.repository.UserFavouriteRealEstateListingRepository;
import com.iic.lab.RealEstate.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
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

    @Resource
    private UserFavouriteRealEstateListingRepository userFavouriteRealEstateListingRepository;


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

    public void saveRealEstateListingToUserFavourites(User user, RealEstateListing realEstateListing){

        boolean isRealEstateListingInFavourites = userFavouriteRealEstateListingRepository.existsByUserAndRealEstateListing(user, realEstateListing);

        if(!isRealEstateListingInFavourites) {

            // Creating the Favourite Real Estate Listing object.
            UserFavouriteRealEstateListing userFavouriteRealEstateListing = new UserFavouriteRealEstateListing();

            // Setting the User and the Real Estate Listing.
            userFavouriteRealEstateListing.setUser(user);
            userFavouriteRealEstateListing.setRealEstateListing(realEstateListing);

            // Saving the Real Estate Listing to favourite.
            userFavouriteRealEstateListingRepository.save(userFavouriteRealEstateListing);
        }
        else throw new RealEstateListingAlreadyInFavouritesException("This Real Estate Listing is already added to favourites.");
    }

    // Getting a User by the id.
    public User getUserById(Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    // Getting all the user's Real Estate Listings from favourite.
    public List<RealEstateListing> getUserFavouriteRealEstateListing(User user){

        // Finding the favourite listings based on the user.
        List<UserFavouriteRealEstateListing> userFavouriteRealEstateListings = userFavouriteRealEstateListingRepository.findByUser(user);

        // Storing the Real Estate Listings in this new ArrayList.
        List<RealEstateListing> favouriteRealEstateListings = new ArrayList<>();

        // Iterating through all the Real Estate listings and adding them to the ArrayList.
        for(UserFavouriteRealEstateListing userFavouriteRealEstateListing : userFavouriteRealEstateListings){
            favouriteRealEstateListings.add(userFavouriteRealEstateListing.getRealEstateListing());
        }

        // Returning the array list.
        return favouriteRealEstateListings;
    }

    // Checking if the user has Real Estate listings added to favourite.
    public boolean hasFavouriteRealEstateListings(User user){
        return !userFavouriteRealEstateListingRepository.findByUser(user).isEmpty();
    }

    @Transactional
    public void removeRealEstateListingFromUserFavourites(User user, RealEstateListing favouriteRealEstateListing){

        // Checking if the Real Estate Listing exists in the user's favourites.
        boolean isRealEstateListingInFavourites = userFavouriteRealEstateListingRepository.existsByUserAndRealEstateListing(user, favouriteRealEstateListing);

        if(isRealEstateListingInFavourites){
            userFavouriteRealEstateListingRepository.deleteByUserAndRealEstateListing(user, favouriteRealEstateListing);
        }
        else {
            throw new RealEstateListingNotInFavouritesException("Real Estate Listing is not a favourite.");
        }
    }

    // Checking if the user has the email in the database.
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User updateUserProfile(User authenticatedUser, UserProfileUpdateDto updatedUser){

        // Updating the old user with the new information.
        if(updatedUser.getFirstName() != null)
            authenticatedUser.setFirstName(updatedUser.getFirstName());
        if(updatedUser.getLastName()!= null)
            authenticatedUser.setLastName(updatedUser.getLastName());
        if(updatedUser.getDateOfBirth() != null)
            authenticatedUser.setDateOfBirth(updatedUser.getDateOfBirth());
        if(updatedUser.getProfilePhotoUrl() != null)
            authenticatedUser.setProfilePhotoUrl(updatedUser.getProfilePhotoUrl());
        if(updatedUser.getDescription() != null)
            authenticatedUser.setDescription(updatedUser.getDescription());

        // Saving the user to repository.
        return userRepository.save(authenticatedUser);
    }

    public User changeUserEmail(User user, String newEmail, String password){
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new UnauthorizedException("Incorrect password. Email change failed.");
        }

        if(user.getEmail().equalsIgnoreCase(newEmail)){
            throw new BadRequestException("New email must be different from the old email.");
        }

        user.setEmail(newEmail);

        return userRepository.save(user);

    }






}
