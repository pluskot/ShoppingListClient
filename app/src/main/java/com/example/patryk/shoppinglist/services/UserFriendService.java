package com.example.patryk.shoppinglist.services;

import com.example.patryk.shoppinglist.models.User;
import com.example.patryk.shoppinglist.models.UserFriend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserFriendService {

    @GET("userfriend/my_friends/")
    Call<List<User>> getMyFriends();

    @FormUrlEncoded
    @POST("userfriend/add_friend/")
    Call<UserFriend> addFriend(@Field("username") String username);
}
