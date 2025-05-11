package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Activity_Login extends AppCompatActivity {
    private TextView textSigin;
    private TextView textForot;
    private EditText textUname;
    private EditText textPassword;
    private Button btnLogin;
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        AnhXa();
        EventClickSigin();
        EventClickForgot();
        EventClickButton();
    }

    private void AnhXa(){
        preManager = new PreManager(this);
        textSigin = findViewById(R.id.textViewCreate);
        textForot = findViewById(R.id.textView3);
        textUname = findViewById(R.id.editTextText);
        textPassword = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.button);
    }
    private void EventClickSigin(){
        textSigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Register.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void EventClickForgot(){
        textForot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_ForgetPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void EventClickButton(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textUname_value = textUname.getText().toString();
                String textPassword_value = textPassword.getText().toString();
                apiService.getAccountByUnameAndPassword(textUname_value,textPassword_value).enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            AccountResponse accountResponse = response.body();
                            if (accountResponse.isStatus() == true){
                                if (accountResponse.getBody().getType()==1){
                                    preManager.saveLoginDetails(accountResponse.getBody().getId());
                                    Intent intent = new Intent(Activity_Login.this, Activity_Main_User.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    preManager.saveLoginDetails(accountResponse.getBody().getId());
                                    Intent intent = new Intent(Activity_Login.this, Activity_Main_Admin.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else{
                                Toast.makeText(Activity_Login.this, "Account not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Log.e("API_ERROR", "Request failed: " + t.getMessage());
                        Toast.makeText(Activity_Login.this, "Server not responding", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
