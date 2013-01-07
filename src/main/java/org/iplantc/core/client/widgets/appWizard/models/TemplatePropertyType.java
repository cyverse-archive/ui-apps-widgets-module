package org.iplantc.core.client.widgets.appWizard.models;

import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardComboBox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardDoubleNumberField;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFolderSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardMultiFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextArea;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyField;



public enum TemplatePropertyType {
    FileInput(AppWizardFileSelector.class),
    FolderInput(AppWizardFolderSelector.class),
    MultiFileSelector(AppWizardMultiFileSelector.class),
    EnvironmentVariable(AppWizardTextField.class),
    Flag(AppWizardCheckbox.class),
    Info(null),
    MultiLineText(AppWizardTextArea.class),
    Number(AppWizardDoubleNumberField.class),
    Text(AppWizardTextField.class),
    Selection(AppWizardComboBox.class), // For selecting from a list of string values.
    ValueSelection(AppWizardComboBox.class), // For selecting from a list of numbers
    TreeSelection(null);
    // Input
    // Output

    private final Class<? extends TemplatePropertyField<?>> editorClazz;
    
    private TemplatePropertyType(Class<? extends TemplatePropertyField<?>> editorClazz) {
        this.editorClazz = editorClazz;
    }

    public Class<? extends TemplatePropertyField<?>> getEditorClass() {
        return editorClazz;
    }

}
