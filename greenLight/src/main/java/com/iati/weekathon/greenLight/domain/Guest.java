package com.iati.weekathon.greenLight.domain;


import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Liron on 27/04/2014.
 */
@Entity
@Table(name = "guests",
        indexes = {@Index(name = "phoneNumber_idx", columnList = "phoneNumber", unique = true)})
public class Guest {

    @Id
    @GeneratedValue
    private long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    @Transient
    private String base64img;

    @Lob
    @Basic(fetch=FetchType.LAZY)   //TODO: this is not working, the blob is always fetched, but maybe it's good for UI
    private byte[] picture;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getId() {
        return id;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBase64img() {
        return base64img;
    }

    public void setBase64img(String base64img) {
        this.base64img = base64img;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


    @JsonIgnore
    public String getFullName(){
        return firstName+" "+lastName;
    }
}
