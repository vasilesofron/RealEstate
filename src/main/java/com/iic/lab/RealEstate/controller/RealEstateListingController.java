package com.iic.lab.RealEstate.controller;

import com.iic.lab.RealEstate.dto.RealEstateListingDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/real-estate-api/real-estate-listing")
public class RealEstateListingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RealEstateListingService realEstateListingService;


    // MUST WORK HERE
    @PostMapping("/create")
    public ResponseEntity<?> createRealEstateListing(@RequestBody @Valid RealEstateListingDto realEstateListingDto, HttpSession session){

        User authenticatedUser = (User) session.getAttribute("user");

        if(authenticatedUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }


        RealEstateListing createdListing = realEstateListingService.createRealEstateListing(realEstateListingDto, authenticatedUser);

        if(createdListing != null){
            return ResponseEntity.status(HttpStatus.CREATED).body("Real estate listing created.");
        } else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create real estate listing.");
        }
    }
}
