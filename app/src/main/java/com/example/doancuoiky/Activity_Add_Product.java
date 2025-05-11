package com.example.doancuoiky;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doancuoiky.CallAPI.APIService;
import com.example.doancuoiky.CallAPI.Response.OneProductResponse;
import com.example.doancuoiky.CallAPI.RetrofitClient;
import com.example.doancuoiky.Preferences.PreManager;
import com.example.doancuoiky.Util.FileUtils;

import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Product extends AppCompatActivity {
    private PreManager preManager;
    APIService apiService = RetrofitClient.getRetrofit().create(APIService.class);
    Button btnUploadImage,btnSaveProduct;
    TextView btn_back;
    EditText edtProductName,edtDescription,edtQuantity,edtHumidity,edtTemperature,edtPrice;
    ImageView imgProduct;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        AnhXa();
        setContent();
        setClick();
    }
    private void AnhXa(){
        preManager = new PreManager(this);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSaveProduct = findViewById(R.id.btnSaveProduct);
        btn_back = findViewById(R.id.btn_back);
        edtProductName = findViewById(R.id.edtProductName);
        edtDescription = findViewById(R.id.edtDescription);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtHumidity = findViewById(R.id.edtHumidity);
        edtTemperature = findViewById(R.id.edtTemperature);
        edtPrice = findViewById(R.id.edtPrice);
        imgProduct = findViewById(R.id.imgProduct);
    }
    private void setContent(){


    }
    private void setClick(){
        btn_back.setOnClickListener(v->{finish();});
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 100);  // Mã yêu cầu là 100
            }
        });
        btnSaveProduct.setOnClickListener(v->{
            XuliSave();
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == this.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            selectedImageUri = data.getData();
            imgProduct.setImageURI(imageUri);
        }
    }
    private void XuliSave() {
        if (selectedImageUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        String productName = edtProductName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        int stockCount = Integer.parseInt(edtQuantity.getText().toString());
        float humadity = Float.parseFloat(edtHumidity.getText().toString());
        int temperature = Integer.parseInt(edtTemperature.getText().toString());
        int price = Integer.parseInt(edtPrice.getText().toString());
        int status = 1; // Mặc định trạng thái

        try {
            // Đọc dữ liệu từ URI ảnh
            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            // Tạo RequestBody từ byte[]
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "upload.jpg", requestFile);

            // Các phần còn lại
            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), productName);
            RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
            RequestBody stockPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(stockCount));
            RequestBody descPart = RequestBody.create(MediaType.parse("text/plain"), description);
            RequestBody humiPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(humadity));
            RequestBody tempPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(temperature));
            RequestBody statusPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(status));

            // Gọi API
            apiService.addProduct(namePart, pricePart, stockPart, descPart, humiPart, tempPart, statusPart, imagePart)
                    .enqueue(new Callback<OneProductResponse>() {
                        @Override
                        public void onResponse(Call<OneProductResponse> call, Response<OneProductResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                                Toast.makeText(Activity_Add_Product.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK); // Cho phép Activity trước đó biết để reload
                                finish();
                            } else {
                                Toast.makeText(Activity_Add_Product.this, "Lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<OneProductResponse> call, Throwable t) {
                            Toast.makeText(Activity_Add_Product.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Không thể xử lý ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
