package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;
import org.iplantc.core.client.widgets.appWizard.view.editors.TemplateGroupListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * Will eventually have to support drops to this form and the things it may contain.
 * 
 * Drops on the form itself
 * Drops on "groups"
 * @author jstroot
 *
 */
public class AppWizardViewImpl extends Composite implements AppWizardView, ValueAwareEditor<AppTemplate> {

    @UiTemplate("AppWizardView.ui.xml")
    interface AppWizardViewUIUiBinder extends UiBinder<Widget, AppWizardViewImpl> {
    }
    
    private static AppWizardViewUIUiBinder BINDER = GWT.create(AppWizardViewUIUiBinder.class);

    @Path("groups")
    @UiField
    TemplateGroupListEditor groupsEditor;
    
    @UiField
    TextField name;

    public AppWizardViewImpl() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public void setDelegate(EditorDelegate<AppTemplate> delegate) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPropertyChange(String... paths) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setValue(AppTemplate value) {
        groupsEditor.asEditor().setValue(value.getGroups());
        
    }
}
