package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import com.sencha.gxt.widget.core.client.form.TextArea;

import org.iplantc.core.resources.client.uiapps.widgets.argumentTypes.TextInputLabels;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

public class MultiLineTextEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<String> editorAdapter;
    private final TextArea textArea;

    public MultiLineTextEditor(AppTemplateWizardAppearance appearance, TextInputLabels labels) {
        super(appearance);
        textArea = new TextArea();
        textArea.setEmptyText(labels.textInputWidgetEmptyText());
        editorAdapter = new ArgumentEditorConverter<String>(textArea, new SplittableToStringConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;
    }

}
