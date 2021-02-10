package com.zhou.lums.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PasswordRequest {

    @NotNull
    @Size(min=1)
    private String oldPassword;

    @NotNull
    @Size(min=1)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }



}
