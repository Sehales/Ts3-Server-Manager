package net.sehales.ts3sm.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import net.sehales.ts3_japi.APIResponse;
import net.sehales.ts3_japi.ServerQueryAPI;
import net.sehales.ts3_japi.command.CmdServerInfo;
import net.sehales.ts3_japi.wrapper.Server;
import net.sehales.ts3_japi.wrapper.ServerInfo;
import net.sehales.ts3sm.ServerManager;
import net.sehales.ts3sm.factory.ServerListAutostartCellFactory;
import net.sehales.ts3sm.factory.ServerListNameCellFactory;
import net.sehales.ts3sm.factory.ServerListNameValueFactory;
import net.sehales.ts3sm.factory.ServerListSlotsValueFactory;
import net.sehales.ts3sm.factory.ServerListUptimeValueFactory;
import net.sehales.ts3sm.util.DialogHelper;

public class ServerListTab extends RefreshableTab {

    @FXML
    private TableView<Server>            serverTable;

    @FXML
    private TableColumn<Server, String>  slotsColumn;

    @FXML
    private TableColumn<Server, Boolean> autostartColumn;

    @FXML
    private TableColumn<Server, Server>  nameColumn;

    @FXML
    private TableColumn<Server, String>  uptimeColumn;

    @FXML
    private TextField                    serverNameField;

    @FXML
    private TextField                    slotsField;

    @FXML
    private TextField                    portField;

    @FXML
    private CheckBox                     autostartCheckBox;

    private ServerManager                srv;
    private ServerQueryAPI               api;

    public ServerListTab(String name, ServerManager srv) throws IOException {
        super(name);
        this.srv = srv;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/sehales/ts3sm/view/ServerList.fxml"), srv.getLang());
        loader.setController(this);
        setContent(new AnchorPane());
        loader.setRoot(getContent());
        loader.load();

        slotsColumn.setCellValueFactory(new ServerListSlotsValueFactory());
        autostartColumn.setCellFactory(new ServerListAutostartCellFactory());
        nameColumn.setCellValueFactory(new ServerListNameValueFactory());
        nameColumn.setCellFactory(new ServerListNameCellFactory());
        uptimeColumn.setCellValueFactory(new ServerListUptimeValueFactory());

        serverTable.setOnMousePressed((event) -> {
            if (event.isPrimaryButtonDown() && (event.getClickCount() == 2)) {
                onSelectServerAction(event);
            }
        });
    }

    @FXML
    private void onCreateAction(ActionEvent event) {
        // TODO api.serverCreate(serverNameField.getText(), port.getText(), slots, autostart)
    }

    @FXML
    private void onDeleteServerAction(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onExitAction(ActionEvent event) {
        if (srv.hasServerSelected()) {
            if (api.use(0, true).isAnsweredAndOk()) {
                srv.setSelectedServer(null);
            } else {
                DialogHelper.showLastCommandError(srv);
            }
        }
    }

    @FXML
    private void onRefreshAction(ActionEvent event) {
        System.out.println(event.getTarget());
        refresh();
    }

    @FXML
    private void onSelectServerAction(Event event) {
        Server server = serverTable.getSelectionModel().getSelectedItem();
        if (server != null) {
            if (api.use(server.getId(), true).isAnsweredAndOk()) {
                srv.setSelectedServer(server);
            } else {
                DialogHelper.showLastCommandError(srv);
            }
        }
    }

    @FXML
    private void onSelectServerByIdAction(ActionEvent event) {
        Optional<Integer> id = DialogHelper.showNumberInputDialogAndWait(srv.lang("serverlist.dialog.selectbyidtitle"), Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @FXML
    private void onSelectServerByPortAction(ActionEvent event) {
        Optional<Integer> port = DialogHelper.showNumberInputDialogAndWait(srv.lang("serverlist.dialog.selectbyporttitle"), 65535, 5);
        if (port.isPresent()) {
            if (api.useByPort(port.get(), false).isAnsweredAndOk()) {
                APIResponse<CmdServerInfo, ServerInfo> res = srv.api().serverInfo();
                if (res.isAnsweredAndOk()) {
                    srv.setSelectedServer(res.getWrapper());
                } else {
                    DialogHelper.showLastCommandError(srv);
                }
            } else {
                DialogHelper.showLastCommandError(srv);
            }
        }
    }

    @FXML
    private void onStartServerAction(ActionEvent event) {
        // TODO
    }

    @FXML
    private void onStopServerAction(ActionEvent event) {
        // TODO
    }

    @Override
    public void refresh() {
        api = srv.api();
        serverTable.setItems(api.serverList().getWrapper());
    }
}
