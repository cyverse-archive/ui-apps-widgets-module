package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.form.FieldSet;

/**
 * TODO JDS Create a UiBinder template for this class which contains the 
 * @author jstroot
 *
 */
public class TemplateGroupEditor extends Composite implements ValueAwareEditor<TemplateGroup>{
    
    FieldSet groupField = new FieldSet();
    
    @Path("properties")
    TemplatePropertyListEditor propertyList = new TemplatePropertyListEditor();

    @Path("groups")
    TemplateGroupListEditor groupList = new TemplateGroupListEditor();
    

    @Override
    public void setDelegate(EditorDelegate<TemplateGroup> delegate) {
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
    public void setValue(TemplateGroup value) {
        // When the value is set, update the FieldSet header text
        groupField.setHeadingText(value.getLabel());
        
        
        
    }

}
