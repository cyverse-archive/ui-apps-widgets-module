package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.Argument;

public interface AppTemplateWizardPresenter {

    void onArgumentPropertyValueChange();

    /**
     * Returns a boolean stating whether or not editing mode is enabled.
     * 
     * When editing mode is true, {@link ArgumentValueEditor} selection is enabled, and all field
     * validation is disabled.
     * 
     * @return true if editing mode is enabled, false otherwise.
     */
    boolean isEditingMode();

    Argument copyArgument(Argument value);

}
