package com.iic.lab.RealEstate.controller;

import com.iic.lab.RealEstate.dto.RealEstateListingDto;
import com.iic.lab.RealEstate.exception.RealEstateListingAlreadyInFavouritesException;
import com.iic.lab.RealEstate.exception.ResourceNotFoundException;
import com.iic.lab.RealEstate.exception.UnauthorizedException;
import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.service.RealEstateListingService;
import com.iic.lab.RealEstate.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/real-estate-api/real-estate-listing")
public class RealEstateListingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RealEstateListingService realEstateListingService;


    // Create Real Estate Listing
    @PostMapping("/create")
    public ResponseEntity<?> createRealEstateListing(@RequestBody @Valid RealEstateListingDto realEstateListingDto, HttpSession session){

        // Get the User information based on the current session.
        User authenticatedUser = (User) session.getAttribute("user");

        // Checking if the user is logged in.
        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        // Create the Real Estate Listing with the DTO and the AuthenticatedUser.
        RealEstateListing createdListing = realEstateListingService.createRealEstateListing(realEstateListingDto, authenticatedUser);

        // Returning a message based on the value of createdListing.
        if(createdListing != null){
            return ResponseEntity.status(HttpStatus.CREATED).body("Real estate listing created.");
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create real estate listing.");
        }
    }

    // Edit Real Estate Listing
    @PutMapping("/update/{realEstateListingId}")
    public ResponseEntity<?> updateRealEstateListing(@PathVariable Long realEstateListingId, @RequestBody @Valid RealEstateListingDto updatedRealEstateListingDto, HttpSession session){

        // Get the User details based on the session.
        User authenticatedUser = (User) session.getAttribute("user");

        // Checking if the user is logged in.
        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        // Updating the Real Estate Listing
        try {
            // Checking if the user who tries to modify the Real Estate Listing is the one who created it.
            if(!realEstateListingService.isUserOwnerOfRealEstateListinig(realEstateListingId, authenticatedUser.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to modify this listing.");
            }
            RealEstateListing updatedRealEstateListing = realEstateListingService.updateRealEstateListing(realEstateListingId, updatedRealEstateListingDto, authenticatedUser);
            return ResponseEntity.ok("Real estate listing updated successfully.");
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Real estate listing not found.");
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to edit this listing.");
        }
    }

    // Deleting a Real Estate Listing
    @DeleteMapping("/delete/{realEstateListingId}")
    public ResponseEntity<?> deleteRealEstateListing(@PathVariable Long realEstateListingId, HttpSession session){

        // Get the user details based on the session.
        User authenticatedUser = (User) session.getAttribute("user");

        // Check if the user is logged in.
        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }

        // Delete the Real Estate Listing.
        try {
            // Checking if the user who tries to delete the Real Estate Listing is the one who created it.
            if(!realEstateListingService.isUserOwnerOfRealEstateListinig(realEstateListingId, authenticatedUser.getId())){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have access to delete this listing.");
            }
            realEstateListingService.deleteRealEstateListing(realEstateListingId, authenticatedUser);
            return ResponseEntity.ok("Real Estate Listing delete successfully.");

        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Real Estate listing does not exist.");
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to delete this listing.");
        }
    }

    // Returning all the Real Estate Listings available.
    @GetMapping("/all-real-estate-listings")
    public ResponseEntity<?> getAllRealEstateListings(){

        // Getting all the Real Estate Listings from the database.
        List<RealEstateListing> allRealEstateListings = realEstateListingService.getAllRealEstateListings();

        // If there are no Real Estate Listings, display a message.
        if(allRealEstateListings.isEmpty()){
            return ResponseEntity.ok("There are no Real Estate Listings at the moment.");
        }

        // Returning all the Real Estate Listings.
        return ResponseEntity.ok(allRealEstateListings);
    }

    // Getting a specific Real Estate Listing.
    @GetMapping("/{realEstateListingId}")
    public ResponseEntity<?> getListingById(@PathVariable Long realEstateListingId){

        //Getting the Real Estate Listing.
        RealEstateListing realEstateListing = realEstateListingService.getRealEstateListingById(realEstateListingId);

        // If the Real Estate Listing does not exist, return a message.
        if(realEstateListing == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Real Estate listing does not exist.");
        }

        // Returning the Real Estate Listing.
        return ResponseEntity.ok(realEstateListing);
    }

    // Adding a Real Estate Listing to Favourites. The user must be logged-in to add it to Favourites.
    @PostMapping("/{realEstateListingId}/favourite")
    public ResponseEntity<?> saveRealEstateListingToUserFavourite(@PathVariable Long realEstateListingId, HttpSession session){

        // Checking if the User is logged in.
        User authenticatedUser = (User) session.getAttribute("user");

        // Displaying a message if the user is not logged-in.
        if(authenticatedUser == null ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in or unauthorized.");
        }

        // Getting the Real Estate Listing from the repository.
        RealEstateListing listing = realEstateListingService.getRealEstateListingById(realEstateListingId);

        // Displaying a message if the Real Estate Listing does not exist.
        if(listing == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Real Estate Listing not found.");
        }

        // TRY - CATCH
        try {
            // Adding the Real Estate listing to the database.
            userService.saveRealEstateListingToUserFavourites(authenticatedUser, listing);

            // Returning a message.
            return ResponseEntity.status(HttpStatus.CREATED).body("Listing saved to favourites.");

        } catch (RealEstateListingAlreadyInFavouritesException e){ // CUSTOM ERROR
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Real Estate Listing already added to favourites.");
        }
    }





}
