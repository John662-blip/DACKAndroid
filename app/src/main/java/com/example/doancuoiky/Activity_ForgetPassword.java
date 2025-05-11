package com.example.doancuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Preferences.PreManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_ForgetPassword extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    Button btn_back,btn_confirm;
    TextView textEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password);
        AnhXa();
        setContent();
        setClick();
    }
    private void AnhXa(){
        btn_back = findViewById(R.id.button2);
        textEmail = findViewById(R.id.editEmail);
        btn_confirm = findViewById(R.id.button);
    }
    private void setContent(){

    }
    private void setClick(){
        btn_back.setOnClickListener(v->{
            Intent intent = new Intent(Activity_ForgetPassword.this,Activity_Login.class);
            startActivity(intent);
            finish();
        });
        btn_confirm.setOnClickListener(v->{
            String email = textEmail.getText().toString().trim();
            if (email.length()==0){
                Toast.makeText(this, "Email cannot be left blank", Toast.LENGTH_SHORT).show();
            }
            else{
                apiService.findForget(email).enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        AccountResponse res = response.body();
                        if (res.isStatus()){
                            int idAcc = res.getBody().getId();
                            openOTPDialog(idAcc);
                            apiService.sendOTP_Reset(email).enqueue(new Callback<AccountResponse>() {
                                @Override
                                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                                }

                                @Override
                                public void onFailure(Call<AccountResponse> call, Throwable t) {
                                }
                            });

                        }
                        else{
                            Toast.makeText(Activity_ForgetPassword.this, "No active account found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toast.makeText(Activity_ForgetPassword.this, "server not responding", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            
        });
    }
    private void openOTPDialog(int idAccount){
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Đổi thành tên activity của bạn

        builder.setTitle("OTP Authentication");
        builder.setMessage("Please enter the OTP code sent to email:");

        // Tạo EditText để nhập OTP
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("Enter OTP code...");
        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String otp = input.getText().toString().trim();
            if (otp.length()==0){
                Toast.makeText(this, "OTP cannot be left blank", Toast.LENGTH_SHORT).show();
            }
            else{
                apiService.comfirm_reset(idAccount,otp).enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        AccountResponse res = response.body();
                        if (res.isStatus()){
                            openChangePassword(idAccount);
                            dialog.cancel();
                        }
                        else{
                            Toast.makeText(Activity_ForgetPassword.this, "OTP code is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toast.makeText(Activity_ForgetPassword.this, "server not responding", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
    private void openChangePassword(int idAccount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // Đảm bảo activity context chính xác

        builder.setTitle("Change Password");
        builder.setMessage("Please enter your new password and confirm it:");

        // Tạo layout tùy chỉnh với 2 EditText cho mật khẩu mới và xác nhận mật khẩu
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Mật khẩu mới
        final EditText newPassword = new EditText(this);
        newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPassword.setHint("New Password...");
        layout.addView(newPassword);

        // Mật khẩu xác nhận
        final EditText confirmPassword = new EditText(this);
        confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPassword.setHint("Confirm Password...");
        layout.addView(confirmPassword);

        builder.setView(layout);

        // Set button xác nhận
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String newPass = newPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();
            if (newPass.length()==0){
                Toast.makeText(this, "Password cannot be left blank", Toast.LENGTH_SHORT).show();
            }
            else 
            if (newPass.equals(confirmPass)) {
                apiService.update_Password(idAccount,newPass).enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                        AccountResponse res = response.body();
                        if (res.isStatus()){
                            Toast.makeText(Activity_ForgetPassword.this, "Update successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_ForgetPassword.this,Activity_Login.class);
                            dialog.cancel();
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Activity_ForgetPassword.this, "Update failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Toast.makeText(Activity_ForgetPassword.this, "server not responding", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Thông báo nếu mật khẩu không trùng khớp
                Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Button hủy
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}
