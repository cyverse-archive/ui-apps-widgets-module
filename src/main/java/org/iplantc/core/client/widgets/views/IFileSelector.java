package org.iplantc.core.client.widgets.views;

import org.iplantc.core.uidiskresource.client.models.File;

/**
 * An interface defining FileSelector
 * 
 * @author sriram
 * 
 */
public interface IFileSelector extends IDiskResourceSelector {
    /**
     * Indicates if the widget has a selected file.
     * 
     * @return true, if there is a selection; otherwise false.
     */
    boolean hasSelectedFile();

    /**
     * Retrieves the selected file.
     * 
     * This will return null when a file is not selected.
     * 
     * @return a reference to the selected file or null when there is no selection.
     */
    File getSelectedFile();

    /**
     * Defines the selected file.
     * 
     * This will set the selected file in all the underlying widgets (including the FileSelectDialog)
     * 
     * @param file the selected file
     */
    void setSelectedFile(File file);

    /**
     * Retrieves an identifier for the file that can be used to fetch metadata or content.
     * 
     * This will return null when a file is not selected.
     * 
     * @return a string representing the identifier for the selected file or null when there is no
     *         selection.
     */
    String getSelectedFileId();

    /**
     * Set file name to display.
     * 
     * @param name file name to display.
     */
    void displayFilename(String name);
}
