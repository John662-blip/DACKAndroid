package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class Activity_OTP extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    private Button btnBack,btnSubmit;
    private TextView textEmail;
    private EditText edOTP;
    private int idAccount = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_comfirm);
        AnhXa();
        SendOTP();
        EventClickBtn();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btnBack = findViewById(R.id.button2);
        btnSubmit = findViewById(R.id.button);
        edOTP = findViewById(R.id.editTextPassword);
        textEmail = findViewById(R.id.textViewCreate);
    }
    private void EventClickBtn(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_OTP.this, Activity_Register.class);
                startActivity(intent);
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textOTP = edOTP.getText().toString().trim();
                if (textOTP.length()!=5){
                    Toast.makeText(Activity_OTP.this, "OTP is incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (idAccount == -1){
                    Toast.makeText(Activity_OTP.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    return;
                }
                apiService.comfirmOtp(idAccount,textOTP).enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            AccountResponse accountResponse = response.body();
                            if (accountResponse.isStatus() == true){
                                preManager.saveLoginDetails(accountResponse.getBody().getId());
                                Intent intent = new Intent(Activity_OTP.this, Activity_Main_User.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Activity_OTP.this, "Wrong OTP code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toast.makeText(Activity_OTP.this, "Server not responding", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void SendOTP(){
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            idAccount = extras.getInt("IdAccount",-1);
            String email = extras.getString("email","");
            textEmail.setText(email);
            if (idAccount==-1) {
                Toast.makeText(this, "An error occurred.", Toast.LENGTH_SHORT).show();
                return;
            }
            apiService.sendOTP_creatAccount(idAccount).enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
//                    Toast.makeText(Activity_OTP.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
