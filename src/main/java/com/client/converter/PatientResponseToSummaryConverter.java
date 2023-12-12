package com.client.converter;

import com.client.PatientSummary;
import com.client.api.response.PatientResponse;

import java.util.Set;
import java.util.stream.Collectors;

public class PatientResponseToSummaryConverter {

    public static PatientSummary convert(String sessionId, String username, PatientResponse patientResponse) {
        return new PatientSummary(sessionId, username, patientResponse.getPatientId(),
                patientResponse.getFirstName(), patientResponse.getLastName(), patientResponse.getSummary());
    }

    public static Set<PatientSummary> convertAll(String sessionId, String username, Set<PatientResponse> patientResponses) {
        return patientResponses.stream().map(p -> convert(sessionId, username, p))
                .collect(Collectors.toSet());
    }
}
