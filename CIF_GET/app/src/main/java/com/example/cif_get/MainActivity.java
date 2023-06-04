package com.example.cif_get;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<ModelClass> Data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView=findViewById(R.id.RecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        Call<String> call=jsonPlaceHolderApi.getProducts();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    if (response.body() != null) {

                        String products = response.body();

                        ArrayList<String> data = showData(products);
                        ModelClass model;

                        for (int i=0;i<data.size();i=i+9) {
                            model = new ModelClass(data.get(i), data.get(i + 1), data.get(i + 2), data.get(i + 3), data.get(i + 4), data.get(i + 5),data.get(i + 6),data.get(i + 7),data.get(i + 8));
                            Data.add(model);
                            Log.d("Arya", "onResponse: "+data.get(i+2));
                        }

                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, Data);

                        recyclerView.setAdapter(adapter);


                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Arya", "onResponse: "+t.getMessage());
            }
        });

    }

    private ArrayList<String> showData(String products) {

        ArrayList<String> Data=new ArrayList<String>();
        try {
            JSONObject jsonObject=new JSONObject(products);
            String str=jsonObject.getString("products");
            JSONArray jsonArray=new JSONArray(str);

//            Log.d("Arya1", "onResponse: " + products);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                Data.add(jsonObject1.getString("title"));
                Data.add(jsonObject1.getString("brand"));
                Data.add(jsonObject1.getString("thumbnail"));
                Data.add(jsonObject1.getString("description"));

                //getting the new price
                int discountPercentage = (int) (jsonObject1.getInt("price") * (1 - jsonObject1.getDouble("discountPercentage")/100));
                Data.add(String.valueOf("$"+discountPercentage));//price

                Data.add(jsonObject1.getString("stock"));
                Data.add(jsonObject1.getString("discountPercentage"));
                Data.add(jsonObject1.getString("rating"));
                Data.add(jsonObject1.getString("images"));
            }
            return Data;

        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }
    }
}