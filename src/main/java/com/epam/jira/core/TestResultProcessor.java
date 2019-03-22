package com.epam.jira.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestResultProcessor {

    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());

    private static final String XML_NAME = "jira-tm-report.xml";

    private static Issue issue;
    private static Issues issues = new Issues();

    private TestResultProcessor() {
    }

    public static void startJiraAnnotatedTest(String jiraKey) {
        addIssue();
        issue = new Issue();
        issue.setIssueKey(jiraKey);
    }

    public static void setStatus(String status) {
        issue.setStatus(status);
    }

    public static void addException(Throwable throwable) {
        String filePath = FileUtils.saveException(throwable);

        String exceptionMessage = throwable.getMessage();
        if (exceptionMessage != null && exceptionMessage.contains("\n")) {
            exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf('\n'));
        }
        String message = "Failed due to: " + throwable.getClass().getName() + ": " + exceptionMessage
                + ".\nFull stack trace attached as " + Paths.get(filePath).getFileName();
        issue.addAttachment(filePath);

        addToSummary(message);
    }

    public static void addToSummary(String toAdd) {
        issue.addToSummary(toAdd);
    }

    public static void addParameter(String title, String value) {
        issue.addParameter(new Parameter(title, value));
    }

    public static void addAttachment(File attachment) {
        String savedFile = FileUtils.saveFile(attachment);
        issue.addAttachment(savedFile);
        issue.addToSummary("Attachment added: " + Paths.get(savedFile).getFileName());
    }

    public static void setTime(String time) {
        issue.setTime(time);
    }

    public static void saveResults() {
        addIssue();
        LOGGER.log(Level.INFO, () -> "Save results of " + issues.getIssues().size() + " tests to " + XML_NAME + ".");
        FileUtils.writeXml(issues, XML_NAME);
    }

    private static void addIssue() {
        if (issue != null) {
            issues.addIssue(issue);
        }
    }
}
