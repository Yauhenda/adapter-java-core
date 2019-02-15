package com.epam.jira.core;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

class FileUtils {

    private FileUtils() {
    }

    private static final String TARGET_DIR = "\\target\\";
    private static final String ATTACHMENTS_DIR = TARGET_DIR + "attachments\\";

    static String saveException(Throwable throwable) {

        String filePath = String.format("stacktrace_%s.txt", LocalDateTime.now().toString().replace(":", "-"));
        return writeStackTrace(throwable, filePath);
    }

    private static String writeStackTrace(Throwable throwable, String filePath) {
        String message = "";

        try {
            File file = File.createTempFile("stack", "tmp");
            BufferedWriter out;
            try (FileWriter fileWriter = new FileWriter(file)) {
                out = new BufferedWriter(fileWriter);
            }
            out.write(ExceptionUtils.getStackTrace(throwable));
            message = saveFile(file, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    static String saveFile(File file) {
        String currentDir = System.getProperty("user.dir");
        String targetFilePath = null;
        try {
            String filePath = file.getCanonicalPath();
            boolean placedOutOfTargetDir = !filePath.startsWith(currentDir + TARGET_DIR);
            targetFilePath = placedOutOfTargetDir
                    ? saveFile(file, file.getName())
                    : filePath.replaceFirst(currentDir, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFilePath;
    }

    private static String saveFile(File file, String newFilePath) {
        try {
            String relativeFilePath = ATTACHMENTS_DIR;
            File copy = new File("." + relativeFilePath + newFilePath);
            if (copy.exists()) {
                relativeFilePath += System.nanoTime() + "\\";
                copy = new File("." + relativeFilePath + newFilePath);
            }
            org.apache.commons.io.FileUtils.copyFile(file, copy);
            return relativeFilePath + newFilePath;
        } catch (IOException e) {
            return null;
        }
    }

    static void writeXml(Issues issues, String filePath) {
        try {
            JAXBContext jaxbCtx = JAXBContext.newInstance(Issues.class);
            Marshaller marshaller = jaxbCtx.createMarshaller();
            marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(issues, new File("." + TARGET_DIR + filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}