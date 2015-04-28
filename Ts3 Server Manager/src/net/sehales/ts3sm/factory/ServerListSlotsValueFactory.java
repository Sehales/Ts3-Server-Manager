package net.sehales.ts3sm.factory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import net.sehales.ts3_japi.wrapper.Server;

public class ServerListSlotsValueFactory implements Callback<CellDataFeatures<Server, String>, ObservableValue<String>> {
    @Override
    public ObservableValue<String> call(CellDataFeatures<Server, String> cellData) {
        Server s = cellData.getValue();
        String result = Integer.toString(s.getClientsOnline() - s.getQueryClientsOnline());
        if (s.getQueryClientsOnline() > 0) {
            result += "+" + s.getQueryClientsOnline();
        }
        result += "/" + s.getMaxClients();

        return new SimpleStringProperty(result);
    }

}
