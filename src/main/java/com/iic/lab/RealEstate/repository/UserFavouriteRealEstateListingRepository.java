package com.iic.lab.RealEstate.repository;

import com.iic.lab.RealEstate.model.UserFavouriteRealEstateListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavouriteRealEstateListingRepository extends JpaRepository<UserFavouriteRealEstateListing, Long> {
}
