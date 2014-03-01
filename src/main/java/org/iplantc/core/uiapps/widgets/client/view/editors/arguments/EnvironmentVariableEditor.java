package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import org.iplantc.core.resources.client.uiapps.widgets.argumentTypes.EnvironmentVariableLabels;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToStringConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uicommons.client.validators.CmdLineArgCharacterValidator;

import com.sencha.gxt.widget.core.client.form.TextField;

public class EnvironmentVariableEditor extends AbstractArgumentEditor {
    private final ArgumentEditorConverter<String> editorAdapter;
    private final TextField textField;

    public EnvironmentVariableEditor(AppTemplateWizardAppearance appearance, EnvironmentVariableLabels labels) {
        super(appearance);
        textField = new TextField();
        textField.setEmptyText(labels.envVarWidgetEmptyText());
        textField.addValidator(new CmdLineArgCharacterValidator());
        editorAdapter = new ArgumentEditorConverter<String>(textField, new SplittableToStringConverter());

        argumentLabel.setWidget(editorAdapter);
    }

    @Override
    public void disableValidations() {
        super.disableValidations();

        textField.getValidators().clear();
    }

    @Override
    public ArgumentEditorConverter<?> valueEditor() {
        return editorAdapter;

    }

}
