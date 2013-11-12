package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import com.google.gwt.editor.client.LeafValueEditor;

import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;

public class VisibilityEditor implements LeafValueEditor<Boolean> {

    private final AppTemplateForm.ArgumentEditor argEditor;
    private Boolean value;

    public VisibilityEditor(AppTemplateForm.ArgumentEditor argEditor) {
        this.argEditor = argEditor;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        if (value == null) {
            return;
        }
        this.value = value.booleanValue();
        if (argEditor.isDisabledOnNotVisible()) {
            argEditor.setVisible(true);
            argEditor.setEnabled(value);
        } else {
            argEditor.setVisible(value);
        }
    }

}