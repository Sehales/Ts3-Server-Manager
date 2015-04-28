package net.sehales.ts3sm.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import net.sehales.ts3sm.ServerManager;

public class InstanceInfoTab extends RefreshableTab {
    private ServerManager srv;

    public InstanceInfoTab(String name, ServerManager srv) throws IOException {
        super(name);
        this.srv = srv;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/sehales/ts3sm/view/InstanceInfo.fxml"), srv.getLang());
        loader.setController(this);
        setContent(new AnchorPane());
        loader.setRoot(getContent());
        loader.load();
    }

    @Override
    public void refresh() {
        System.out.println("InstanceInfoTab");
    }

}
