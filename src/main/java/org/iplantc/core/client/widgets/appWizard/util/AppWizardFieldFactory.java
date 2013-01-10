package org.iplantc.core.client.widgets.appWizard.util;

import java.util.List;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.models.TemplatePropertyType;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidatorType;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardComboBox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardFolderSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardMultiFileSelector;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextArea;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.DoubleNumberField;
import org.iplantc.core.client.widgets.appWizard.view.fields.IntegerNumberField;
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
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

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

            case Number:
                field = getNumberField(property);
                break;

            case Double:
                field = new DoubleNumberField();
                break;

            case Integer:
                field = new IntegerNumberField();
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
                field = new DoubleNumberField();
                break;

            case Output:
                field = new AppWizardTextField();
                break;

            default:
                GWT.log(AppWizardFieldFactory.class.getName() + ": Unknown " + TemplatePropertyType.class.getName() + " type.");
                field = null;
                break;
        }

        setDefaultValue(property);

        if (field != null) {

            if ((property.getDescription() != null) && !property.getDescription().isEmpty()) {
                ToolTipConfig ttCon = new ToolTipConfig(property.getDescription());
                field.setToolTip(ttCon);
            }
        }

        return field;
    }

    /**
     * Returns a NumberField based on any applicable validators. If the property contains Integer
     * validators, then an Integer number field will be returned. Otherwise, a Double number field will
     * be
     * returned.
     * 
     * @param property
     * @return
     */
    private static TemplatePropertyField getNumberField(TemplateProperty property) {

        TemplatePropertyField field = null;
        List<TemplateValidator> validators = property.getValidators();
        if (validators != null) {
            for (TemplateValidator tv : validators) {
                if (tv.getType().equals(TemplateValidatorType.IntAbove) || tv.getType().equals(TemplateValidatorType.IntBelow) || tv.getType().equals(TemplateValidatorType.IntRange)) {
                    field = new IntegerNumberField();
                    break;
                }
            }
        }
        if (field == null) {
            field = new DoubleNumberField();
        }

        return field;
    }

    private static void setDefaultValue(TemplateProperty property) {
        String defaultValue = property.getDefaultValue();
        if ((defaultValue != null) && !defaultValue.isEmpty()) {
            try {
                Splittable create = StringQuoter.split(defaultValue);
                property.setValue(create);
            } catch (Exception e) {
                // If we couldn't parse as a JSON value, create a string splittable.
                Splittable create = StringQuoter.create(defaultValue);
                property.setValue(create);
            }
        }
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

    public static Validator<Integer> createIntegerValidator(TemplateValidator tv) {
        Validator<Integer> validator;

        switch (tv.getType()) {
            case IntRange:
                // array of two integers
                int min = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                int max = Double.valueOf(tv.getParams().get(1).asNumber()).intValue();
                validator = new NumberRangeValidator<Integer>(min, max);
                break;
            case IntAbove:
                // array of one integer
                int min2 = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                validator = new MinNumberValidator<Integer>(min2);
                break;
            case IntBelow:
                // array of one integer
                int max2 = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                validator = new MaxNumberValidator<Integer>(max2);
                break;
            default:
                throw new UnsupportedOperationException("Given validator type is not an Integer validator type.");
        }
        return validator;
    }

    public static Validator<Double> createDoubleValidator(TemplateValidator tv) {
        Validator<Double> validator;
        switch (tv.getType()) {
            case DoubleRange:
                // Array of two doubles
                double min = Double.valueOf(tv.getParams().get(0).asNumber());
                double max = Double.valueOf(tv.getParams().get(1).asNumber());
                validator = new NumberRangeValidator<Double>(min, max);
                break;
            case DoubleAbove:
                // Array of one double
                double min2 = Double.valueOf(tv.getParams().get(0).asNumber());
                validator = new MinNumberValidator<Double>(min2);
                break;
            case DoubleBelow:
                // Array of one double
                double max2 = Double.valueOf(tv.getParams().get(0).asNumber());
                validator = new MaxNumberValidator<Double>(max2);
                break;

            default:
                throw new UnsupportedOperationException("Given validator type is not a Double validator type.");

        }
        return validator;
    }

    public static Validator<String> createStringValidator(TemplateValidator tv) {
        Validator<String> validator;
        switch (tv.getType()) {
            case CharacterLimit:
                // Array containing single integer
                int min = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                validator = new MaxLengthValidator(min);
                break;
            case Regex:
                // Array containing one string
                String regex = tv.getParams().get(0).asString();
                validator = new RegExValidator(regex);
                break;
            case FileName:
                validator = new NameValidator3();
                break;
            default:
                throw new UnsupportedOperationException("Given validator type is not a String validator type.");

        }
        return validator;
    }
}
