package com.client;

import com.client.components.CustomButton;
import com.client.components.MoreIcon;
import com.client.components.PlaceholderTextField;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PatientSummary extends JPanel {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String sessionId;

    private String username;

    private String patientId;

    private String firstName;

    private String lastName;

    private String summary;

    private boolean hover;

    private PlaceholderTextField firstNameField;

    private PlaceholderTextField lastNameField;

    private JButton viewPatientButton;

    public PatientSummary(String sessionId, String username, String patientId, String firstName, String lastName,
                          String summary) {
        this.sessionId = sessionId;
        this.username = username;
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.hover = false;

        setLayout(new MigLayout());

        firstNameField = new PlaceholderTextField(firstName);
        lastNameField = new PlaceholderTextField(lastName);
        firstNameField.setColumns(20);
        lastNameField.setColumns(20);

        firstNameField.setFocusable(false);
        lastNameField.setFocusable(false);
        firstNameField.setEditable(false);
        lastNameField.setEditable(false);

        add(firstNameField, "width 100::, growx, split 2");
        add(lastNameField, "width 100::, growx, wrap");

        this.viewPatientButton = new MoreIcon();
        add(this.viewPatientButton, "align center");
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
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
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBorder(BorderFactory.createLineBorder(Color.WHITE));
                setForeground(Color.LIGHT_GRAY);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        super.paintComponent(g2);
        Color color = hover ? Color.LIGHT_GRAY : Color.WHITE;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setBackground(color); // Button color
        g2.dispose();
    }
}
