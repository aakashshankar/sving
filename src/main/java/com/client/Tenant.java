package com.client;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Collection;
import java.util.Set;

public class Tenant extends JPanel {

    private String sessionId;

    private String username;

    private Set<PatientSummary> patientSummaries;

    private JButton addPatientButton;

    private JButton logoutButton;

    public Tenant(Main main, String sessionId, String username, Collection<PatientSummary> patientSummaries) {
        this.sessionId = sessionId;
        this.username = username;
        this.patientSummaries = (Set<PatientSummary>) patientSummaries;
        main.setSize(700, 500);
        setLayout(new MigLayout("insets 10", "[grow][]", "[]"));

        addPatientButton = new JButton("Add Patient");
        logoutButton = new JButton("Logout");

        add(addPatientButton, "split 2, span, align right");
        add(logoutButton, "align right, wrap");
        int i = 0;
        for (var s : patientSummaries) {
            s.setBorder(BorderFactory.createDashedBorder(null, 1, 5, 5, false));
            add(s, "width 100:300:500, height 100:100:150" + (i % 3 == 0 ? ", wrap" : ""));
            i++;
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
