package net.sehales.ts3sm.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import org.controlsfx.glyphfont.FontAwesome;

import net.sehales.ts3_japi.wrapper.Server;
import net.sehales.ts3sm.util.Util;

public class ServerListAutostartCellFactory implements Callback<TableColumn<Server, Boolean>, TableCell<Server, Boolean>> {

    @Override
    public TableCell<Server, Boolean> call(TableColumn<Server, Boolean> column) {
        return new TableCell<Server, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    if (item) {
                        setGraphic(Util.getFANode(FontAwesome.Glyph.CHECK).color(Color.GREEN));
                    } else {
                        setGraphic(Util.getFANode(FontAwesome.Glyph.TIMES).color(Color.RED));
                    }
                }
            }
        };
    }
}
