package net.sehales.ts3sm.factory;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import net.sehales.ts3_japi.wrapper.Server;

public class ServerListUptimeValueFactory implements Callback<CellDataFeatures<Server, String>, ObservableValue<String>> {
    private static int SECONDS_PER_MINUTE = 60;
    private static int SECONDS_PER_HOUR   = SECONDS_PER_MINUTE * 60;
    private static int SECONDS_PER_DAY    = SECONDS_PER_HOUR * 24;

    @Override
    public ObservableValue<String> call(CellDataFeatures<Server, String> cellData) {
        Server server = cellData.getValue();
        long seconds = server.getUptime();

        int days = (int) (seconds / SECONDS_PER_DAY);
        int hours = (int) (seconds % SECONDS_PER_DAY) / SECONDS_PER_HOUR;
        int minutes = (int) ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);

        String formattedTime = String.format("%dd %02d:%02d", days, hours != 0 ? hours : 0, minutes != 0 ? minutes : 0);
        return new SimpleStringProperty(formattedTime);
    }
}
