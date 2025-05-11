package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class Activity_Register extends AppCompatActivity {
    private Button btn;
    private TextView textLogin;
    private EditText edUname,edEmail,edPassword,edPassConfirm;
    private Button btnBack;
    RadioButton rdMale,rdFemale;
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        AnhXa();
        LogicClick();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btn = findViewById(R.id.button);
        textLogin = findViewById(R.id.textViewCreate);
        btnBack = findViewById(R.id.button2);
        edUname = findViewById(R.id.editTextUser);
        edEmail = findViewById(R.id.editTextEmail);
        edPassword = findViewById(R.id.editTextPassword);
        edPassConfirm = findViewById(R.id.editTextConfirm);
        rdMale = findViewById(R.id.radioButtonMale);
        rdFemale = findViewById(R.id.radioButtonFemale);
    }
    private void LogicClick(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
                startActivity(intent);
                finish();
            }
        });
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Register.this, Activity_Login.class);
                startActivity(intent);
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = edUname.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                String passwordConfirm = edPassConfirm.getText().toString().trim();
                if (uname.length()==0||email.length()==0||passwordConfirm.length()==0||password.length()==0){
                    Toast.makeText(Activity_Register.this, "Information cannot be left blank", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!passwordConfirm.equals(password)) {
                    Log.d("Error",passwordConfirm+" "+password);
                    Toast.makeText(Activity_Register.this, "Password confirmation is incorrect.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Boolean gender;
                if (rdMale.isChecked()) gender = true;
                else gender = false;
                apiService.addAccount(email,uname,password,1L,gender).enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            AccountResponse accountResponse = response.body();
                            if (accountResponse.isStatus() == true){
                                Intent intent = new Intent(Activity_Register.this, Activity_OTP.class);
                                intent.putExtra("IdAccount",accountResponse.getBody().getId());
                                intent.putExtra("email",accountResponse.getBody().getEmail());
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(Activity_Register.this, "Email or Username is active", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toast.makeText(Activity_Register.this, "Server not responding", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
