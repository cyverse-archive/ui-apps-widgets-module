package org.iplantc.core.client.widgets.dialogs;

import org.iplantc.core.uidiskresource.client.models.File;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

public interface IFileSelectDialog {
    /**
     * Add a handler for when the OK button is clicked.
     * 
     * @param handler handler to be added.
     */
    void addOkClickHandler(SelectionListener<ButtonEvent> handler);

    /**
     * Retrieve the file the user has selected.
     * 
     * @return the selected file.
     */
    File getSelectedFile();

    /**
     * Display the dialog.
     */
    void show();
}
