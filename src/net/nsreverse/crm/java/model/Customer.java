package net.nsreverse.crm.java.model;

public class Customer {
    private String _id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String[] interactions;
    private String[] alerts;
    private int[] entitlements;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getInteractions() {
        return interactions;
    }

    public void setInteractions(String[] interactions) {
        this.interactions = interactions;
    }

    public String[] getAlerts() {
        return alerts;
    }

    public void setAlerts(String[] alerts) {
        this.alerts = alerts;
    }

    public int[] getEntitlements() {
        return entitlements;
    }

    public void setEntitlements(int[] entitlements) {
        this.entitlements = entitlements;
    }


}
