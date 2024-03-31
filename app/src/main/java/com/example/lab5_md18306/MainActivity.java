package com.example.lab5_md18306;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_md18306.Model.ModelLab5;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    String TAG = "NAAAAAAaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAA";
    RecyclerView rcvList;
    AdapterLab5 adapterLab5;

    List<ModelLab5> listEnterprise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rcvList = findViewById(R.id.rcvList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rcvList.setLayoutManager(layoutManager);
        listEnterprise = new ArrayList<>();
        adapterLab5 = new AdapterLab5(listEnterprise, this);
        GetAllEnterprise();

    }

    void GetAllEnterprise() {
        //tạo converter
        Gson gson = new GsonBuilder().setLenient().create();
        //khởi tạo retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        // tạo interface
        ApiService apiService = retrofit.create(ApiService.class);
        //tạo dối tượng call
        Call<List<ModelLab5>> call = apiService.layDanhsach();
        //thực hiện gọi hàm để lấy dữ liệu
        call.enqueue(new Callback<List<ModelLab5>>() {
            @Override
            public void onResponse(Call<List<ModelLab5>> call, Response<List<ModelLab5>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"Lấy dữ liệu thành công"+ response.body().toString());
                    //Cập nhật list và hển thị lên danh sách
                    listEnterprise.clear();
                    listEnterprise.addAll(response.body());
                    rcvList.setAdapter(adapterLab5);
                    adapterLab5.notifyDataSetChanged();
                }else {
                    Log.d(TAG, "onResponse: Không lấy được dữ liệu");
                }

            }
            @Override
            public void onFailure(Call<List<ModelLab5>> call, Throwable throwable) {
                Log.e(TAG, "onFailure: "+ throwable.getMessage());
                throwable.printStackTrace();
            }
        });



    }
}