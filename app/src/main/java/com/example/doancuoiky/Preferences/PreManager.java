package com.example.doancuoiky.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreManager {
    Context context;

    public PreManager(Context context) {
        this.context = context;
    }

    public void saveLoginDetails(int idAccount) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idAccount", idAccount);
        editor.commit();
    }

    public int getIDAccount() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("idAccount", -1);
    }

    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("idAccount"); // Xóa idAccount
        editor.apply(); // Lưu thay đổi
    }
}
