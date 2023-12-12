package com.client.components;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class AudioRecorder {

    private static TargetDataLine targetLine;

    private static AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 16;
        int channels = 1;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public static void captureAudioToFile(String filePath) {
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Line not supported");
                return;
            }

            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open(format);
            targetLine.start();

            Thread captureThread = new Thread(() -> {
                AudioInputStream audioStream = new AudioInputStream(targetLine);
                File audioFile = new File(filePath);

                try {
                    AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            captureThread.start();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
    }

    public static void stopRecording() {
        targetLine.stop();
        targetLine.close();
    }

    public static void deleteCapturedAudio(String filepath) {
        // implement
        Path path = Path.of(filepath);
        File file = path.toFile();

        boolean deleted = file.delete();
        if (!deleted) {
            System.err.println("Could not delete file");
        }
    }
}
