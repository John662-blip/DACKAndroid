package com.example.doancuoiky.CallAPI;

import com.example.doancuoiky.CallAPI.Response.AccountResponse;
import com.example.doancuoiky.CallAPI.Response.BillAllResponse;
import com.example.doancuoiky.CallAPI.Response.BillPageResponse;
import com.example.doancuoiky.CallAPI.Response.BillResponse;
import com.example.doancuoiky.CallAPI.Response.CartResponse;
import com.example.doancuoiky.CallAPI.Response.CategoryResponse;
import com.example.doancuoiky.CallAPI.Response.CountResponse;
import com.example.doancuoiky.CallAPI.Response.ObjectResponse;
import com.example.doancuoiky.CallAPI.Response.OneCategoryResponse;
import com.example.doancuoiky.CallAPI.Response.OneProductHasCategory;
import com.example.doancuoiky.CallAPI.Response.OneProductResponse;
import com.example.doancuoiky.CallAPI.Response.ProductPageResponse;
import com.example.doancuoiky.CallAPI.Response.ProductResponse;
import com.example.doancuoiky.CallAPI.Response.genera.BillPage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIService {
    ///Account
    @POST("/api/account/findByID")
    Call<AccountResponse> getAccountDetailsByID(@Query("IdAcount") Long idAccount);
    @POST("/api/account/findByUnameAndPassword")
    Call<AccountResponse> getAccountByUnameAndPassword(@Query("uname") String uname,
                                                       @Query("password") String password);
    @POST("/api/account/addAccount")
    Call<AccountResponse> addAccount(@Query("email") String email,
                                     @Query("Uname") String uname,
                                     @Query("password") String password,
                                     @Query("type") Long type,
                                     @Query("gender") Boolean gender
                                     );
    @POST("/api/otp_token/sendOTP_CreateAccount")
    Call<AccountResponse> sendOTP_creatAccount(@Query("idAccount") int idAccount);
    @POST("/api/otp_token/comfirm_OTP")
    Call<AccountResponse> comfirmOtp(@Query("idAccount") int idAccount,
                                     @Query("otp") String otp);
    @POST("/api/otp_token/sendOTP_Reset")
    Call<AccountResponse> sendOTP_Reset(@Query("email") String email);
    @POST("/api/otp_token/findForget")
    Call<AccountResponse> findForget(@Query("email") String email);
    @POST("/api/otp_token/reset_OTP")
    Call<AccountResponse> comfirm_reset(@Query("idAccount") int idAccount,@Query("otp") String otp);

    @POST("/api/account/update_account")
    Call<AccountResponse> update_Password(@Query("idAccount") int idAccount,@Query("password") String password);
    @Multipart
    @POST("/api/account/update_account")
    Call<AccountResponse> updateProfile(@Part("password") RequestBody password,
                                        @Part("name") RequestBody name,
                                        @Part("phone") RequestBody phone,
                                        @Part("idAccount") RequestBody idAccount,
                                        @Part("gender") RequestBody gender,
                                        @Part MultipartBody.Part avatar
    );

///Category
    @GET("/api/category")
    Call<CategoryResponse> getAllCategory();
    @POST("/api/category/addCategory")
    Call<OneCategoryResponse> addCategory(@Query("categoryName") String name);
    @DELETE("/api/category/deleteCategory")
    Call<OneCategoryResponse> deleteCategory (@Query("categoryId") int id);
    @PUT("/api/category/updateCategory")
    Call<OneCategoryResponse> updateCategory (@Query("categoryId") int id , @Query("categoryName") String name);


    ///Product
    @Multipart
    @POST("/api/product/addProduct")
    Call<OneProductResponse> addProduct(@Part("productName") RequestBody productName,
                                        @Part("price") RequestBody price,
                                        @Part("stockCount") RequestBody stockCount,
                                        @Part("description") RequestBody description,
                                        @Part("Humadity") RequestBody humadity,
                                        @Part("temperature") RequestBody temperature,
                                        @Part("status") RequestBody status,
                                        @Part MultipartBody.Part image
                                        );
    @GET("/api/product/getPageProduct")
    Call<ProductPageResponse> getPageProduct(@Query("page") int page,
                                            @Query("size") int size);
    @POST("/api/product/getPageProductsByCategoryID")
    Call<ProductPageResponse> getPageProductByIDCategory(@Query("categoryID") int id,
                                                         @Query("page") int page,
                                                        @Query("size") int size);
    @PUT("/api/product/changeStatusProduct")
    Call<OneProductResponse> changeStatusProduct(@Query("productID") int id);
    @POST("/api/product/getProduct")
    Call<OneProductHasCategory> getOneProduct(@Query("id") int id);
    @POST("/api/product/ProductAddCategory")
    Call<OneProductResponse> ProductAddCategory(@Query("productID") Long idProduct,
                                                @Query("categoryID") Long idCategory);
    @POST("/api/product/getProductsByCategoryID")
    Call<ProductResponse> getProductCategory(@Query("categoryID") int idCategory);
    @POST("/api/product/getPageProductByName")
    Call<ProductPageResponse> getPageProductByName(@Query("key") String key,
                                               @Query("page") int page,
                                               @Query("size") int size);

    //Cart
    @POST("/api/cart/getCountItemCart")
    Call<CountResponse> getCountItemCart(@Query("idAccount") int idAccount);
    @POST("/api/cart/getItemCartByIDAccount")
    Call<CartResponse> getItemCartByIDAccount(@Query("idAccount") int idAccount);
    @POST("/api/cart/addItemInCart")
    Call<CartResponse> addItemInCart(@Query("idAccount") int idAccount,
                                     @Query("idProduct") int idProduct);
    @POST("/api/cart/removeItemInCart")
    Call<CartResponse> removeItemInCart(@Query("idAccount") int idAccount,
                                     @Query("idProduct") int idProduct);
    @POST("/api/cart/deleteItemInCart")
    Call<CartResponse> deleteItemInCart(@Query("idAccount") int idAccount,
                                        @Query("idProduct") int idProduct);

    //Bill
    @POST("/api/bill/order")
    Call<BillResponse> order(@Query("idAccount") int idAccount,
                             @Query("address") String address);
    @POST("/api/bill/getAllBillByIdAccount")
    Call<BillAllResponse> getAllBillByIdAccount(@Query("idAccount") int idAccount);
    @POST("/api/bill/getBilllByBillID")
    Call<BillResponse> getBilllByBillID(@Query("idBill") int idBill);
    @POST("/api/bill/cancelBill")
    Call<BillResponse> cancelBill(@Query("idBill") int idBill);
    @POST("/api/bill/getAllPageBill")
    Call<BillPageResponse> getAllPageBill(@Query("page") int page,
                                      @Query("size") int size);
    @POST("/api/bill/getPageBillDate")
    Call<BillPageResponse> getPageBillDate(
            @Query("dateStart") String dateStart,
            @Query("dateEnd") String dateEnd,
            @Query("page") int page,
            @Query("size") int size);
    @POST("/api/bill/changeStatusBill")
    Call<BillResponse> changeStatusBill(@Query("idBill") int idBill);

    //Statist
    @POST("/api/bill/findTopSellingProductsByMonth")
    Call<ObjectResponse> findTopSellingProductsByMonth(@Query("month") int month);
    @POST("/api/bill/getMonthlyRevenueByYear")
    Call<ObjectResponse> getMonthlyRevenueByYear(@Query("year") int year);

}
