package net.sehales.ts3sm.config;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ConnectionInfo {
    private StringProperty name     = new SimpleStringProperty();
    private StringProperty address  = new SimpleStringProperty();
    private StringProperty port     = new SimpleStringProperty();
    private StringProperty username = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();

    public ConnectionInfo(String name, String address, String port, String username, String password) {
        setName(name);
        setAddress(address);
        setPort(port);
        setUsername(username);
        setPassword(password);
    }

    public StringProperty addressProperty() {
        return address;
    }

    public String getAddress() {
        return addressProperty().getValue();
    }

    public java.lang.String getName() {
        return nameProperty().getValue();
    }

    public java.lang.String getPassword() {
        return passwordProperty().getValue();
    }

    public String getPort() {
        return portProperty().getValue();
    }

    public String getUsername() {
        return usernameProperty().getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty portProperty() {
        return port;
    }

    public void setAddress(final String address) {
        addressProperty().setValue(address);
    }

    public void setName(final java.lang.String name) {
        nameProperty().setValue(name);
    }

    public void setPassword(final java.lang.String password) {
        passwordProperty().setValue(password);
    }

    public void setPort(final String port) {
        portProperty().setValue(port);
    }

    public void setUsername(final String username) {
        usernameProperty().setValue(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

}
