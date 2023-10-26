package com.iic.lab.RealEstate.exception;

import com.iic.lab.RealEstate.model.RealEstateListing;

public class RealEstateListingAlreadyInFavouritesException extends RuntimeException{

    // Custom Error message.
    public RealEstateListingAlreadyInFavouritesException(String message){
        super(message);
    }
}
