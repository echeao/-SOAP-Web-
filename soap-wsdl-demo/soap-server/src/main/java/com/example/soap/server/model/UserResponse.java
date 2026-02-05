package com.example.soap.server.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "userResponse")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserResponse")
public class UserResponse {

    @XmlElement(name = "message")
    private String message;

    public UserResponse() {
    }

    public UserResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
