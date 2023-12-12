package com.client.api.request;

public class SummarizedSpeechRequest {

    private String transcript;

    public SummarizedSpeechRequest(String transcript) {
        this.transcript = transcript;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }
}
