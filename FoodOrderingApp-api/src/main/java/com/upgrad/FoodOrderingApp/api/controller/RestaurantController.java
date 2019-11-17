package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private StateService stateService;

    @RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE,path="/restaurant/name/{restaurant_name}")
    public ResponseEntity<RestaurantListResponse> getRestaurantByName(@PathVariable("restaurant_name") String restaurantName ) throws RestaurantNotFoundException {
        if(restaurantName.equals("")){
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }

        //Get all the restaurants
        List<RestaurantEntity> restaurantList = restaurantService.geRestaurantViaName(restaurantName);

        //Response in form of Restaurant List
        List<RestaurantList> restaurants = new ArrayList<>();

        //Iterate over the restaurant list
        for(RestaurantEntity res : restaurantList){

            //Fetch the categories
            List<RestaurantCategoryEntity> categories = restaurantService.getCategoriesViaRestaurantId(res.getId());
            String categoryStirng = "";

            //Iterate over the categories and create a category string
            for(RestaurantCategoryEntity cat: categories){
                CategoryEntity categoryEntity = restaurantService.getCategoryViaId(cat.getCategoryId());
                categoryStirng += (categoryEntity.getCategoryName()+" ");

            }

            //Create a response list item
            RestaurantList listItem = new RestaurantList();

            //Set the fields
            listItem.setCategories(categoryStirng);
            listItem.setRestaurantName(res.getRestaurantName());
            listItem.setAveragePrice(res.getAveragePriceForTwo());
            listItem.setId(UUID.fromString(res.getUuid()));
            listItem.setNumberCustomersRated(res.getNumberOfCustomersRated());

            //create a new Response address
            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();

            //Fetch the full address via the address id
            FullAddressEntity fullAddressOfRestaurant = addressService.getFullAddressViaAddressId(res.getAddressId().toString());

            //Set the address feilds
            address.setCity(fullAddressOfRestaurant.getCity());
            address.setFlatBuildingName(fullAddressOfRestaurant.getFlatBuilNumber());
            address.setId(UUID.fromString(fullAddressOfRestaurant.getUuid()));
            address.setPincode(fullAddressOfRestaurant.getPincode());
            address.setLocality(fullAddressOfRestaurant.getLocality());

            //Fetch the state
            StateEntity getState = stateService.getStateViaId(fullAddressOfRestaurant.getStateId().toString());

            //Create a new object for the state feild in the response
            RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
            state.setId(UUID.fromString(getState.getUuid()));
            state.setStateName(getState.getStateName());

            //Set the state in the address
            address.setState(state);

            //Set the address to the list item
            listItem.setAddress(address);

            //Add the list item to the response list
            restaurants.add(listItem);


        }

        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();
        restaurantListResponse.setRestaurants(restaurants);

        return new ResponseEntity<RestaurantListResponse>(restaurantListResponse, HttpStatus.OK);
    }

}
