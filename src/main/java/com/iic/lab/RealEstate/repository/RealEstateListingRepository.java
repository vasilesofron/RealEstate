package com.iic.lab.RealEstate.repository;

import com.iic.lab.RealEstate.model.RealEstateListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealEstateListingRepository extends JpaRepository<RealEstateListing, Long> {

    // Customs query to find all the Real Estate Listings by their creator.
    List<RealEstateListing> findAllByCreatorId(Long userId);


}
