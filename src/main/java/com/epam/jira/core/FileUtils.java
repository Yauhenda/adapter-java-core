package com.epam.jira.core;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class FileUtils {

    private FileUtils() {
    }

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());
    private static final String TARGET_DIR = "\\target\\";
    private static final String ATTACHMENTS_DIR = TARGET_DIR + "attachments\\";
    private static final String EXCEPTION_FILE_PATH = "stacktrace_%s.txt";
    private static final String CLASS_NAME = "FilUtils";
    private static final String ERROR_SAVING_FILE = "Error saving file: ";

    static String saveException(Throwable throwable) {
        String filePath = String.format(EXCEPTION_FILE_PATH, LocalDateTime.now().toString().replace(":", "-"));
        return writeStackTrace(throwable, filePath);
    }

    private static String writeStackTrace(Throwable throwable, String filePath) {
        String message = "";
        try {
            File temp = File.createTempFile("stacktrace", ".tmp");
            Path tempPath = Paths.get(temp.getPath());
            try (BufferedWriter out = new BufferedWriter(new FileWriter(temp))) {
                out.write(StringEscapeUtils.escapeJson(ExceptionUtils.getStackTrace(throwable)));
            }
            message = saveFile(temp, filePath);
            LOGGER.log(Level.INFO, () -> "Save file with name:" + filePath);
            Files.delete(tempPath);
        } catch (IOException e) {
            LOGGER.logp(Level.SEVERE, CLASS_NAME, "writeStackTrace(Throwable throwable, String filePath)", ERROR_SAVING_FILE + filePath, e);
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
                    : filePath.replace(currentDir, "");
        } catch (IOException e) {
            LOGGER.logp(Level.SEVERE, CLASS_NAME, "saveFile(File file)", ERROR_SAVING_FILE + file.getPath(), e);
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
            LOGGER.logp(Level.SEVERE, CLASS_NAME, "saveFile(File file, String newFilePath)", ERROR_SAVING_FILE + newFilePath, e);
            return null;
        }
    }

    static void writeXml(Issues issues, String filePath) {
        File configFile = new File("." + TARGET_DIR + filePath);
        List<Issue> issuesList = issues.getIssues();
        LOGGER.log(Level.INFO, () -> "Save XML file with name:" + filePath);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Issues.class);
            if (configFile.exists() && configFile.isFile()) {
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                Issues issuesExisting = (Issues) unmarshaller.unmarshal(configFile);
                issuesList = new ArrayList<>(issuesExisting.getIssues());
                issuesExisting.setIssues(issuesList);
            }
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(new Issues(issuesList), configFile);
        } catch (JAXBException e) {
            LOGGER.logp(Level.SEVERE, CLASS_NAME, "writeXml(Issues issues, String filePath)", "Error writing XML: " + filePath, e);
        }
    }
}