package org.iplantc.core.client.widgets.appWizard.util;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.models.TemplatePropertyType;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyField;

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
    
    public static TemplatePropertyField<?> createPropertyField(TemplateProperty property) {
        TemplatePropertyField<?> field = null;
        if (property.getType().getEditorClass() != null) {
            // FIXME JDS This next line needs to be tested.
            field = GWT.create(property.getType().getEditorClass());
            // Set default value
            if ((property.getDefaultValue() != null) && !property.getDefaultValue().isEmpty()) {
                Splittable create = StringQuoter.split(property.getDefaultValue());
                property.setValue(create);
            }
            field.initialize(property);

        }

        if((field != null) 
                && (property.getDescription() != null)
                && !property.getDescription().isEmpty()){
            QuickTip qt = new QuickTip(field.asWidget());
            qt.setToolTip(property.getDescription());
        }

        return field;
    }
    
    /**
     * @param v
     * @return
     */
    private static Validator<?> createValidator(TemplateValidator v) {

        switch (v.getType()) {
            case CHARACTER_LIMIT:

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

            case REGEX:
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
