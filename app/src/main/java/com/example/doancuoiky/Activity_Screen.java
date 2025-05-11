package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Preferences.PreManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Screen extends AppCompatActivity {
    private Button btn;
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen);
        AnhXa();
        EventClickStart();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btn = findViewById(R.id.button);
    }
    private void EventClickStart(){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idAccount = preManager.getIDAccount();
                if (idAccount==-1){
                    Intent intent = new Intent(Activity_Screen.this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    apiService.getAccountDetailsByID(Long.valueOf(idAccount)).enqueue(new Callback<AccountResponse>() {
                        @Override
                        public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                            if (response.isSuccessful() && response.body()!=null){
                                AccountResponse accountResponse = response.body();
                                if (accountResponse.isStatus() == false){
                                    Intent intent = new Intent(Activity_Screen.this, Activity_Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    AccountResponse.Body body = accountResponse.getBody();
                                    if (body.getAccountStatus()==1){
                                        if (accountResponse.getBody().getType()==1){
                                            Intent intent = new Intent(Activity_Screen.this, Activity_Main_User.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Intent intent = new Intent(Activity_Screen.this, Activity_Main_Admin.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<AccountResponse> call, Throwable t) {
                            Intent intent = new Intent(Activity_Screen.this, Activity_Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
