package org.iplantc.core.client.widgets.appWizard.util;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardFieldFactory {

    interface FieldLabelTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml fieldLabel(String textClassName, SafeHtml name, String textToolTip, String elementName);

        @SafeHtmlTemplates.Template("<span class=\"{0}\">*</span>")
        SafeHtml fieldLabelRequired(String style);
    }
    
    private static FieldLabelTextTemplates templates = GWT.create(FieldLabelTextTemplates.class); 
    
    public static TemplatePropertyEditorBase createPropertyField(TemplateProperty property) {
        TemplatePropertyEditorBase field;
        switch (property.getType()) {
            case FILE_INPUT:
                field = null;
                break;

            case FOLDER_INPUT:
                field = null;
                break;
            
            case MULTI_FILE_SELECTOR:
                field = null;
                break;
            
            case INFO:
                field = null;
                break;

            case TEXT:
//                field = new TextField();
                field = null;

                break;

            case QUOTED_TEXT:
//                field = new TextField();
                field = null;

                break;

            case ENV_VARIABLE:
//                field = new TextField();
                field = null;

                break;

            case MULTI_LINE_TEXT:
//                field = new TextArea();
                field = null;

                break;

            case NUMBER:
//                field = new TextField();
                field = null;

                // Must ensure user can only enter number
                break;

            case FLAG:
                field = new AppWizardCheckbox();
                break;

            case SKIP_FLAG:
                field = null;
                break;
          
            case X_BASE_PAIRS:
                field = null;
                break;
          
            case X_BASE_PAIRS_TEXT:
                field = null;
                break;
           
            case BARCODE_SELECTOR:
                field = null;
                break;
            
            case CLIPPER_SELECTOR:
                field = null;
                break;
           
            case SELECTION:
                field = null;
                break;
           
            case VALUE_SELECTION:
                field = null;
                break;
           
            case TREE_SELECTION:
                field = null;
                break;
           
            case PERCENTAGE:
                field = null;
                break;
           
            case DESCRIPTIVE_TEXT:
                field = null;
                break;
            
            default:
                field = null;
                break;
        }

        /* 
         * Once we've determined the correct field, we need to interrogate the
         *  property's "validator" and its rules to determine which validators to 
         *  add to the form field.
         *  
         *  Each field will need to be set to auto-validate.
         *  
         *  Each validator will need to have this class added as a valid/invalid 
         *  handler.
         */
        for(TemplateValidator v : property.getValidators()){
            Validator<String> validator = createValidator(v);
//            field.addValidator(validator);
            
        }
        return field;
    }
    
    /**
     * TODO JDS Determine if a String type will be sufficient
     * @param v
     * @return
     */
    private static Validator<String> createValidator(TemplateValidator v) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static SafeHtml createFieldLabelText(TemplateProperty property){
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        switch (property.getType()) {
            case FILE_INPUT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case FOLDER_INPUT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case MULTI_FILE_SELECTOR:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case INFO:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case TEXT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case QUOTED_TEXT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case ENV_VARIABLE:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case MULTI_LINE_TEXT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;

            case NUMBER:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case FLAG:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case SKIP_FLAG:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case X_BASE_PAIRS:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case X_BASE_PAIRS_TEXT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case BARCODE_SELECTOR:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case CLIPPER_SELECTOR:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case SELECTION:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case VALUE_SELECTION:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case TREE_SELECTION:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case PERCENTAGE:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            case DESCRIPTIVE_TEXT:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
                
            default:
                labelText.append(templates.fieldLabel("", null, "", ""));
                break;
        }
        if (property.isRequired()) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired(""));
        }
        return labelText.toSafeHtml();
    }
}
