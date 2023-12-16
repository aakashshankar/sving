package com.client;

import com.client.api.response.PatientResponse;
import com.client.components.*;
import com.client.converter.PatientResponseToSummaryConverter;
import com.client.model.Attribute;
import com.client.service.BackendCommunicator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.net.http.HttpResponse;
import java.util.Set;

import static com.client.converter.PatientResponseToSummaryConverter.convertAll;

public class Patient extends JPanel {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String sessionId;

    private String username;

    private String patientId;

    private String firstName;

    private String lastName;

    private String summary;

    private BackendCommunicator backendCommunicator;

    private JButton backButton;

    private JButton deleteButton;

    private JButton logoutButton;

    private PlaceholderTextField firstNameField;

    private PlaceholderTextField lastNameField;

    private PlaceholderTextAreaPane summaryTextArea;

    public Patient(Main main, String sessionId, String username,
                   String patientId, String firstName, String lastName, String summary) {
        this.sessionId = sessionId;
        this.username = username;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        backendCommunicator = new BackendCommunicator();


        setLayout(new MigLayout("insets 10", "[grow][]", "[]"));

        firstNameField = new PlaceholderTextField(firstName);
        lastNameField = new PlaceholderTextField(lastName);
        summaryTextArea = new PlaceholderTextAreaPane(summary);
        firstNameField.setFocusable(false);
        lastNameField.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(summaryTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        firstNameField.setEditable(false);
        lastNameField.setEditable(false);
        summaryTextArea.setEditable(false);

        firstNameField.setColumns(20);
        lastNameField.setColumns(20);

        backButton = new BackButton();
        backButton.addActionListener(e -> {
            try {
                Set<PatientResponse> patientResponses =
                        readPatientResponses(backendCommunicator.getAllForTenant(this.sessionId, this.username));
                main.updateCardPanel(new Tenant(main, this.sessionId, this.username, convertAll(this.sessionId, this.username,
                        patientResponses)), "tenant");
                main.switchTo("tenant");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        deleteButton = new DeleteButton();
        deleteButton.addActionListener(e -> {
            try {
                HttpResponse<String> response = backendCommunicator.deletePatient(this.sessionId, this.username, this.patientId);
                if (response.statusCode() == 200) {
                    Set<PatientResponse> patientResponses =
                            readPatientResponses(backendCommunicator.getAllForTenant(this.sessionId, this.username));
                    main.updateCardPanel(new Tenant(main, this.sessionId, this.username, convertAll(this.sessionId, this.username,
                            patientResponses)), "tenant");
                    main.switchTo("tenant");
                } else {
                    JOptionPane.showMessageDialog(null, "Error deleting patient");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        logoutButton = new LogoutButton();
        logoutButton.addActionListener(e -> {
            try {
                main.updateCardPanel(new Login(main), "login");
                main.switchTo("login");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        add(backButton, "align left");
        add(deleteButton, "align right");
        add(logoutButton, "align right, wrap");

        JPanel names = new JPanel(new MigLayout("insets 10", "[]", "[grow, fill]"));
        names.add(firstNameField, "split 2, align center");
        names.add(lastNameField, "align center, wrap");
        add(names, "span, align center, wrap");
        add(scrollPane, "span, grow, wrap");
    }

    private Set<PatientResponse> readPatientResponses(HttpResponse<String> allForTenant) {
        try {
            return MAPPER.readValue(allForTenant.body(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
