package com.example.lab5_md18306;

import com.example.lab5_md18306.Model.ModelLab5;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    String DOMAIN = "http://192.168.1.75:3000/";

    @GET("api/")
    Call<List<ModelLab5>> layDanhsach();


    @POST("api/enterprise")
    Call<ModelLab5> themDanhSach(@Body ModelLab5 modelLab5);


    @PUT("api/enterprise/{id}")
    Call<ModelLab5> suaDanhSach(@Path("id") String id, @Body ModelLab5 modelLab5);


    @DELETE("api/enterprise/{id}")
    Call<Void> xoaDanhSach(@Path("id") String id);
}
