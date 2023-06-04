package com.example.ecom;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("products")
    Call<String> getProducts();
}
