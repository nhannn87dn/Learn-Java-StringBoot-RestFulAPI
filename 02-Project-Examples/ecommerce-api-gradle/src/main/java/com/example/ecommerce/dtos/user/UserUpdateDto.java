package com.example.ecommerce.dtos.user;

public class UserUpdateDto {
    private Long id;
    private String fullname;
    private String email;
    private String password;
    private Boolean active; // allow null
    private String[] roles;

    public UserUpdateDto() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Boolean isActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public String[] getRoles() { return roles; }
    public void setRoles(String[] roles) { this.roles = roles; }
}
