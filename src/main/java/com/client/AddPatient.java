package com.client;

import com.client.api.response.PatientResponse;
import com.client.components.*;
import com.client.service.BackendCommunicator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.miginfocom.swing.MigLayout;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotEqual;
import net.sf.oval.constraint.NotNull;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.net.http.HttpResponse;

import static com.client.components.AudioRecorder.captureAudioToFile;

public class AddPatient extends JPanel {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private String sessionId;

    private String username;

    private String transcription;

    private BackendCommunicator backendCommunicator;

    private PlaceholderTextField firstName;

    private PlaceholderTextField lastName;

    private PlaceholderTextAreaPane transcriptionTextArea;

    private PlaceholderTextAreaPane clinicalNoteTextArea;

    private CustomButton recordButton;

    private CustomButton browseButton;

    private JButton backButton;

    private CustomButton summaryButton;

    private CustomButton saveButton;

    private JButton logoutButton;

    public AddPatient(Main main, String sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        this.backendCommunicator = new BackendCommunicator();

        setLayout(new MigLayout("insets 10, fillx", "[grow][]", "[]"));
        firstName = new PlaceholderTextField("First Name");
        firstName.setColumns(10);
        lastName = new PlaceholderTextField("Last Name");
        lastName.setColumns(10);

        clinicalNoteTextArea = new PlaceholderTextAreaPane("Clinical Notes");
        clinicalNoteTextArea.setEditable(false);
        clinicalNoteTextArea.setPreferredSize(new Dimension(400, 350));
        JScrollPane summaryScrollPane = new JScrollPane(clinicalNoteTextArea);
        summaryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        summaryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        transcriptionTextArea = new PlaceholderTextAreaPane("Transcription");
        transcriptionTextArea.setEditable(false);
        transcriptionTextArea.setPreferredSize(new Dimension(400, 350));
        JScrollPane transcriptionScrollPane = new JScrollPane(transcriptionTextArea);
        transcriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        transcriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        recordButton = new CustomButton("Record", 16);
        browseButton = new CustomButton("Browse", 16);
        backButton = new BackButton();
        saveButton = new CustomButton("Save", 16);
        summaryButton = new CustomButton("Summarize", 16);
        logoutButton = new LogoutButton();

        add(backButton, "align left");
        add(logoutButton, "align right, wrap");
        add(firstName, "split 2, align center, gapright 20");
        add(lastName, "align center, wrap");
        add(transcriptionScrollPane, "span, grow, wrap");
        add(summaryScrollPane, "span, grow, wrap");

        JPanel buttonsPanel = new JPanel(new MigLayout("insets 15, gap 20 15", "[grow, fill]",
                "[grow, fill]"));
        add(buttonsPanel, "span, grow, wrap");
        buttonsPanel.add(browseButton, "grow");
        buttonsPanel.add(recordButton, "grow, wrap");
        buttonsPanel.add(summaryButton, "grow");
        buttonsPanel.add(saveButton, "grow");

        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.showOpenDialog(this);
            transcription = backendCommunicator.transcribe(fileChooser.getSelectedFile(), this.sessionId, this.username)
                    .body();
            transcriptionTextArea.setText(transcription);
        });

        backButton.addActionListener(e -> {
            try {
                main.removeTopPanel();
                main.switchTo("tenant");
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

        recordButton.addActionListener(e -> {
            String filepath = "src/main/resources/record.wav";
            startRecording(filepath);
            int result = JOptionPane.showConfirmDialog(null, "Recording...", "Recording",
                    JOptionPane.OK_CANCEL_OPTION);

            if (result != 0) {
                deleteCapturedAudio(filepath);
            } else {
                stopRecording();
                transcription = backendCommunicator.transcribe(new File(filepath), this.sessionId, this.username).body();
                transcriptionTextArea.setText(transcription);
            }
        });

        summaryButton.addActionListener(e -> {
            if (StringUtils.isNotEmpty(transcriptionTextArea.getText())) {
                String summary = backendCommunicator.summarize(transcriptionTextArea.getText(), this.sessionId,
                        this.username).body();
                clinicalNoteTextArea.setMarkdownText(summary);
            } else {
                JOptionPane.showMessageDialog(null, "Please transcribe audio first");
            }
        });

        saveButton.addActionListener(e -> {
            if (StringUtils.isNotEmpty(firstName.getText()) && StringUtils.isNotEmpty(lastName.getText())) {
                HttpResponse<String> response;
                response = backendCommunicator.addPatientAndAttachSummary(firstName.getText(), lastName.getText(),
                        clinicalNoteTextArea.getText(), this.sessionId, this.username);
                PatientResponse patientResponse = readPatientResponse(response);
                JOptionPane.showMessageDialog(null, "Patient added successfully");
                main.updateCardPanel(new Patient(main, sessionId, username, patientResponse.getPatientId(), patientResponse.getFirstName(),
                        patientResponse.getLastName(), clinicalNoteTextArea.getText()), "patient");
                main.switchTo("patient");
            } else {
                JOptionPane.showMessageDialog(null, "Please enter first name and last name");
            }
        });
    }

    private PatientResponse readPatientResponse(HttpResponse<String> response) {
        try {
            return MAPPER.readValue(response.body(), PatientResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteCapturedAudio(String filepath) {
        stopRecording();
        AudioRecorder.deleteCapturedAudio(filepath);
    }

    private void stopRecording() {
        AudioRecorder.stopRecording();
    }

    private void startRecording(String filepath) {
        captureAudioToFile(filepath);
    }

    static class PatientDetails {
        @NotNull
        @NotEmpty(message = "First name is required")
        private String firstName;

        @NotNull
        @NotEmpty(message = "Last name is required")
        private String lastName;

        @NotNull
        @NotEmpty(message = "Provide an audio file to transcribe")
        private String transcription;

        @NotNull
        @NotEmpty(message = "Summarize the transcription")
        @NotEqual(value = "Clinical Notes", message = "Summarize the transcription")
        private String clinicalNote;
    }
}
