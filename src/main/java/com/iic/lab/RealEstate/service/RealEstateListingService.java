package com.iic.lab.RealEstate.service;

import com.iic.lab.RealEstate.dto.RealEstateListingDto;
import com.iic.lab.RealEstate.exception.UnauthorizedException;
import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.repository.RealEstateListingRepository;
import com.iic.lab.RealEstate.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class RealEstateListingService {

    @Resource
    private RealEstateListingRepository realEstateListingRepository;

    @Resource
    private UserRepository userRepository;

    public RealEstateListing createRealEstateListing(RealEstateListingDto realEstateListingDto, User user){

        /*User authenticatedUser = (User) session.getAttribute("user");

        if(authenticatedUser == null){
            throw new UnauthorizedException("User not logged in.");
        }
        */
        // We create the Real Estate Listing, without the creator.
        RealEstateListing realEstateListing = new RealEstateListing(
                realEstateListingDto.getName(),
                realEstateListingDto.getDescription(),
                realEstateListingDto.getAddress(),
                realEstateListingDto.getPrice(),
                realEstateListingDto.getUtilitySize(),
                realEstateListingDto.getTotalSize(),
                realEstateListingDto.getBedroomNumber(),
                realEstateListingDto.getBathroomNumber(),
                realEstateListingDto.getListingType(),
                realEstateListingDto.getConstructionYear()
        );

        // We assign the creator of the Real Estate Listing.
        realEstateListing.setCreator(user);

        // We save the Real Estate Listing to the repository.
        return realEstateListingRepository.save(realEstateListing);
    }

}
