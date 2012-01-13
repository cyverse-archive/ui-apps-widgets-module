package org.iplantc.core.client.widgets.views;

import org.iplantc.core.uidiskresource.client.models.Folder;

/**
 * An interface defining a FolderSelector
 * 
 * @author psarando
 * 
 */
public interface IFolderSelector extends IDiskResourceSelector {
    /**
     * Indicates if the widget has a selected folder.
     * 
     * @return true, if there is a selection; otherwise false.
     */
    boolean hasSelectedFolder();

    /**
     * Retrieves the selected folder.
     * 
     * This will return null when a folder is not selected.
     * 
     * @return a reference to the selected folder or null when there is no selection.
     */
    Folder getSelectedFolder();

    /**
     * Defines the selected folder.
     * 
     * This will set the selected folder in all the underlying widgets (including the FolderSelectDialog)
     * 
     * @param folder the selected folder
     */
    void setSelectedFolder(Folder folder);

    /**
     * Retrieves an identifier for the folder that can be used to fetch metadata or content.
     * 
     * This will return null when a folder is not selected.
     * 
     * @return a string representing the identifier for the selected folder or null when there is no
     *         selection.
     */
    String getSelectedFolderId();

    /**
     * Set folder name to display.
     * 
     * @param name folder name to display.
     */
    void displayFolderName(String name);
}
