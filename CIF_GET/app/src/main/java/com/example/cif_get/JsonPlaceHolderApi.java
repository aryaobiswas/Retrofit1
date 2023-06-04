package com.example.cif_get;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("products")
    Call<String> getProducts();
}
