package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryListResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CategoryItemEntity;
import com.upgrad.FoodOrderingApp.service.entity.ItemEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/")
public class CategoryController {

    @Autowired
    private RestaurantService restaurantService;
    @RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE,path="/category")
    public ResponseEntity<CategoriesListResponse> getAllCategories(){
        CategoriesListResponse response = new CategoriesListResponse();
        List<CategoryEntity> categoryEntityList = restaurantService.getAllCategories();
        List<CategoryListResponse> responseList = new ArrayList<>();
        //Loop through the category list fetched via the service and convert them accroding to the response
    for(CategoryEntity c : categoryEntityList){
        CategoryListResponse item = new CategoryListResponse();
        item.setCategoryName(c.getCategoryName());
        item.setId(UUID.fromString(c.getUuid()));
        responseList.add(item);
        }
        response.setCategories(responseList);
        return new ResponseEntity<CategoriesListResponse>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE,path="/category/{categoryuuid}")
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@PathVariable("categoryuuid") final String uuid) throws CategoryNotFoundException {
       //Check if the category feild is empty
        if(uuid==null||uuid.equals("")){
            throw new CategoryNotFoundException("CNF-001","Category field should not be empty");
        }

        //Create a new response
        CategoryDetailsResponse response = new CategoryDetailsResponse();
//        System.out.print(uuid);
        List<ItemList> responseList = new ArrayList<>();

        //Fetch the category from the service
        CategoryEntity category = restaurantService.getCategoryViaUuid(uuid);

        //Throw error if the cateogry is not present
        if(category==null){
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }

        //Get the category item from the service
        List<CategoryItemEntity> categoryItemEntities = restaurantService.getCategoryItemViaCategoryId(category.getId());

        // Loop through the category items
        for(CategoryItemEntity e : categoryItemEntities){
            //Fetch the item entity using the category item id
            ItemEntity itemEntity = restaurantService.getItemViaId(e.getItemId());
            ItemList item = new ItemList();
            ItemList.ItemTypeEnum type;

            //Set the item type
            if(Integer.parseInt(itemEntity.getType())==1){
                type = ItemList.ItemTypeEnum.NON_VEG;
            }else{
                type = ItemList.ItemTypeEnum.VEG;
            }
            item.setItemType(type);
            item.setPrice(itemEntity.getPrice());
            item.setId(UUID.fromString(itemEntity.getUuid()));
            item.setItemName(itemEntity.getItemName());
            responseList.add(item);
        }
        response.setCategoryName(category.getCategoryName());
        response.setId(UUID.fromString(category.getUuid()));
        response.setItemList(responseList);
        return new ResponseEntity<CategoryDetailsResponse>(response,HttpStatus.OK);

    }

}
