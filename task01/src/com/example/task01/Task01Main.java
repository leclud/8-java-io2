package com.example.task01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Task01Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(extractSoundName(new File("task01/src/main/resources/3727.mp3")));
    }

    public static String extractSoundName(File file) throws IOException, InterruptedException {
        String ffProbePath = "C:\\Users\\User\\Desktop\\ffmpeg-2024-11-28-git-bc991ca048-full_build\\bin\\ffprobe.exe";

        ProcessBuilder processBuilder = new ProcessBuilder(ffProbePath, "-v", "error", "-of", "flat", "-show_format", file.getAbsolutePath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        return parseTitle(readProcessOutput(process));
    }

    private static String readProcessOutput(Process process) throws IOException {
        String output = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output += line + "\n";
            }
        }
        return output;
    }

    private static String parseTitle(String output) {
        String titlePrefix = "format.tags.title=";
        for (String line : output.split("\n")) {
            if (line.contains(titlePrefix)) {
                return line.substring(line.indexOf(titlePrefix) + titlePrefix.length()).replace("\"", "").trim();
            }
        }
        return "Название не найдено";
    }
}
