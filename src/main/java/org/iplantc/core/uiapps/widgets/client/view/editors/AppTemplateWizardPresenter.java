package org.iplantc.core.uiapps.widgets.client.view.editors;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface AppTemplateWizardPresenter {

    public interface SelectionCss extends CssResource {

        String selectionTargetHover();

        String selectionTarget();
    }

    public interface Resources extends ClientBundle {
        @Source("AppTemplateSelection.css")
        SelectionCss selectionCss();
    }

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

}
