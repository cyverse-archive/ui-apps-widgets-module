package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.FieldSet;

public class TemplateGroupEditor extends Composite implements ValueAwareEditor<TemplateGroup> {

    @UiTemplate("TemplateGroupEditor.ui.xml")
    interface TemplateGroupEditorUiBinder extends UiBinder<Widget, TemplateGroupEditor> {}
    
    private static TemplateGroupEditorUiBinder BINDER = GWT.create(TemplateGroupEditorUiBinder.class);

    @Ignore
    @UiField
    FieldSet groupField;
    
//    @UiField
//    @Path("properties")
//    TemplatePropertyListEditor propertiesEditor;

    @UiField
    @Path("groups")
    TemplateGroupListEditor groupsEditor;
    
    public TemplateGroupEditor() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public void setDelegate(EditorDelegate<TemplateGroup> delegate) {
    }

    @Override
    public void flush() {
    }

    @Override
    public void onPropertyChange(String... paths) {
    }

    @Override
    public void setValue(TemplateGroup value) {
        // When the value is set, update the FieldSet header text
        groupField.setHeadingText(value.getLabel());
        groupsEditor.asEditor().setValue(value.getGroups());
//        propertiesEditor.asEditor().setValue(value.getProperties());
    }

}
