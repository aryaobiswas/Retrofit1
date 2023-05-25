package com.example.cif_get;

import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult=findViewById(R.id.text_view_result);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        Call<String> call=jsonPlaceHolderApi.getProducts();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.isSuccessful()){
                    textViewResult.setText("Code: "+ response.code());
                    return;
                }

                if(response.isSuccessful())
                    if(response.body()!=null) {
//                        String products=response.body().toString();
                        String products=response.body().toString();
                        Log.d("Arya", "onResponse: "+products);
                    }



//                for(Product product : products){
//                    String content="";
//                    content += "ID: "+ product.getId() + "\n";
//                    content += "Title: "+ product.getTitle() + "\n";
//
//                    textViewResult.append(content);
//                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }
}