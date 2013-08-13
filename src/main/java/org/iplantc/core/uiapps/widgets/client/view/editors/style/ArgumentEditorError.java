package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

public final class ArgumentEditorError extends DefaultEditorError implements HasLabel {

    private final String label;

    public ArgumentEditorError(String label, EditorError err) {
        super(err.getEditor(), err.getMessage(), err.getValue());
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {/* Do Nothing */}

}