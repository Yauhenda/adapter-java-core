package com.epam.jira.core;

import org.apache.commons.text.StringEscapeUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
class Parameter {

    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "value")
    private String value;

    Parameter() {
    }

    Parameter(String title, String value) {
        setTitle(title);
        setValue(value);
    }

    String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = StringEscapeUtils.escapeJson(title);
    }

    String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = StringEscapeUtils.escapeJson(value);
    }
}