package net.sehales.ts3sm.controller;

import javafx.scene.control.Tab;

/**
 * Base class for tabs containing refreshable information
 * 
 * @author Sehales
 *
 */
public abstract class RefreshableTab extends Tab {
    public RefreshableTab(String name) {
        super(name);
    }

    /**
     * the method maybe be called for refreshing all information and is getting called each time a users accesses a specific tab
     */
    public abstract void refresh();
}
