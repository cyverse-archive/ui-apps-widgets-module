package org.iplantc.core.client.widgets.appWizard.util;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.models.TemplatePropertyType;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardComboBox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextArea;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardFieldFactory {

    interface AppWizardFieldStyles extends CssResource {

    }

    interface FieldLabelTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml fieldLabel(String textClassName, SafeHtml name, String textToolTip, String elementName);

        @SafeHtmlTemplates.Template("<span style=\"color: red; float: left;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }
    
    private static FieldLabelTextTemplates templates = GWT.create(FieldLabelTextTemplates.class); 
    
    public static TemplatePropertyEditorBase createPropertyField(TemplateProperty property) {
        TemplatePropertyEditorBase field;
        switch (property.getType()) {
            case FileInput:
                field = new AppWizardFileSelector();
                break;

            case FolderInput:
                field = new AppWizardFileSelector();
                break;

            case MultiFileSelector:
                field = new AppWizardFileSelector();
                break;


            case Text:
                field = new AppWizardTextField();
                break;

            case QuotedText:
                field = new AppWizardTextField();
                break;

            case EnvironmentVariable:
                field = new AppWizardTextField();
                break;

            case MultiLineText:
                field = new AppWizardTextArea();
                break;

            case Number:
                field = new AppWizardTextField();
                // Must ensure user can only enter number
                break;

            case Flag:
                field = new AppWizardCheckbox();
                break;

            case BarcodeSelector:
                field = new AppWizardFileSelector();
                break;

            case ClipperSelector:
                field = new AppWizardFileSelector();
                break;

            case Selection:
                field = new AppWizardComboBox();
                break;

            case ValueSelection:
                field = new AppWizardComboBox();
                break;

            case TreeSelection:
                field = null;
                break;

            case Percentage:
                field = new AppWizardTextField("Some prepending label text");
                break;

            case Info:
                field = null;
                break;

            default:
                GWT.log(AppWizardFieldFactory.class.getName() + ": Unknown " + TemplatePropertyType.class.getName() + " type.");
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
//        for(TemplateValidator v : property.getValidators()){
//            Validator<String> validator = createValidator(v);
////            field.addValidator(validator);
//            
//        }
       // return field;
        
        return new AppWizardTextField();
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
        if (property.isRequired()) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired());
        }
        TemplatePropertyType type = property.getType();
        SafeHtml label = SafeHtmlUtils.fromString(property.getLabel());
        switch (type) {
            case FileInput:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case FolderInput:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case MultiFileSelector:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Info:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Text:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case EnvironmentVariable:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case MultiLineText:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Number:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Flag:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case BarcodeSelector:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case ClipperSelector:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Selection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case ValueSelection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case TreeSelection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Percentage:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            default:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;
        }

        return labelText.toSafeHtml();
    }
}
