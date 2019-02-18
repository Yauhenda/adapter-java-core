package com.epam.jira.core;

import org.apache.commons.text.StringEscapeUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "test")
@XmlAccessorType(XmlAccessType.FIELD)
class Issue {

    @XmlElement(name = "key", required = true)
    private String issueKey;

    @XmlElement(name = "status", required = true)
    private String status;

    @XmlElement(name = "summary")
    private String summary = "";

    @XmlElement(name = "time")
    private String time;

    @XmlElementWrapper(name = "attachments")
    @XmlElement(name = "attachment")
    private List<String> attachments;

    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    private List<Parameter> parameters;

    void setIssueKey(String issueKey) {
        this.issueKey = StringEscapeUtils.escapeJson(issueKey);
    }

    void setStatus(String status) {
        this.status = StringEscapeUtils.escapeJson(status);
    }

    void addToSummary(String summary) {
        this.summary = this.summary.concat(StringEscapeUtils.escapeJson(summary + "\n"));
    }

    void addParameter(Parameter parameter) {
        if (parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(parameter);
    }

    void addAttachment(String attachment) {
        if (attachments == null) {
            this.attachments = new ArrayList<>();
        }
        this.attachments.add(StringEscapeUtils.escapeJson("." + attachment));
    }

    void setTime(String time) {
        this.time = time;
    }

    String getSummary() {
        return summary;
    }
}