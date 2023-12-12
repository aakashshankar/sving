package com.client.api.response;

public class PatientResponse {

    private String patientId;

    private String firstName;

    private String lastName;

    private PatientAttributeResponse attributes;

    private String summary;

    private String tenant;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PatientAttributeResponse getAttributes() {
        return attributes;
    }

    public void setAttributes(PatientAttributeResponse attributes) {
        this.attributes = attributes;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
