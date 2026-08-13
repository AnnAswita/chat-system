package com.chatapp.chatsystemeda.commands;

public class AuthenticateUserCommand {
    private String username;
    private String password;

    // ⭐ Add this field
    private String generatedToken;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // ⭐ Add getter + setter for token
    public String getGeneratedToken() { return generatedToken; }
    public void setGeneratedToken(String generatedToken) { this.generatedToken = generatedToken; }
}
