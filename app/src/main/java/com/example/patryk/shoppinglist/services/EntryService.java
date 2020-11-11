package com.example.patryk.shoppinglist.services;

import com.example.patryk.shoppinglist.models.Entry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EntryService {
    @GET("entry/entries/{id}")
    Call<List<Entry>> getEntries(@Path("id") int id);

    @POST("entry/add/{pk}/")
    Call<Entry> addEntry(@Body Entry entry, @Path("pk") int pk);

    @DELETE("entry/{id}/")
    Call<Entry> deleteEntry(@Path("id") int id);
}
