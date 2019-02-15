package com.epam.jira.core;

import java.io.File;

public class TestResultProcessor {

    private static Issue issue;
    private static Issues issues = new Issues();

    private TestResultProcessor() {}

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
        if (exceptionMessage.contains("\n")) {
            exceptionMessage = exceptionMessage.substring(0, exceptionMessage.indexOf('\n'));
        }
        String message = "Failed due to: " + throwable.getClass().getName() + ": " + exceptionMessage
                + ".\nFull stack trace attached as " + filePath;
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
        issue.addToSummary("Attachment added: " + savedFile);
    }

    public static void setTime(String time) {
        issue.setTime(time);
    }

    public static void saveResults() {
        addIssue();
        FileUtils.writeXml(issues,"jira-tm-report.xml" );
    }

    private static void addIssue() {
        if (issue != null) {
            issues.addIssue(issue);
        }
    }
}
