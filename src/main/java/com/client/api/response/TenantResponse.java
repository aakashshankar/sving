package com.client.api.response;

import java.util.Set;

public class TenantResponse {

    private String name;

    private String email;

    private String username;

    private String phone;

    private String password;

    private Set<PatientResponse> patients;

    public TenantResponse(String name, String email, String username, String phone, String password,
                          Set<PatientResponse> patients) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.password = password;
        this.patients = patients;
    }

    public TenantResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<PatientResponse> getPatients() {
        return patients;
    }

    public void setPatients(Set<PatientResponse> patients) {
        this.patients = patients;
    }
}
