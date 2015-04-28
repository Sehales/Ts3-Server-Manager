package net.sehales.ts3sm.factory;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import net.sehales.ts3_japi.wrapper.Server;

public class ServerListNameValueFactory implements Callback<CellDataFeatures<Server, Server>, ObservableValue<Server>> {
    @Override
    public ObservableValue<Server> call(CellDataFeatures<Server, Server> cellData) {
        return new SimpleObjectProperty<Server>(cellData.getValue());
    }

}
