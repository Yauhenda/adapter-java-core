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

    @XmlElement(name = "test")
    private List<Issue> issueList = new ArrayList<>();

    void addIssue(Issue issue) {
        this.issueList.add(issue);
    }
}