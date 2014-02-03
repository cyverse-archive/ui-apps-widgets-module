package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import com.sencha.gxt.widget.core.client.form.TextField;

import org.iplantc.de.resources.client.uiapps.widgets.argumentTypes.EnvironmentVariableLabels;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

public class EnvironmentVariableEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<String> editorAdapter;
    private final TextField textField;

    public EnvironmentVariableEditor(AppTemplateWizardAppearance appearance, EnvironmentVariableLabels labels) {
        super(appearance);
        textField = new TextField();
        textField.setEmptyText(labels.envVarWidgetEmptyText());
        editorAdapter = new ArgumentEditorConverter<String>(textField, new SplittableToStringConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;

    }

}
