package org.iplantc.core.client.widgets.appWizard.view.fields;


import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * XXX JDS This class will have to support an optional field which preceeds the box (for "x = " labels).
 * @author jstroot
 *
 */
public class AppWizardTextField extends TextField implements TemplatePropertyEditorBase<String> {
    
    public AppWizardTextField(){
        
    }
    
    public AppWizardTextField(String prependedLabelText) {
    }


}
