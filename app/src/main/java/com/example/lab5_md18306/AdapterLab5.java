package com.example.lab5_md18306;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5_md18306.Model.ModelLab5;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterLab5 extends RecyclerView.Adapter<AdapterLab5.ViewHolder> {

    private List<ModelLab5> list;
    private Context context;

    String TAG ="nanannanananananananananananananananan";
    public AdapterLab5(List<ModelLab5> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelLab5 model = list.get(position);
        holder.txtList.setText("Enterprise: " + model.getEnterprise());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, "heheheh", Toast.LENGTH_SHORT).show();
                UpdateEnterprise(model);
                return view.isLongClickable();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có chắc muốn xóa không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteEnterprise(model);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtList;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtList = itemView.findViewById(R.id.txtList);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void DeleteEnterprise(ModelLab5 model){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d(TAG, "DeleteEnterprise: " + model.get_Id());
        ApiService apiService = retrofit.create(ApiService.class);
        Call<Void> call = apiService.xoaDanhSach(model.get_Id());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    list.remove(model);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                    Toast.makeText(context, "Xóa kh thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.getMessage());
                Toast.makeText(context, "Xóa không thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void UpdateEnterprise(ModelLab5 model){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.from(this.context).inflate(R.layout.item_update, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        EditText edtEnterprise = view.findViewById(R.id.edtEnterpriseUD);
        Button btnUp = view.findViewById(R.id.btnUp);
        Button btnCancle = view.findViewById(R.id.btnCancle);

        edtEnterprise.setText(model.getEnterprise());

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newEnterprise = edtEnterprise.getText().toString();

                //taojo đối tượng mới
                ModelLab5 upmodelLab5 = new ModelLab5(newEnterprise);


                //gọi retrofit

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiService.DOMAIN)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiService apiService = retrofit.create(ApiService.class);
                Call<ModelLab5> call = apiService.suaDanhSach(model.get_Id(), upmodelLab5);
//                Toast.makeText(context, "alooooooooo", Toast.LENGTH_SHORT).show();


                call.enqueue(new Callback<ModelLab5>() {
                    @Override
                    public void onResponse(Call<ModelLab5> call, Response<ModelLab5> response) {
                        Toast.makeText(context, "alooooooooo", Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()){
                            Call<List<ModelLab5>> call1 = apiService.layDanhsach();
                            Toast.makeText(context, "alooooooooo", Toast.LENGTH_SHORT).show();
                            call1.enqueue(new Callback<List<ModelLab5>>() {
                                @Override
                                public void onResponse(Call<List<ModelLab5>> call, Response<List<ModelLab5>> response) {
                                    if (response.isSuccessful()){
                                        List<ModelLab5> fechEnterprise = response.body();
                                        if(fechEnterprise != null){
                                            list.clear();
                                            list.addAll(fechEnterprise);
                                            notifyDataSetChanged();
                                        }
                                    }else {
                                        Log.e(TAG, "onResponse: "+ response.message());
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<ModelLab5>> call, Throwable throwable) {
                                    Log.e(TAG, "onFailure: "+ response.message() );
                                }
                            });
                            Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }else {
                            Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ModelLab5> call, Throwable throwable) {
                        Log.e("Update EnterPrise", "Error updating Enterprise: " + throwable.getMessage());
                    }
                });
            }
        });
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }
}

