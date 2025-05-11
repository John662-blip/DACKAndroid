package com.example.doancuoiky.CallAPI.Response;

public class AccountResponse {
    private boolean status;
    private String message;
    private Body body;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Body getBody() {
        return body;
    }
    public static class Body {
        private int id;
        private String imageUrl;
        private String email;
        private String password;
        private String name;
        private String phone;
        private int type;
        private int accountStatus;
        private String uname;
        private boolean gender;

        public int getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public int getType() {
            return type;
        }

        public int getAccountStatus() {
            return accountStatus;
        }

        public String getUname() {
            return uname;
        }

        public boolean isGender() {
            return gender;
        }
    }
}
