package com.iic.lab.RealEstate.repository;

import com.iic.lab.RealEstate.model.RealEstateListing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealEstateListingRepository extends JpaRepository<RealEstateListing, Long> {
}
