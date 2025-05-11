package com.example.doancuoiky.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.doancuoiky.Activity_Login;
import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Config.Config;
import com.example.doancuoiky.Preferences.PreManager;
import com.example.doancuoiky.R;

import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fr_profile_user extends Fragment {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    CircleImageView imageAvatar ;
    Button buttonUpload2;
    EditText editName,editPhone,editCurrentPassword,editNewPassword;
    Spinner spinnerGender;
    Button buttonEdit,buttonLogout;
    TextView labelNewPassword;
    private String password;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmment_profile_user, container, false);
        AnhXa(view);
        setContent();
        setClick();
        return view;
    }

    private void AnhXa(View view){
        preManager = new PreManager(getContext());
        imageAvatar = view.findViewById(R.id.imageAvatar);
        buttonUpload2 = view.findViewById(R.id.buttonUpload2);
        editName = view.findViewById(R.id.editName);
        editPhone = view.findViewById(R.id.editPhone);
        editCurrentPassword = view.findViewById(R.id.editCurrentPassword);
        editNewPassword = view.findViewById(R.id.editNewPassword);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        labelNewPassword = view.findViewById(R.id.labelNewPassword);
    }
    private void setContent(){
        LoadAPI();
        changeStatus(false);
    }

    private void setClick(){
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonEdit.getText()=="Edit Information"){
                    changeStatus(true);
                }
                else{
                    XuliSave();
                }
            }
        });


        buttonUpload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở thư viện ảnh
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);  // Mã yêu cầu là 100
            }
        });
        buttonLogout.setOnClickListener(v->{
            preManager.logout();
            Intent intent = new Intent(getActivity(), Activity_Login.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
    private void changeStatus(boolean bool){

        editName.setEnabled(bool);
        editPhone.setEnabled(bool);
        editCurrentPassword.setEnabled(bool);
        editNewPassword.setEnabled(bool);
        spinnerGender.setEnabled(bool);
        if (bool){
            spinnerGender.setOnTouchListener(null);
            editNewPassword.setText("");
            editCurrentPassword.setText("");
        }
        else{
            spinnerGender.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }
        labelNewPassword.setVisibility(bool ? View.VISIBLE : View.GONE);
        editNewPassword.setVisibility(bool ? View.VISIBLE : View.GONE);
        buttonUpload2.setVisibility(bool ? View.VISIBLE : View.GONE);
        if (bool == true) {
            buttonEdit.setText("Save");
            buttonEdit.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), android.R.color.holo_green_light));
        }
        else{
            buttonEdit.setText("Edit Information");
            buttonEdit.setBackgroundTintList(null);
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            selectedImageUri = data.getData();
            imageAvatar.setImageURI(imageUri);
        }
    }
    void LoadAPI(){
        int idAcc = preManager.getIDAccount();
        apiService.getAccountDetailsByID(Long.valueOf(idAcc)).enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                    editName.setText(response.body().getBody().getName());
                    editPhone.setText(response.body().getBody().getPhone());
                    if (response.body().getBody().isGender()){
                        spinnerGender.setSelection(0);
                    }
                    else{
                        spinnerGender.setSelection(1);
                    }
                    Glide.with(getContext())
                            .load(Config.URL_API_IMAGE+response.body().getBody().getImageUrl())
                            .placeholder(R.drawable.user_avatar)
                            .error(R.drawable.user_avatar)
                            .into(imageAvatar);
                    password = response.body().getBody().getPassword();
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {

            }
        });
    }
    void XuliSave(){
        String name = editName.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String currentPassword = editCurrentPassword.getText().toString().trim();
        String newPassword = editNewPassword.getText().toString().trim();
        boolean gender = spinnerGender.getSelectedItemPosition() == 0;
        if (name.isEmpty()) {
            editName.setError("Please enter your name");
            editName.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            editPhone.setError("Please enter your phone number");
            editPhone.requestFocus();
            return;
        }
        if (currentPassword.isEmpty()) {
            editCurrentPassword.setError("Please enter your current password");
            editCurrentPassword.requestFocus();
            return;
        }
        if (newPassword.isEmpty()) {
            editNewPassword.setError("Please enter a new password");
            editNewPassword.requestFocus();
            return;
        }
        if (!password.equals(currentPassword)) {
            editCurrentPassword.setError("Incorrect password");
            editCurrentPassword.requestFocus();
            return;
        }
        try {
            MultipartBody.Part imagePart = null;
            if (selectedImageUri != null) {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(selectedImageUri);
                byte[] imageBytes = new byte[inputStream.available()];
                inputStream.read(imageBytes);
                inputStream.close();
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                imagePart = MultipartBody.Part.createFormData("avatar", "upload.jpg", requestFile);
            }

            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), name);
            RequestBody phonePart = RequestBody.create(MediaType.parse("text/plain"), phone);
            RequestBody newPart = RequestBody.create(MediaType.parse("text/plain"), newPassword);
            RequestBody genderPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(gender));
            RequestBody idAccount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(preManager.getIDAccount()));

            apiService.updateProfile(newPart,namePart,phonePart,idAccount,genderPart,imagePart).enqueue(new Callback<AccountResponse>() {
                @Override
                public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                    if (response.isSuccessful()&&response.body()!=null&&response.body().isStatus()){
                        Toast.makeText(getContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                        changeStatus(false);
                    }
                    else {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AccountResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
