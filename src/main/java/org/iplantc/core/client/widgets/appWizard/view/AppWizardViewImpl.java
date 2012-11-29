package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;
import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;
import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.util.AppWizardFieldFactory;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldSet;
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
    
    private final AppTemplate appTemplate;
    
    public AppWizardViewImpl(final AppTemplate appTemplate){
        this.appTemplate = appTemplate;
        
        init();
    }

    private void init() {
        VerticalLayoutContainer mainContainer = new VerticalLayoutContainer();
        // First, loop through the template property groups.
        
        for(TemplateGroup group : appTemplate.getGroups()){
            // Construct a container to hold this group's properties (a.k.a. Fields)
            VerticalLayoutContainer vlc = new VerticalLayoutContainer();

            // For each group, loop through its properties and determine which field is required
            for(TemplateProperty property : group.getProperties()){
                FieldLabel propertyFieldLabel = AppWizardFieldFactory.createPropertyField(property);
                vlc.add(propertyFieldLabel);
                
            }
            
            // Create a FieldSet for this group, add the property container, then add the field set to the main container.
            FieldSet fieldSet = new FieldSet();
            fieldSet.add(vlc);
            mainContainer.add(fieldSet);
            
        }
    }

}
