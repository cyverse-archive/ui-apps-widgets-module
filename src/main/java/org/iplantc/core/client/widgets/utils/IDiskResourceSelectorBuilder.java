package org.iplantc.core.client.widgets.utils;

import org.iplantc.core.client.widgets.dialogs.IFileSelectDialog;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.client.widgets.views.IFolderSelector;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.uidiskresource.client.models.File;

import com.google.gwt.user.client.Command;

public interface IDiskResourceSelectorBuilder {
    /**
     * Build a new IFileSelector
     * 
     * @param cmdChange a command to act upon when a file selection changes
     */
    IFileSelector buildFileSelector(Command cmdChange);

    /**
     * Build a new IFileSelector
     * 
     * @param property a meta data property
     * @param an instance of component value table
     */
    IFileSelector buildFileSelector(final Property property, final ComponentValueTable tblComponentVals);

    /**
     * Build a IFileSelectDialog.
     * 
     * @param tag unique tag for this dialog.
     * @param caption caption to display.
     * @param file the selected file.
     * @param currentFolderId id of the folder where the selected file resides.
     */
    IFileSelectDialog buildFileSelectorDialog(String tag, String caption, File file,
            String currentFolderId);

    /**
     * Build a new IFolderSelector
     * 
     * @param cmdChange a command to act upon when a folder selection changes
     */
    IFolderSelector buildFolderSelector(Command cmdChange);

    /**
     * Build a new IFolderSelector
     * 
     * @param property a meta data property
     * @param an instance of component value table
     */
    IFolderSelector buildFolderSelector(final Property property,
            final ComponentValueTable tblComponentVals);

}
