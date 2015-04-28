package net.sehales.ts3sm.controller;

import java.io.IOException;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import net.sehales.ts3_japi.exception.QueryException;
import net.sehales.ts3sm.ServerManager;
import net.sehales.ts3sm.config.ConnectionInfo;
import net.sehales.ts3sm.config.ConnectionStorage;
import net.sehales.ts3sm.util.DialogHelper;

public class StartTab extends RefreshableTab {
    @FXML
    private TextField                    usernameField;

    @FXML
    private Button                       logoutBtn;

    @FXML
    private Button                       loginBtn;

    @FXML
    private PasswordField                passwordField;

    @FXML
    private TextField                    addressField;

    @FXML
    private TextField                    portField;

    @FXML
    private Label                        infoLabel;

    @FXML
    private Button                       saveBtn;

    @FXML
    private Button                       loadBtn;

    @FXML
    private Button                       deleteBtn;

    @FXML
    private TableView<ConnectionInfo>    favTable;

    private ServerManager                srv;

    private ListProperty<ConnectionInfo> connections = new SimpleListProperty<>(FXCollections.observableArrayList());

    public StartTab(String name, ServerManager srv) throws IOException {
        super(name);
        this.srv = srv;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/sehales/ts3sm/view/Start.fxml"), srv.getLang());
        loader.setController(this);
        setContent(new AnchorPane());
        loader.setRoot(getContent());
        loader.load();

        GlyphFont fa = GlyphFontRegistry.font("FontAwesome");
        saveBtn.setGraphic(fa.create(FontAwesome.Glyph.SAVE).color(Color.DARKCYAN).size(16));
        loadBtn.setGraphic(fa.create(FontAwesome.Glyph.FOLDER_OPEN_ALT).color(Color.DARKCYAN).size(16));
        deleteBtn.setGraphic(fa.create(FontAwesome.Glyph.TIMES).color(Color.DARKCYAN).size(16));
        // --login and logout field validation and button disabling
        BooleanExpression expr = addressField.textProperty().isEmpty()
                        .or(portField.textProperty().isEmpty())
                        .or(usernameField.textProperty().isEmpty())
                        .or(passwordField.textProperty().isEmpty());

        loginBtn.disableProperty().bind(expr.or(srv.loggedInProperty()));
        logoutBtn.disableProperty().bind(srv.loggedInProperty().isNotEqualTo(new SimpleBooleanProperty(true)).or(expr));

        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((!newValue.matches("\\d+") && !newValue.isEmpty()) || (newValue.length() > 5)) {
                StringProperty sp = (StringProperty) observable;
                sp.setValue(oldValue);
            }
        });

        // --saved connection management
        favTable.setItems(connections.get());
        saveBtn.disableProperty().bind(
                        addressField.textProperty().isEmpty()
                                        .or(portField.textProperty().isEmpty())
                                        .or(usernameField.textProperty().isEmpty()));

        loadBtn.disableProperty().bind(connections.sizeProperty().isEqualTo(0));
        deleteBtn.disableProperty().bind(favTable.getSelectionModel().selectedItemProperty().isNull());
        final ConnectionStorage conStorage = new ConnectionStorage(connections);
        try {
            conStorage.load();
        } catch (IOException e) {
            DialogHelper.showModalExceptionDialog(e);
            e.printStackTrace();
        }

        favTable.setOnMousePressed((event) -> {
            if (event.isPrimaryButtonDown() && (event.getClickCount() == 2)) {
                onLoadAction(event);
            }
        });

        srv.addExitHandler(() -> {
            try {
                conStorage.save();
            } catch (Exception e) {
                DialogHelper.showModalExceptionDialog(e);
            }
        });
        if (connections.size() > 0) {
            favTable.getSelectionModel().select(0);
        }
    }

    private void infoFadeOut() {
        infoLabel.setOpacity(100);
        FadeTransition trans = new FadeTransition(Duration.seconds(3), infoLabel);
        trans.setFromValue(100);
        trans.setToValue(0);
        trans.play();
    }

    private void loggedOut() {
        infoLabel.setTextFill(Color.GREEN);
        infoLabel.setText("Logout successful");
        infoFadeOut();
    }

    private void loginFailed() {
        infoLabel.setTextFill(Color.RED);
        infoLabel.setText("Login failed");
        infoFadeOut();
    }

    private void loginSuccessful() {
        infoLabel.setTextFill(Color.GREEN);
        infoLabel.setText("Login successful");
        infoFadeOut();
    }

    @FXML
    private void onDeleteAction(ActionEvent event) {
        connections.remove(favTable.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onLoadAction(Event event) {
        if (!favTable.getSelectionModel().isEmpty()) {
            ConnectionInfo info = favTable.getSelectionModel().getSelectedItem();
            addressField.setText(info.getAddress());
            portField.setText(info.getPort().toString());
            usernameField.setText(info.getUsername());
            passwordField.setText(info.getPassword());
            passwordField.requestFocus();
        }
    }

    @FXML
    private void onLoginAction(ActionEvent event) {
        try {
            srv.login(addressField.getText(), Integer.parseInt(portField.getText()), usernameField.getText(), passwordField.getText());
            loginSuccessful();
        } catch (NumberFormatException | IOException | QueryException e) {
            DialogHelper.showModalExceptionDialog(e);
            e.printStackTrace();
            loginFailed();
        }
    }

    @FXML
    private void onLogoutAction(ActionEvent event) {
        srv.logout();
        loggedOut();
    }

    @FXML
    private void onSaveAction(ActionEvent event) {
        Optional<String> name = DialogHelper.showInputDialogAndWait(
                        srv.lang("start.servernamedialog.title"),
                        srv.lang("start.servernamedialog.default") + connections.size());
        if (name.isPresent()) {
            connections.add(new ConnectionInfo(name.get(),
                            addressField.getText(),
                            portField.getText(),
                            usernameField.getText(),
                            passwordField.getText()));
        }
    }

    @Override
    public void refresh() {/* not needed */}
}
