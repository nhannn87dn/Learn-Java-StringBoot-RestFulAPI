package com.example.ecommerce.dtos.user;

public class UserCreateDto {
    private String fullname;
    private String email;
    private String password;
    private boolean active = true; // default true
    private String[] roles = new String[]{"USER"}; // default USER

    // Getters and setters
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public String[] getRoles() { return roles; }
    public void setRoles(String[] roles) { this.roles = roles; }
}
