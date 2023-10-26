package com.iic.lab.RealEstate.service;

import com.iic.lab.RealEstate.dto.RealEstateListingDto;
import com.iic.lab.RealEstate.exception.RealEstateListingNotInFavouritesException;
import com.iic.lab.RealEstate.exception.ResourceNotFoundException;
import com.iic.lab.RealEstate.exception.UnauthorizedException;
import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.repository.RealEstateListingRepository;
import com.iic.lab.RealEstate.repository.UserFavouriteRealEstateListingRepository;
import com.iic.lab.RealEstate.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class RealEstateListingService {

    @Resource
    private RealEstateListingRepository realEstateListingRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserFavouriteRealEstateListingRepository userFavouriteRealEstateListingRepository;

    public RealEstateListing createRealEstateListing(RealEstateListingDto realEstateListingDto, User user){

        // We create the Real Estate Listing, without the creator.
        RealEstateListing realEstateListing = new RealEstateListing(
                realEstateListingDto.getName(),
                realEstateListingDto.getAddress(),
                realEstateListingDto.getDescription(),
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

    public RealEstateListing updateRealEstateListing(Long realEstateListingId, RealEstateListingDto updatedRealEstateListingDto, User authenticatedUser){

        // Check if the Real Estate Listing exists in the database.
        RealEstateListing existingRealEstateListing = realEstateListingRepository.findById(realEstateListingId)
                .orElseThrow(() -> new ResourceNotFoundException("Real estate listing not found."));

        // Update the Real Estate Listing
        existingRealEstateListing.setName(updatedRealEstateListingDto.getName());
        existingRealEstateListing.setDescription(updatedRealEstateListingDto.getDescription());
        existingRealEstateListing.setAddress(updatedRealEstateListingDto.getAddress());
        existingRealEstateListing.setPrice(updatedRealEstateListingDto.getPrice());
        existingRealEstateListing.setUtilitySize(updatedRealEstateListingDto.getUtilitySize());
        existingRealEstateListing.setTotalSize(updatedRealEstateListingDto.getTotalSize());
        existingRealEstateListing.setBedroomNumber(updatedRealEstateListingDto.getBedroomNumber());
        existingRealEstateListing.setBathroomNumber(updatedRealEstateListingDto.getBathroomNumber());
        existingRealEstateListing.setListingType(updatedRealEstateListingDto.getListingType());
        existingRealEstateListing.setConstructionYear(updatedRealEstateListingDto.getConstructionYear());

        // Save the update in the RealEStateRepository
        return realEstateListingRepository.save(existingRealEstateListing);

    }

    public boolean isUserOwnerOfRealEstateListinig(Long realEstateListingId, Long userId){

        // Checking if the Real Estate Listing exists.
        RealEstateListing existingRealEstateListing = realEstateListingRepository.findById(realEstateListingId)
                .orElseThrow(() -> new ResourceNotFoundException("Real estate listing not found."));

        // Returning TRUE or FALSE if the creator ID is the same with the logged-in User id.
        return existingRealEstateListing.getCreator().getId().equals(userId);
    }



    // Delete a Real Estate Listing
    public void deleteRealEstateListing(Long realEstateListingId, User authenticatedUser){

        // Checking if the Real Estate Listing exists.
        RealEstateListing existingRealEstateListing = realEstateListingRepository.findById(realEstateListingId)
                .orElseThrow(() -> new ResourceNotFoundException("The real estate listing not found."));

        // Deleting the Real Estate Listing
        realEstateListingRepository.delete(existingRealEstateListing);
    }

    // Returning all the Real Estate Listings, does not matter who created them.
    public List<RealEstateListing> getAllRealEstateListings(){
        return realEstateListingRepository.findAll();
    }

    // Getting a Real Estate Listing by the id.
    public RealEstateListing getRealEstateListingById(Long realEstateListingId){
        return realEstateListingRepository.findById(realEstateListingId).orElse(null);
    }

    /*public void removeRealEstateListingFromUserFavourites(User user, RealEstateListing realEstateListing){

        // Checking if the Real Estate Listing exists in the user's favourites.
        boolean isRealEstateListingInFavourites = userFavouriteRealEstateListingRepository.existsByUserAndRealEstateListing(user, realEstateListing);

        if(isRealEstateListingInFavourites){
            userFavouriteRealEstateListingRepository.deleteByUserAndRealEstateListing(user, realEstateListing);
        }
        else {
            throw new RealEstateListingNotInFavouritesException("Real Estate Listing is not a favourite.");
        }
    }
    */


}
