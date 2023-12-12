package com.client;

import com.client.api.response.TenantResponse;
import com.client.components.CustomButton;
import com.client.components.PlaceholderPasswordField;
import com.client.components.PlaceholderTextField;
import com.client.converter.PatientResponseToSummaryConverter;
import com.client.service.BackendCommunicator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miginfocom.swing.MigLayout;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class Register extends JPanel {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JTextField name;

    private JTextField email;

    private JTextField username;

    private JTextField phone;

    private JPasswordField password;

    private CustomButton register;

    private CustomButton login;

    private BackendCommunicator backendCommunicator;

    public Register(Main main) throws IOException {
        name = new PlaceholderTextField("Name");
        email = new PlaceholderTextField("Email");
        username = new PlaceholderTextField("Username");
        phone = new PlaceholderTextField("Phone");
        password = new PlaceholderPasswordField("Password");
        register = new CustomButton("Register", 15);
        login = new CustomButton("Login", 15);
        backendCommunicator = new BackendCommunicator();

        setLayout(new MigLayout("insets 10, fillx", "[grow][]", "[]"));
        main.setSize(350, 400);
        add(name, "span, growx, wrap, gapbottom 10");
        add(email, "span, growx, wrap, gapbottom 10");
        add(username, "span, growx, wrap, gapbottom 10");
        add(phone, "span, growx, wrap, gapbottom 10");
        add(password, "span, growx, wrap, gapbottom 10");
        add(register, "span, split 2, growx, sizegroup btn, align center, gapright 15");
        add(login, "span, split 2, growx, sizegroup btn, align center, gapright 15");

        login.addActionListener(e -> {
            main.updateCardPanel(new Login(main), "login");
            main.switchTo("login");
        });

        register.addActionListener(e -> {
            List<ConstraintViolation> violations = getConstraintViolations();
            validateAndProceed(main, violations);
        });

        password.addActionListener(e -> {
            List<ConstraintViolation> violations = getConstraintViolations();
            validateAndProceed(main, violations);
        });
    }

    private void validateAndProceed(Main main, List<ConstraintViolation> violations) {
        if (!violations.isEmpty()) {
            StringBuilder message = new StringBuilder();
            for (ConstraintViolation violation : violations) {
                message.append(violation.getMessage()).append("\n");
            }
            JOptionPane.showMessageDialog(Register.this, message.toString());
        } else {
            // Proceed with further processing
            TenantResponse tenantResponse;

            HttpResponse<String> tenantHttpResponse = backendCommunicator.register(name.getText(), email.getText(),
                    username.getText(), phone.getText(), new String(password.getPassword()));

            HttpResponse<String> loginResponse = backendCommunicator.login(username.getText(),
                    new String(password.getPassword()));

            tenantResponse = readTenantResponse(tenantHttpResponse);
            String sessionId = extractSessionId(loginResponse);
            main.updateCardPanel(new Tenant(main, sessionId, tenantResponse.getUsername(),
                    PatientResponseToSummaryConverter.convertAll(sessionId, tenantResponse.getUsername(),
                            tenantResponse.getPatients())), "tenant");
            main.switchTo("tenant");
        }
    }

    private List<ConstraintViolation> getConstraintViolations() {
        TenantDetails details = new TenantDetails(name.getText(), email.getText(), username.getText(), phone.getText(),
                new String(password.getPassword()));
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(details);
        return violations;
    }

    private void validateTenant() {

    }

    private static TenantResponse readTenantResponse(HttpResponse<String> response) {
        TenantResponse tenantResponse;
        try {
            tenantResponse = MAPPER.readValue(response.body(), TenantResponse.class);
            return tenantResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String extractSessionId(HttpResponse<String> response) {
        return response.headers().firstValue("Set-Cookie").map(s -> s.split(";")[0]).orElseThrow();
    }

    static class TenantDetails {

        @NotNull
        @NotEmpty(message = "Name is required")
        private String name;

        @NotNull
        @NotEmpty(message = "Email is required")
        @Email(message = "Email is invalid")
        private String email;

        @NotNull
        @NotEmpty(message = "Username is required")
        private String username;

        private String phone;

        @NotNull
        @NotEmpty(message = "Password is required")
        private String password;

        public TenantDetails(String name, String email, String username, String phone, String password) {
            this.name = name;
            this.email = email;
            this.username = username;
            this.phone = phone;
            this.password = password;
        }
    }

}
