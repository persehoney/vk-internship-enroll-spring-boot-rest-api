package ru.vk.intern.authorization;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserCredentials {
    @NotEmpty
    @Size(min = 2, max = 255)
    private String username;

    @NotEmpty
    @Size(min = 1, max = 60)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
