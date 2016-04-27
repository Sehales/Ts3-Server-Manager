package net.sehales.ts3sm;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import net.sehales.ts3_japi.ServerQuery;
import net.sehales.ts3_japi.ServerQueryAPI;
import net.sehales.ts3_japi.command.CmdLogout;
import net.sehales.ts3_japi.exception.QueryException;
import net.sehales.ts3_japi.wrapper.Server;
import net.sehales.ts3sm.controller.MainController;
import net.sehales.ts3sm.util.DialogHelper;

public class ServerManager extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private ObservableList<Runnable> exitHandlers   = FXCollections.observableArrayList();
    private ServerQuery              query;
    private BooleanProperty          loggedIn       = new SimpleBooleanProperty();
    private ObjectProperty<Server>   selectedServer = new SimpleObjectProperty<>();
    private Stage                    stage;

    private ResourceBundle           lang;

    public void addExitHandler(Runnable run) {
        exitHandlers.add(run);
    }

    /**
     * Same as getQuery().api()
     * 
     * @return
     */
    public ServerQueryAPI api() {
        return query.api();
    }

    public ResourceBundle getLang() {
        return lang;
    }

    public ServerQuery getQuery() {
        return query;
    }

    public Server getSelectedServer() {
        return selectedServer.getValue();
    }

    public Stage getStage() {
        return stage;
    }

    public boolean hasServerSelected() {
        return selectedServer.get() != null;
    }

    public Boolean isLoggedIn() {
        return loggedInProperty().getValue();
    }

    /**
     * returns a localized string
     * 
     * @param key
     *            the key of the requested string, defined in the locale bundle
     * @return the localized string
     * */
    public String lang(String key) {
        return lang.getString(key);
    }

    /**
     * returns a localized string and replaces <code>{N}</code> tags, where <code>N</code> is the number of the element in the array
     * 
     * @param key
     *            the key of the requested string, defined in the locale bundle
     * @param values
     *            an array of objects which will replace the appropriate tags
     * @return the localized string
     */
    public String lang(String key, Object... values) {
        String string = lang(key);
        if (values.length != 0) {
            int i = 1;
            for (Object obj : values) {
                string = string.replace("{" + i + "}", obj.toString());
            }
        }
        return string;
    }

    public ReadOnlyBooleanProperty loggedInProperty() {
        return ReadOnlyBooleanProperty.readOnlyBooleanProperty(loggedIn);
    }

    public void login(String host, int port, String username, String password) throws UnknownHostException, IOException, QueryException {
        if ((query != null) && query.isOpen()) {
            logout();
        }
        query = ServerQuery.newConnection(host, port, username, password);
        query.setDebugMode(true);
        setLoggedIn(query.isOpen());
    }

    public void logout() {
        if ((query != null) && query.isOpen()) {
            query.send(new CmdLogout());
            query.close();
        }
        if (isLoggedIn()) {
            setLoggedIn(false);
        }
        query = null;
    }

    private void setLoggedIn(final Boolean loggedIn) {
        this.loggedIn.setValue(loggedIn);
    }

    public void setSelectedServer(final Server server) {
        selectedServer.setValue(server);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        try {
            Locale.setDefault(Locale.ENGLISH);
            lang = ResourceBundle.getBundle("lang");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/net/sehales/ts3sm/view/MainGUI.fxml"), lang);
            AnchorPane root = loader.load();
            MainController mainCon = loader.getController();
            mainCon.initTabs(this);
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/net/sehales/ts3sm/view/main.css");
            stage.setScene(scene);
            stage.setMinHeight(650);
            stage.setMinWidth(900);
            stage.setTitle(lang.getString("main.title"));
            stage.show();
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                System.err.println("UNCAUGHT EXCEPTION IN THREAD '" + thread.getName() + "'");
                throwable.printStackTrace();
                Platform.runLater(() -> DialogHelper.showModalExceptionDialog(throwable));
            });
            addExitHandler(() -> logout());
        } catch (Exception e) {
            DialogHelper.showModalExceptionDialog(e);
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        for (Runnable run : exitHandlers) {
            run.run();
        }
    }

}
