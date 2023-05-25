package com.example.retrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public static String api="https://jsonplaceholder.typicode.com";
    List<userModel> allUserList;
    RecyclerView revMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        revMain=findViewById(R.id.reView);
        revMain.setLayoutManager(new LinearLayoutManager(this));

        RetrofitInstance.getInstance().apiInterface.getUsers().enqueue(new Callback<List<userModel>>() {
            @Override
            public void onResponse(Call<List<userModel>> call, Response<List<userModel>> response) {
                allUserList=response.body();
                revMain.setAdapter(new UserAdapter(MainActivity.this,allUserList));
            }

            @Override
            public void onFailure(Call<List<userModel>> call, Throwable t) {
                Log.e("api", "onFailure: " + t.getLocalizedMessage());
            }
        });

    }
}