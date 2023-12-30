package com.client.service;

import com.client.api.request.LoginRequest;
import com.client.api.request.PatientRequest;
import com.client.api.request.SummarizedSpeechRequest;
import com.client.api.request.TenantRequest;
import com.client.api.response.PatientResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static com.client.Main.useLocal;
import static java.lang.String.format;


public class BackendCommunicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackendCommunicator.class);

    private static String BASE_URL = useLocal ? "http://localhost:8080" :
            "https://speech-to-text-production-c702.up.railway.app";

    private static final String BASE_TENANT_URL = format("%s/api/v1/tenant", BASE_URL);

    private static final String BASE_PATIENT_URL = format("%s/api/v1/patient", BASE_URL);

    private static final String BASE_SPEECH_TO_TEXT_URL = format("%s/api/v1/speechtotext", BASE_URL);

    private static final String X_TENANT = "X-Tenant";

    private static ObjectMapper MAPPER = new ObjectMapper();

    private HttpClient httpClient;

    public BackendCommunicator() {
        httpClient = HttpClient.newHttpClient();
    }

    public HttpResponse<String> login(String username, String password) {
        LOGGER.info("Logging in tenant with username {}", username);
        String requestJson;
        try {
            requestJson = MAPPER.writeValueAsString(new LoginRequest(username, password));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(BASE_TENANT_URL + "/noauth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred while logging in");
            throw new RuntimeException(e);
        }

    }

    public HttpResponse<String> register(String name, String email, String username, String phone, String password) {
        LOGGER.info("Registering tenant with email {}", email);
        String requestJson;
        try {
            requestJson = MAPPER.writeValueAsString(new TenantRequest(name, email, username, phone, password));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(BASE_TENANT_URL + "/noauth/register"))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred while registering tenant");
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> getTenant(String sessionId, String username) {
        LOGGER.info("Getting tenant with username {}", username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_TENANT_URL + "/me"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.COOKIE, sessionId)
                .header(X_TENANT, username)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while fetching tenant");
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> transcribe(File audioFile, String sessionId, String username) {
        LOGGER.info("Transcribing audio file {}", audioFile.getName());
        try {
            Path path = audioFile.toPath();
            byte[] content = Files.readAllBytes(path);
            String boundary = UUID.randomUUID().toString();
            String formDataHeader = "--" + boundary + "\r\n" +
                    "Content-Disposition: form-data; name=\"audio\"; filename=\"" + path.getFileName() + "\"\r\n" +
                    "Content-Type: application/octet-stream\r\n\r\n";
            String formDataFooter = "\r\n--" + boundary + "--\r\n";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(BASE_SPEECH_TO_TEXT_URL + "/transcribe"))
                    .header(HttpHeaders.CONTENT_TYPE, "multipart/form-data; boundary=" + boundary)
                    .header(HttpHeaders.COOKIE, sessionId)
                    .header(X_TENANT, username)
                    .POST(HttpRequest.BodyPublishers.ofByteArrays(List.of(formDataHeader.getBytes(), content,
                            formDataFooter.getBytes())))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                LOGGER.error("Error occurred with response code {}", response.statusCode());
                throw new RuntimeException();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error occurred while transcribing audio");
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> summarize(String transcript, String sessionId, String username) {
        LOGGER.info("Summarizing transcript {}", transcript);
        String requestJson;
        try {
            requestJson = MAPPER.writeValueAsString(new SummarizedSpeechRequest(transcript));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(BASE_SPEECH_TO_TEXT_URL + "/summarize"))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.COOKIE, sessionId)
                    .header(X_TENANT, username)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            LOGGER.error("Error occurred while attaching transcript to patient");
            JOptionPane.showMessageDialog(null, "Error occurred while attaching transcript to patient");
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> addPatientAndAttachSummary(String firstName, String lastName, String summary, String sessionId, String username) {
        LOGGER.info("Registering patient with name {}", firstName + " " + lastName);
        String requestJson;
        try {
            requestJson = MAPPER.writeValueAsString(new PatientRequest(firstName, lastName));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(BASE_PATIENT_URL + "/register"))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.COOKIE, sessionId)
                    .header(X_TENANT, username)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                PatientResponse patientResponse = readPatientResponse(response);
                return attachSummary(summary, patientResponse.getPatientId(), sessionId, username);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while registering patient");
            JOptionPane.showMessageDialog(null, "Error occurred while registering patient");
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> attachSummary(String summary, String patientId, String sessionId, String username) {
        LOGGER.info("Attaching summary to patient with id {}", patientId);
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(BASE_PATIENT_URL + "/" + patientId + "/attach_summary"))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
                    .header(HttpHeaders.COOKIE, sessionId)
                    .header(X_TENANT, username)
                    .PUT(HttpRequest.BodyPublishers.ofString(summary))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }

        } catch (Exception e) {
            LOGGER.error("Error occurred while attaching summary to patient");
            JOptionPane.showMessageDialog(null, "Error occurred while attaching summary to patient");
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> getAllForTenant(String sessionId, String username) {
        LOGGER.info("Getting all patients for tenant {}", username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_PATIENT_URL + "/all"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.COOKIE, sessionId)
                .header(X_TENANT, username)
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while fetching patients");
            throw new RuntimeException(e);
        }

    }

    private PatientResponse readPatientResponse(HttpResponse<String> response) {
        try {
            return MAPPER.readValue(response.body(), PatientResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> deletePatient(String sessionId, String username, String patientId) {
        LOGGER.info("Deleting patient with id {}", patientId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_PATIENT_URL + "/" + patientId + "/delete"))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.COOKIE, sessionId)
                .header(X_TENANT, username)
                .DELETE()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                return response;
            } else {
                throw new RuntimeException();
            }
        } catch (IOException | InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error occurred while deleting patient");
            throw new RuntimeException(e);
        }
    }
}
