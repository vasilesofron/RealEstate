package com.iic.lab.RealEstate.controller;

import com.iic.lab.RealEstate.dto.ChangeEmailRequestDto;
import com.iic.lab.RealEstate.dto.LoginDto;
import com.iic.lab.RealEstate.dto.UserDto;
import com.iic.lab.RealEstate.dto.UserProfileUpdateDto;
import com.iic.lab.RealEstate.exception.BadRequestException;
import com.iic.lab.RealEstate.exception.RealEstateListingNotInFavouritesException;
import com.iic.lab.RealEstate.exception.ResourceNotFoundException;
import com.iic.lab.RealEstate.exception.UnauthorizedException;
import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.service.RealEstateListingService;
import com.iic.lab.RealEstate.service.UserService;
import com.sun.net.httpserver.HttpsServer;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@Validated
@RequestMapping("/real-estate-api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RealEstateListingService realEstateListingService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint for user registration.
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDto userDto){

        // Checking if the email is already registered.
        if(userService.existsByEmail(userDto.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already registered.");
        }

        // We create a new user entity based on our DTO.
        User registeredUser = userService.registerUser(userDto);

        // We save the user entity to our repository. (All the validation has been done in the DTO file).
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. UserID: "+ registeredUser.getId());
    }

    // Endpoint to log n the user.
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginDto loginDto, HttpSession session){

        // Create a new user entity.
        User loggedInUser = userService.loginUser(loginDto);

        // If there is a user to be logged in, we give him a session.
        if(loggedInUser != null){
            session.setAttribute("user", loggedInUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Logged in.");
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }
    }

    // Log Out endpoint for the user.
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session){
        // We invalidate the session.
        session.invalidate();
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully.");
    }

    // Endpoint for the user's profile.
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpSession session){

        // We get the user based on the session.
        User loggedInUser = userService.getLoggedInUser(session);

        // If the user is logged in, we display his information.
        if (loggedInUser != null){
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
    }

    @DeleteMapping("/delete-account/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, HttpSession session){

        //We get the user details.
        User authenticatedUser = (User) session.getAttribute("user");

        // We check that the user is logged in AND if the logged-in user is not trying to delete somebody else's account. (We check if the logged-in user ID is the same with the requested ID to be deleted).
        if(authenticatedUser == null || !authenticatedUser.getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        }

        // Accessing the service method.
        try {
            userService.deleteUserAndRealEstateListings(userId);
            session.invalidate();
            return ResponseEntity.ok("User account and their Real Estate Listings have been deleted.");
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    // Getting all the Real Estate Listings created by user.
    @GetMapping("/listings/{userId}")
    public ResponseEntity<?> getUserListings(@PathVariable Long userId){

        //Checking if the user exists.
        boolean userExists = userService.doesUserExist(userId);

        if(!userExists){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("There is no user with such ID.");
        }

        // Getting all the listings.
        List<RealEstateListing> userRealEstateListings = userService.getAllRealEstateListingByUser(userId);

        // If the user exists, but there are no Real Estate Listings, display a message.
        if(userRealEstateListings.isEmpty()){
            return ResponseEntity.ok("User does not have any listings.");
        }

        // Returning all the Real Estate Listings.
        return ResponseEntity.ok(userRealEstateListings);
    }

    @GetMapping("/{userId}/favourites")
    public ResponseEntity<?> getUserFavouriteRealEstateListings(HttpSession session, @PathVariable Long userId){

        // Checking if the user is logged-in.
        User authenticatedUser = (User) session.getAttribute("user");
        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        // Checking if the user tries to access his own favourites or somebody else's.
        if(!authenticatedUser.getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        // Checking if the favourite Real Estate Listing is empty.
        if(!userService.hasFavouriteRealEstateListings(authenticatedUser)){
            return ResponseEntity.ok("There are no Real Estate Listings added to favourite.");
        }

        // Getting the list of favourite Real Estate listing.
        List<RealEstateListing> favouriteRealEstateListings = userService.getUserFavouriteRealEstateListing(authenticatedUser);

        // Returning the list.
        return ResponseEntity.ok(favouriteRealEstateListings);
    }

    @DeleteMapping("/{userId}/favourites/remove/{favouriteRealEstateListingId}")
    public ResponseEntity<?> removeRealEstateListingFromUserFavourite(@PathVariable Long favouriteRealEstateListingId, @PathVariable Long userId, HttpSession session){

        User authenticatedUser = (User) session.getAttribute("user");

        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        if(!authenticatedUser.getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        RealEstateListing favouriteRealEstateListing = realEstateListingService.getRealEstateListingById(favouriteRealEstateListingId);

        if(favouriteRealEstateListing == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Real Estate Listing is not added to favourites.");
        }

        try{
            userService.removeRealEstateListingFromUserFavourites(authenticatedUser, favouriteRealEstateListing);

            return ResponseEntity.status(HttpStatus.OK).body("Listing removed from favourites.");
        } catch (RealEstateListingNotInFavouritesException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favourite listing not found.");
        }
    }

    @PatchMapping("/{userId}/update-profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody @Valid UserProfileUpdateDto updatedUserDto, HttpSession session, @PathVariable Long userId){

        User authenticatedUser = (User) session.getAttribute("user");

        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        if(!authenticatedUser.getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        User updatedUser = userService.updateUserProfile(authenticatedUser, updatedUserDto);

        return ResponseEntity.status(HttpStatus.OK).body("Profile updated!");
    }

    @PutMapping("/{userId}/change-email")
    public ResponseEntity<?> changeUserEmail(@RequestBody @Valid ChangeEmailRequestDto changeEmailRequestDto, HttpSession session, @PathVariable Long userId){

        User authenticatedUser = (User) session.getAttribute("user");

        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        if(!authenticatedUser.getId().equals(userId)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }

        /*if(!authenticatedUser.getEmail().equals(changeEmailRequestDto.getCurrentEmail()) || !passwordEncoder.matches(changeEmailRequestDto.getPassword(), authenticatedUser.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect current email or password.");
        }
        */

        try {
            User updatedUser = userService.changeUserEmail(authenticatedUser, changeEmailRequestDto.getNewEmail(), changeEmailRequestDto.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body("Email changed successfully.");
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access.");
        } catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New email must be different from old email.");
        }




    }



}
