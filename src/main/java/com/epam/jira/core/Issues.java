package com.epam.jira.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "tests")
@XmlAccessorType(XmlAccessType.FIELD)
class Issues {

    public Issues() {
    }

    Issues(List<Issue> issueList) {
        this.issueList = issueList;
    }

    @XmlElement(name = "test")
    private List<Issue> issueList;

    void addIssue(Issue issue) {
        if (issueList == null) {
            issueList = new ArrayList<>();
        }
        this.issueList.add(issue);
    }

    List<Issue> getIssues() {
        return issueList;
    }

    void setIssues(List<Issue> issueList) {
        this.issueList = issueList;
    }
}