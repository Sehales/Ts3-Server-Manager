package net.sehales.ts3sm.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.controlsfx.glyphfont.FontAwesome;

import net.sehales.ts3_japi.wrapper.Server;
import net.sehales.ts3sm.util.Util;

public class ServerListNameCellFactory implements Callback<TableColumn<Server, Server>, TableCell<Server, Server>> {

    @Override
    public TableCell<Server, Server> call(TableColumn<Server, Server> column) {
        return new TableCell<Server, Server>() {
            @Override
            protected void updateItem(Server item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setText(item.getName());
                    if (item.getStatus().equalsIgnoreCase("online")) {
                        setGraphic(Util.getFANode(FontAwesome.Glyph.CHECK).color(Color.GREEN));
                    } else {
                        setGraphic(Util.getFANode(FontAwesome.Glyph.TIMES).color(Color.RED));
                    }
                }
            }
        };
    }
}
