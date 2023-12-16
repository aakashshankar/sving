package com.client;

import com.client.components.CustomButton;
import com.client.components.LogoutButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Set;

public class Tenant extends JPanel {

    private String sessionId;

    private String username;

    private Set<PatientSummary> patientSummaries;

    private JButton addPatientButton;

    private JButton plusButton;

    private JButton logoutButton;

    public Tenant(Main main, String sessionId, String username, Collection<PatientSummary> patientSummaries) {
        this.sessionId = sessionId;
        this.username = username;
        this.patientSummaries = (Set<PatientSummary>) patientSummaries;
        main.setSize(800, 700);
        setLayout(new MigLayout("insets 10", "[grow][]", "[]"));

        addPatientButton = new CustomButton("Add Patient", 16);
        logoutButton = new LogoutButton();

        ImageIcon plusIcon = new ImageIcon("src/main/resources/plus-button.png");
        plusButton = new JButton(plusIcon);
        JPanel plusPanel = new JPanel(new MigLayout("fill, insets 0", "[center]", "[center]"));
        plusPanel.add(plusButton);
        plusButton.addActionListener(e -> {
            try {
                main.updateCardPanel(new AddPatient(main, sessionId, username), "addpatient");
                main.switchTo("addpatient");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        add(plusPanel, "align center, gapbottom 10");
        add(logoutButton, "align right, wrap");

        JPanel container = new JPanel(new MigLayout("wrap 3, insets 0, gap 0"));
        for (PatientSummary patientSummary : patientSummaries) {
            container.add(patientSummary, "align center, w 200, h 70");
        }
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        if (!patientSummaries.isEmpty()) {
            add(scrollPane, "span, grow, wrap");
        }
        addPatientButton.addActionListener(e -> {
            try {
                main.updateCardPanel(new AddPatient(main, sessionId, username), "addpatient");
                main.switchTo("addpatient");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        logoutButton.addActionListener(e -> {
            try {
                main.updateCardPanel(new Login(main), "login");
                main.switchTo("login");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

    }
}
