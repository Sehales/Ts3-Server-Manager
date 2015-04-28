package net.sehales.ts3sm.controller;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.controlsfx.glyphfont.FontAwesome;

import net.sehales.ts3sm.ServerManager;
import net.sehales.ts3sm.util.Util;

public class MainController {
    @FXML
    private TabPane        tabs;

    private ResourceBundle lang;
    private ServerManager  srv;

    public void initTabs(ServerManager srv) throws IOException {
        this.srv = srv;
        lang = srv.getLang();
        loadStartTab();
        loadInstanceInfoTab();
        loadServerListTab();

        tabs.getSelectionModel().selectedItemProperty().addListener((value, oldValue, newValue) -> {
            Util.refreshTab(newValue);
        });

        // whenever we get logged in, the instance info and server list tabs will get enabled
        srv.loggedInProperty().addListener((value, oldValue, newValue) -> {
            tabs.getTabs().get(1).setDisable(oldValue);
            tabs.getTabs().get(2).setDisable(oldValue);
        });

        // disable all tabs except the start tab
        for (int i = 1; i < tabs.getTabs().size(); i++) {
            tabs.getTabs().get(i).setDisable(true);
        }
    }

    private void loadInstanceInfoTab() throws IOException {
        InstanceInfoTab instanceInfo = new InstanceInfoTab(lang.getString("main.tab.instanceinfo"), srv);
        instanceInfo.setGraphic(Util.getTabFANode(FontAwesome.Glyph.DASHBOARD));
        tabs.getTabs().add(instanceInfo);
    }

    private void loadServerListTab() throws IOException {
        ServerListTab serverList = new ServerListTab(lang.getString("main.tab.serverlist"), srv);
        serverList.setGraphic(Util.getTabFANode(FontAwesome.Glyph.TH_LIST));
        tabs.getTabs().add(serverList);
    }

    private void loadStartTab() throws IOException {
        StartTab start = new StartTab(lang.getString("main.tab.start"), srv);
        start.setGraphic(Util.getTabFANode(FontAwesome.Glyph.HOME));
        tabs.getTabs().add(start);
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.F5) {
            Tab tab = tabs.getSelectionModel().getSelectedItem();
            Util.refreshTab(tab);
        }
    }

}
