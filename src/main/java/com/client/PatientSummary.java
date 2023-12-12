package com.client;

import com.client.components.CustomButton;
import com.client.components.PlaceholderTextField;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class PatientSummary extends JPanel {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String sessionId;

    private String username;

    private String patientId;

    private String firstName;

    private String lastName;

    private String summary;

    private PlaceholderTextField firstNameField;

    private PlaceholderTextField lastNameField;

    private CustomButton viewPatientButton;

    public PatientSummary(String sessionId, String username, String patientId, String firstName, String lastName,
                          String summary) {
        this.sessionId = sessionId;
        this.username = username;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;

        setLayout(new MigLayout());

        firstNameField = new PlaceholderTextField(firstName);
        lastNameField = new PlaceholderTextField(lastName);
        firstNameField.setColumns(20);
        lastNameField.setColumns(20);

        firstNameField.setFocusable(false);
        lastNameField.setFocusable(false);
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);

        add(firstNameField);
        add(lastNameField, "wrap");

        this.viewPatientButton = new CustomButton("View Patient", 10);
        add(this.viewPatientButton, "align center, span, gapright 15");

        viewPatientButton.addActionListener(e -> {
            try {
                Main main = (Main) SwingUtilities.getWindowAncestor(this);
                main.updateCardPanel(new Patient(main, this.sessionId, this.username, this.patientId, this.firstName,
                        this.lastName, this.summary), "patient");
                main.switchTo("patient");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

}
