package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppTemplateWizardPresenter extends IsWidget {

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

    void showToolSearchDialog();

    Object getValueChangeEventSource();

    void onArgumentPropertyValueChange(Object source);

    boolean isOnlyLabelEditMode();

    void setOnlyLabelEditMode(boolean onlyLabelEditMode);

    AppTemplateWizardAppearance getAppearance();

}
