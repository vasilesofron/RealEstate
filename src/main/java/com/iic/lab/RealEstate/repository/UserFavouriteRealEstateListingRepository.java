package com.iic.lab.RealEstate.repository;

import com.iic.lab.RealEstate.model.RealEstateListing;
import com.iic.lab.RealEstate.model.User;
import com.iic.lab.RealEstate.model.UserFavouriteRealEstateListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavouriteRealEstateListingRepository extends JpaRepository<UserFavouriteRealEstateListing, Long> {
    // Custom query to find favourite Real Estate Listing by user.
    List<UserFavouriteRealEstateListing> findByUser(User user);

    // Custom query to find the user's favourite Real Estate Listings.
    boolean existsByUserAndRealEstateListing(User user, RealEstateListing realEstateListing);

    // Custom query to delete Real Estate Listing from user's favourites.
    void deleteByUserAndRealEstateListing(User user, RealEstateListing realEstateListing);
}
