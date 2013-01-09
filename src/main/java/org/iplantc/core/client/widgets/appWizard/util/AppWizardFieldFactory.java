package org.iplantc.core.client.widgets.appWizard.util;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.models.TemplatePropertyType;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardComboBox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardDoubleNumberField;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFolderSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardMultiFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextArea;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyField;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.validators.NumberRangeValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

public class AppWizardFieldFactory {

    interface FieldLabelTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml fieldLabel(String textClassName, SafeHtml name, String textToolTip, String elementName);

        @SafeHtmlTemplates.Template("<span style=\"color: red; float: left;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }
    
    private static FieldLabelTextTemplates templates = GWT.create(FieldLabelTextTemplates.class); 
    
    public static TemplatePropertyField createPropertyField(TemplateProperty property) {
        TemplatePropertyField field;
        switch (property.getType()) {
            case FileInput:
                field = new AppWizardFileSelector();
                break;

            case FolderInput:
                field = new AppWizardFolderSelector();
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

            case Double:
                field = new AppWizardDoubleNumberField();
                break;

            case Integer:
                field = null;
                break;

            case Flag:
                field = new AppWizardCheckbox();
                break;

            case TextSelection:
                field = new AppWizardComboBox();
                break;

            case IntegerSelection:
                field = null;
                break;

            case DoubleSelection:
                field = null;
                break;

            case TreeSelection:
                field = null;
                break;

            case Info:
                field = null;
                break;

            // Legacy Types
            case Selection:
                // Default to Text Selection
                field = new AppWizardComboBox();
                break;

            case ValueSelection:
                // TODO JDS Map this to either IntegerSelection or DoubleSelection
                field = new AppWizardDoubleNumberField();
                break;

            default:
                GWT.log(AppWizardFieldFactory.class.getName() + ": Unknown " + TemplatePropertyType.class.getName() + " type.");
                field = null;
                break;
        }

        if (field != null) {
            if ((property.getDefaultValue() != null) && !property.getDefaultValue().isEmpty()) {
                Splittable create = StringQuoter.split(property.getDefaultValue());
                property.setValue(create);
            }
            if ((property.getDescription() != null) && !property.getDescription().isEmpty()) {
                QuickTip qt = new QuickTip(field.asWidget());
                qt.setToolTip(property.getDescription());
            }

        }
        return field;
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

            case Integer:
            case Double:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case Flag:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case TextSelection:
                labelText.append(templates.fieldLabel("", label, "", ""));
                break;

            case IntegerSelection:
            case DoubleSelection:
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

    /**
     * @param v
     * @return
     */
    public static Validator<?> createValidator(TemplateValidator tv) {
        Validator<?> validator;
        switch (tv.getType()) {
            case IntRange:
                validator = new NumberRangeValidator<Integer>(0, 0);
                break;
            case IntAbove:
                validator = new MinNumberValidator<Integer>(0);
                break;
            case IntBelow:
                validator = new MaxNumberValidator<Integer>(0);
                break;
            case DoubleRange:
                validator = new NumberRangeValidator<Double>(0d, 0d);
                break;
            case DoubleAbove:
                validator = new MinNumberValidator<Double>(0d);
                break;
            case DoubleBelow:
                validator = new MaxNumberValidator<Double>(0d);
                break;
            case FileName:
                validator = new NameValidator3();
                break;
            case Regex:
                validator = new RegExValidator("", "");
                break;
            case CharacterLimit:
                validator = new MaxLengthValidator(200);
                break;

            default:
                // FIXME JDS Throw exception to notify of bad condition.
                validator = null;
                break;

        }

        return validator;
    }
}
