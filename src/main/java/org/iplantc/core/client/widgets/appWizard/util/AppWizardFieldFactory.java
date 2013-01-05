package org.iplantc.core.client.widgets.appWizard.util;

import java.util.List;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.models.TemplatePropertyType;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardComboBox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardDiskResourceSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardDoubleNumberField;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardMultiFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextArea;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;
import org.iplantc.core.uicommons.client.validators.HasValidators;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

public class AppWizardFieldFactory {

    interface FieldLabelTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml fieldLabel(String textClassName, SafeHtml name, String textToolTip, String elementName);

        @SafeHtmlTemplates.Template("<span style=\"color: red; float: left;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }
    
    private static FieldLabelTextTemplates templates = GWT.create(FieldLabelTextTemplates.class); 
    
    public static TemplatePropertyEditorBase<?> createPropertyField(TemplateProperty property) {
        TemplatePropertyEditorBase<?> field;
        switch (property.getType()) {
            case FileInput:
                field = AppWizardDiskResourceSelector.asFileSelector();
                break;

            case FolderInput:
                field = AppWizardDiskResourceSelector.asFolderSelector();
                break;

            case MultiFileSelector:
                field = new AppWizardMultiFileSelector();
                break;

            case Text:
                field = new AppWizardTextField();
                break;

            case EnvironmentVariable:
                field = new AppWizardTextField();
                break;

            case MultiLineText:
                field = new AppWizardTextArea();
                break;

            case Number:
                field = new AppWizardDoubleNumberField();
                if ((property.getDefaultValue() != null) && !property.getDefaultValue().isEmpty()) {
                    Splittable create = StringQuoter.split(property.getDefaultValue());
                    property.setValue(create);
                }

                break;

            case Flag:
                field = new AppWizardCheckbox();
                break;

            case Selection:
                field = new AppWizardComboBox(property.getArguments());
                break;

            case ValueSelection:
                field = new AppWizardComboBox(property.getArguments());
                break;

            case TreeSelection:
                field = null;
                break;

            case Info:
                field = null;
                break;

            default:
                GWT.log(AppWizardFieldFactory.class.getName() + ": Unknown " + TemplatePropertyType.class.getName() + " type.");
                field = null;
                break;
        }
        
        if((field != null) 
                && (property.getDescription() != null)
                && !property.getDescription().isEmpty()){
            QuickTip qt = new QuickTip(field.asWidget());
            qt.setToolTip(property.getDescription());
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
        // }
        return field;
    }
    
    private static <T> void applyValidators(final HasValidators<T> hasValidators, final List<Validator<T>> validators) {
        hasValidators.addValidators(validators);
    }

    private static <T> List<Validator<T>> createValidators(TemplateProperty tp) {
        List<Validator<T>> validators = Lists.newArrayList();
        for (TemplateValidator tv : tp.getValidators()) {
            validators.add(createValidator(tv));
        }
        return null;
    }

    /**
     * @param v
     * @return
     */
    private static Validator<?> createValidator(TemplateValidator v) {

        switch (v.getType()) {
            case CHARACTER_LIMIT:

                break;

            case CLIPPER_DATA:
                break;

            case DOUBLE_ABOVE:
                break;

            case DOUBLE_BELOW:
                break;

            case DOUBLE_RANGE:
                break;

            case FILE_NAME:
                break;

            case GENOTYPE_NAME:
                break;

            case INT_ABOVE:
                break;

            case INT_ABOVE_FIELD:
                break;

            case INT_BELOW:
                break;

            case INT_BELOW_FIELD:
                break;

            case INT_RANGE:
                break;

            case MUST_CONTAIN:
                break;

            case NON_EMPTY_CLASS:
                break;

            case REGEX:
                break;

            case SELECT_ONE_CHECKBOX:
                break;

            default:
                break;
        }
        return null;
    }
    
    public static SafeHtml createFieldLabelText(TemplateProperty property){
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        if (property.isRequired()) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired());
        }
        TemplatePropertyType type = property.getType();
        // JDS Remove the trailing colon. The FieldLabels will apply it automatically.
        SafeHtml label = SafeHtmlUtils.fromString(property.getLabel().replaceFirst(":$", ""));

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

            case Selection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case ValueSelection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case TreeSelection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            default:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;
        }

        return labelText.toSafeHtml();
    }
}
