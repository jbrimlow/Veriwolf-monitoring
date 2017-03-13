package com.google.firebase.quickstart.database.models;

/**
 * Created by john on 11/16/16.
 */
public class Worker {


    String lastname;
    String firstname;
    String gateScanId;
    Long id;
    boolean active;
    String contractor;
    String supervisor;
    String supervisorContact;

    public Worker() {
    }

    public Worker(String lastname, String firstname, String gateScanId, Long id, boolean active, String contractor, String supervisor, String supervisorContact) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.gateScanId = gateScanId;
        this.id = id;
        this.active = active;
        this.contractor = contractor;
        this.supervisor = supervisor;
        this.supervisorContact = supervisorContact;
    }

    public boolean isActive() {
        return active;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getContractor() {
        return contractor;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGateScanId() {
        return gateScanId;
    }

    public Long getId() {
        return id;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setGateScanId(String gateScanId) {
        this.gateScanId = gateScanId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setContractor(String contractor) {
        this.contractor = contractor;
    }
}
