package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.JobExecution;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardFolderSelector;
import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * @author jstroot
 * 
 */
public class LaunchAnalysisWidget implements IsWidget, ValueAwareEditor<JobExecution> {

    private final String HEADING_PREFIX = "Analysis Name: ";
    private static LaunchAnalysisWidgetUiBinder BINDER = GWT.create(LaunchAnalysisWidgetUiBinder.class);

    interface LaunchAnalysisWidgetUiBinder extends UiBinder<Widget, LaunchAnalysisWidget> {}

    interface EditorDriver extends SimpleBeanEditorDriver<JobExecution, LaunchAnalysisWidget> {}

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Ignore
    @UiField
    ContentPanel contentPanel;

    @UiField
    TextField name;

    @UiField
    TextArea description;

    @UiField
    CheckBox retainInputs;

    @Ignore
    @UiField
    AppWizardFolderSelector awFolderSel;

    ConverterEditorAdapter<String, HasId, AppWizardFolderSelector> outputDirectory;

    public LaunchAnalysisWidget() {
        BINDER.createAndBindUi(this);
        outputDirectory = new ConverterEditorAdapter<String, HasId, AppWizardFolderSelector>(awFolderSel, new Converter<String, HasId>() {
            @Override
            public String convertFieldValue(HasId object) {
                return object.getId();
            }

            @Override
            public HasId convertModelValue(String object) {
                return CommonModelUtils.createHasIdFromString(object);
            }
        });
        editorDriver.initialize(this);
    }

    @UiHandler("name")
    void onNameChange(ValueChangeEvent<String> event) {
        contentPanel.setHeadingText(HEADING_PREFIX + name.getCurrentValue());
    }

    public void edit(JobExecution je) {
        editorDriver.edit(je);
    }

    public JobExecution flushJobExecution() {
        return editorDriver.flush();
    }

    public boolean hasErrors() {
        return editorDriver.hasErrors();
    }

    public List<EditorError> getErrors() {
        return editorDriver.getErrors();
    }

    @Override
    public void setDelegate(EditorDelegate<JobExecution> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setValue(JobExecution value) {
        contentPanel.setHeadingText(HEADING_PREFIX + value.getName());
    }

    @Override
    public Widget asWidget() {
        return contentPanel;
    }
}
