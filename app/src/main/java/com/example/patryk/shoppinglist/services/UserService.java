package com.example.patryk.shoppinglist.services;

import com.example.patryk.shoppinglist.models.User;
import com.example.patryk.shoppinglist.utils.Token;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {
    @POST("user/")
    Call<User> createUser(@Body User user);

    @FormUrlEncoded
    @POST("api-token-auth/")
    Call<Token> login(@Field("username") String username, @Field("password") String password);
}
