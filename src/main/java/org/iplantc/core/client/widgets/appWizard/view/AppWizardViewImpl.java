package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.view.editors.TemplateGroupListEditor;

import com.sencha.gxt.widget.core.client.form.FormPanel;

/**
 * 
 * Will eventually have to support drops to this form and the things it may contain.
 * 
 * Drops on the form itself
 * Drops on "groups"
 * @author jstroot
 *
 */
public class AppWizardViewImpl extends FormPanel implements AppWizardView {
    
    
    @Path("groups")
    TemplateGroupListEditor groups;
    
    public AppWizardViewImpl(){
        groups = new TemplateGroupListEditor();
        add(groups);
        
    }

}
