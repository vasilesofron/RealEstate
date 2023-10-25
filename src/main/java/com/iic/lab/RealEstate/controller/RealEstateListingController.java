package com.iic.lab.RealEstate.controller;

import com.iic.lab.RealEstate.dto.RealEstateListingDto;
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
}
