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
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.springframework.http.HttpStatus;

import javax.swing.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

public class Login extends JPanel {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PlaceholderTextField username;
    private PlaceholderPasswordField password;
    private JButton login;
    private JButton register;

    private BackendCommunicator backendCommunicator;

    public Login(Main main) {
        username = new PlaceholderTextField("Username");
        password = new PlaceholderPasswordField("Password");
        login = new CustomButton("Login", 15);
        register = new CustomButton("Register", 15);
        backendCommunicator = new BackendCommunicator();

        setLayout(new MigLayout("insets 10, fillx", "[grow][]", "[]"));
        main.setSize(350, 400);

        add(username, "span, growx, wrap, gapbottom 10");
        add(password, "span, growx, wrap, gapbottom 10");
        add(login, "align center, span, split 2, gapright 15");
        add(register, "align center");

        register.addActionListener(e -> {
            try {
                main.updateCardPanel(new Register(main), "register");
                main.switchTo("register");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        login.addActionListener(e -> {
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
            JOptionPane.showMessageDialog(null, violations.get(0).getMessage());
        } else {
            TenantResponse tenantResponse;
            HttpResponse<String> response = backendCommunicator.login(username.getText(),
                    new String(password.getPassword()));
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                String sessionId = extractSessionId(response);
                response = backendCommunicator.getTenant(sessionId, username.getText());
                tenantResponse = readTenantResponse(response);

                main.updateCardPanel(new Tenant(main, sessionId, username.getText(),
                        PatientResponseToSummaryConverter.convertAll(sessionId, tenantResponse.getUsername(),
                                tenantResponse.getPatients())), "tenant");
                main.switchTo("tenant");
            } else {
                JOptionPane.showMessageDialog(null, "Error occurred while logging in");
            }
        }
    }

    private List<ConstraintViolation> getConstraintViolations() {
        LoginDetails details = new LoginDetails(username.getText(), new String(password.getPassword()));
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(details);
        return violations;
    }

    private TenantResponse readTenantResponse(HttpResponse<String> response) {
        try {
            return MAPPER.readValue(response.body(), TenantResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractSessionId(HttpResponse<String> response) {
        return response.headers().firstValue("Set-Cookie").map(s -> s.split(";")[0]).orElseThrow();
    }

    static class LoginDetails {

        @NotNull
        @NotEmpty(message = "Username cannot be empty")
        private String username;

        @NotNull
        @NotEmpty(message = "Password cannot be empty")
        private String password;

        public LoginDetails(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
