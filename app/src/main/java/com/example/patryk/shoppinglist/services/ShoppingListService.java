package com.example.patryk.shoppinglist.services;

import com.example.patryk.shoppinglist.models.ShoppingList;
import com.example.patryk.shoppinglist.models.User;
import com.example.patryk.shoppinglist.models.UserFriend;
import com.example.patryk.shoppinglist.models.WholeShoppingList;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ShoppingListService {
    @GET("shoppinglist/my_shopping_lists/")
    Call<List<ShoppingList>> getMyShoppingLists();

    @FormUrlEncoded
    @POST("shoppinglist/add_shopping_list/")
    Call<ShoppingList> addShoppingList(@Field("name") String name);

    @POST("shoppinglist/create_whole_list/")
    Call<Void> createWholeShoppingList(@Body WholeShoppingList shoppingList);
}
