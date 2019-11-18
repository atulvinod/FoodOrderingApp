package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private CategoryItemDao categoryItemDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private RestaurantCategoryDao restaurantCategoryDao;

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantItemDao restaurantItemDao;

    //Category entity
    public CategoryEntity getCategoryViaId(Integer id){
      return  categoryDao.getCategoryViaId(id);
    }
    public CategoryEntity getCategoryViaUuid(String uuid){
        return categoryDao.getCategoryViaUuid(uuid);
    }

    //Category Item entity

    public CategoryItemEntity getCategoryItemViaItemId(Integer id){
        return categoryItemDao.getCategoryItemViaItemId(id);
    }

    public List<CategoryItemEntity> getCategoryItemViaCategoryId(Integer id){
        return categoryItemDao.getCategoryItemViaCategoryId(id);
    }

    //Item entity
    public ItemEntity getItemViaId(Integer id){
        return itemDao.getItemViaId(id);
    }

    //Restaurant Item Entity
    public RestaurantItemEntity getRestaurantItemViaItemId(Integer id){
        return restaurantItemDao.getRestaurantItemViaItemId(id);
    }

    public RestaurantItemEntity getRestaurantItemViaId(Integer id){
        return restaurantItemDao.getRestaurantItemViaId(id);
    }

    public RestaurantItemEntity getRestaurantItemViaRestaurantId(Integer id){
        return restaurantItemDao.getRestaurantItemViaRestaurantId(id);
    }

    //Restaurant Entity

    public List<RestaurantEntity> geRestaurantViaName(String name){
        return restaurantDao.getRestaurantViaName(name);
    }
    public RestaurantEntity getRestaurantViaId(Integer id){
        return restaurantDao.getRestaurantViaId(id);
    }
    public RestaurantEntity getRestaurantViaUuid(String uuid){
        return restaurantDao.getRestaurantViaUuid(uuid);
    }
    public RestaurantEntity getRestaurantViaAddressId(Integer id){
        return restaurantDao.getRestaurantViaAddressId(id);
    }

    //Restaurant Category
    public RestaurantCategoryEntity getRestaurantCategoryViaRestaurantId(Integer id){
        return restaurantCategoryDao.getRestaurantCategoryViaRestaurantId(id);
    }
    public List<RestaurantCategoryEntity> getRestaurantCategoryViaCategoryId(Integer id){
        return restaurantCategoryDao.getRestaurantCategoryViaCategoryId(id);
    }
    public List<RestaurantCategoryEntity> getCategoriesViaRestaurantId(Integer id){
        return restaurantCategoryDao.getCategoryViaRestaurantId(id);
    }


}
