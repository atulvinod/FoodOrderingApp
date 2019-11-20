package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.businness.StateService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
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

    @Autowired
    private AuthenticationService authenticationService;

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
                //Todo: Implement commas seperataion
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

    @RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE,path="/restaurant/category/{category_uuid}")
    public ResponseEntity<RestaurantListResponse> getRestaurantByCategory(@PathVariable("category_uuid") final String categoryUuid) throws CategoryNotFoundException {
        if(categoryUuid.equals("")||categoryUuid==null){
            throw new CategoryNotFoundException("CNF-001","Category id feild should not be empty");
        }
        CategoryEntity category = restaurantService.getCategoryViaUuid(categoryUuid);
        if(category == null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        RestaurantListResponse response = new RestaurantListResponse();
        List<RestaurantList> restaurantListForResponse = new ArrayList<>();

        List<RestaurantCategoryEntity> restaurantsUnderTheCategory = restaurantService.getRestaurantCategoryViaCategoryId(category.getId());


        for(RestaurantCategoryEntity e : restaurantsUnderTheCategory){
            RestaurantEntity restaurant = restaurantService.getRestaurantViaId(e.getRestaurantId());

            RestaurantList item = new RestaurantList();

            item.setId(UUID.fromString(restaurant.getUuid()));
            item.setNumberCustomersRated(restaurant.getNumberOfCustomersRated());
            item.setRestaurantName(restaurant.getRestaurantName());
            item.setPhotoURL(restaurant.getPhotoUrl());
            item.setAveragePrice(restaurant.getAveragePriceForTwo());
            item.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));

            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();

            FullAddressEntity fullAddress = addressService.getFullAddressViaAddressId(restaurant.getAddressId().toString());
            StateEntity state = stateService.getStateViaId(fullAddress.getStateId().toString());
            RestaurantDetailsResponseAddressState addressState = new RestaurantDetailsResponseAddressState();
            addressState.setStateName(state.getStateName());
            addressState.setId(UUID.fromString(state.getUuid()));

            address.setState(addressState);
            address.setLocality(fullAddress.getLocality());
            address.setPincode(fullAddress.getPincode());
            address.setId(UUID.fromString(fullAddress.getUuid()));
            address.setFlatBuildingName(fullAddress.getFlatBuilNumber());

            //Get the categories
            List<RestaurantCategoryEntity> restaurantCategories = restaurantService.getCategoriesViaRestaurantId(e.getRestaurantId());

            //todo: fix the categories comma
            String categories = "";
            for(RestaurantCategoryEntity c : restaurantCategories){
                CategoryEntity ce = restaurantService.getCategoryViaId(c.getCategoryId());
                categories += (ce.getCategoryName()+" ");

            }


            item.setCategories(categories);
            item.setAddress(address);

            restaurantListForResponse.add(item);

        }

        response.setRestaurants(restaurantListForResponse);
        return new ResponseEntity<RestaurantListResponse>(response,HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE,path="/api/restaurant/{restaurant_id}")
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantDetails(@RequestHeader("authorization") String authorization , @PathVariable("restaurant_id")final String restaurantId) throws AuthorizationFailedException, RestaurantNotFoundException {
        CustomerAuthTokenEntity token = authenticationService.getToken(authorization);
        if(token == null){
            throw new AuthorizationFailedException("ATHR-001","Customer not logged in");
        }
        ZonedDateTime now = ZonedDateTime.now();

        //If the user has already logged out, then throw an exception
        if(token.getLogoutAt() != null ){
            if(token.getLogoutAt().isBefore(now))
                throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint");
        }

        //If the current time is greater than the expiry time of the token, then throw an exception
        if(now.isAfter(token.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint");
        }

        if(restaurantId.equals("")||restaurantId==null){
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }

        RestaurantEntity restaurant = restaurantService.getRestaurantViaUuid(restaurantId);

        if(restaurant==null){
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }

        //Create the required response entities
        RestaurantDetailsResponse response = new RestaurantDetailsResponse();
        RestaurantDetailsResponseAddress restaurantAddress = new RestaurantDetailsResponseAddress();
        RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

        //Get the full address of the Restaurant
        FullAddressEntity getRestaurantAddress = addressService.getFullAddressViaAddressId(restaurant.getAddressId().toString());

        //Fetch the state from the state service and set the required feilds for the response
        StateEntity stateEntity = stateService.getStateViaId(getRestaurantAddress.getStateId().toString());
        restaurantDetailsResponseAddressState.setId(UUID.fromString(stateEntity.getUuid()));
        restaurantDetailsResponseAddressState.setStateName(stateEntity.getStateName());

        //Create a category list
        List<CategoryList> categoryList = new ArrayList<>();
        List<RestaurantCategoryEntity> categoryEntities = restaurantService.getCategoriesViaRestaurantId(restaurant.getId());

        //Cycle through the restaurant category list
        for(RestaurantCategoryEntity cat : categoryEntities){

            //get the category entity using the id of the restaurant category
            CategoryEntity categoryEntity = restaurantService.getCategoryViaId(cat.getCategoryId());
            CategoryList listItem = new CategoryList();
            listItem.setId(UUID.fromString(categoryEntity.getUuid()));
            listItem.setCategoryName(categoryEntity.getCategoryName());

            List<CategoryItemEntity> categoryItemEntities = restaurantService.getCategoryItemViaCategoryId(cat.getCategoryId());
            List<ItemList> itemList = new ArrayList<>();

            // Fetch the item entities for each category entity
            for(CategoryItemEntity c:categoryItemEntities){
                ItemList i = new ItemList();
                ItemEntity itemEntity = restaurantService.getItemViaId(c.getItemId());

                i.setId(UUID.fromString(itemEntity.getUuid()));
                i.setItemName(itemEntity.getItemName());
                i.setPrice(itemEntity.getPrice());


                ItemList.ItemTypeEnum type;
                if(Integer.parseInt(itemEntity.getType())==1){
                    type = ItemList.ItemTypeEnum.NON_VEG;
                }else{
                    type= ItemList.ItemTypeEnum.VEG;
                }
                i.setItemType(type);
                itemList.add(i);
            }

            listItem.setItemList(itemList);
            categoryList.add(listItem);

        }

        //Set the restaurant address feilds
        restaurantAddress.setFlatBuildingName(getRestaurantAddress.getFlatBuilNumber());
        restaurantAddress.setId(UUID.fromString(getRestaurantAddress.getUuid()));
        restaurantAddress.setState(restaurantDetailsResponseAddressState);
        restaurantAddress.setPincode(getRestaurantAddress.getPincode());
        restaurantAddress.setLocality(getRestaurantAddress.getLocality());
        restaurantAddress.setCity(getRestaurantAddress.getCity());

        //Set response
        response.setAddress(restaurantAddress);
        response.setAveragePrice(restaurant.getAveragePriceForTwo());
        response.setCustomerRating(BigDecimal.valueOf(restaurant.getCustomerRating()));
        response.setId(UUID.fromString(restaurant.getUuid()));
        response.setNumberCustomersRated(restaurant.getNumberOfCustomersRated());
        response.setPhotoURL(restaurant.getPhotoUrl());
        response.setRestaurantName(restaurant.getRestaurantName());
        response.setCategories(categoryList);

        return new ResponseEntity<RestaurantDetailsResponse>(response,HttpStatus.OK);

    }

}
