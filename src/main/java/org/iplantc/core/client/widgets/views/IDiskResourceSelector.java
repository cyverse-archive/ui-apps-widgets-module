package org.iplantc.core.client.widgets.views;

import org.iplantc.core.client.widgets.validator.IPlantValidator;

import com.extjs.gxt.ui.client.widget.Component;

/**
 * An interface defining a ResourceSelector
 * 
 * @author psarando
 * 
 */
public interface IDiskResourceSelector {
    /**
     * Set id for the resource selector widget
     */
    void setId(String id);

    /**
     * Get id for the resource selector widget
     * 
     * @return resource selector id
     */
    String getId();

    /**
     * Get the resource selector widget
     * 
     * @return The resource selector as a Component
     */
    Component getWidget();

    /**
     * Sets the internal spacing on the widget's root panel.
     * 
     * @param spacing an integer representing the spacing to use
     */
    void setSpacing(int spacing);

    /**
     * Sets the text for the button used to launch the resource selection dialog.
     * 
     * @param text button text to display.
     */
    void setButtonText(String text);

    /**
     * This will allow the text field within the resource selector to have a validator.
     * 
     * @param validator validator for edit box (generally this used to be invalid if this field is
     *            required).
     */
    void setValidator(IPlantValidator validator);

    /**
     * Get the folder id where the selected resource resides
     * 
     * @return the current folder ID
     */
    String getCurrentFolderId();

    /**
     * Sets the folder id where the selected resource resides
     * 
     * @param currentFolderId the folder id where the selected resource resides
     */
    void setCurrentFolderId(String currentFolderId);
}
