package org.iplantc.core.uiapps.widgets.client.view.fields.util;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidatorType;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardComboBox;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardDiskResourceSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardFileSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardFolderSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardMultiFileSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToBooleanConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToDoubleConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToHasIdConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToHasIdListConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToIntegerConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.validators.NumberRangeValidator;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.TakesValue;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class AppWizardFieldFactory {

    interface FieldLabelTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span qtip=\"{1}\">{0}</span>")
        SafeHtml fieldLabel(SafeHtml name, String textToolTip);

        @SafeHtmlTemplates.Template("<span style=\"color: red; float: left;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }
    
    private static FieldLabelTextTemplates templates = GWT.create(FieldLabelTextTemplates.class); 
    
    public static ArgumentField createPropertyField(Argument argument) {
        printGwtLogInfo(argument);
        ArgumentField field = null;
        TextField tf = new TextField();
        NumberField<Double> dblNumField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
        NumberField<Integer> intNumField = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        switch (argument.getType()) {
            case FileInput:
                AppWizardFileSelector awFileSel = new AppWizardFileSelector();
                ConverterFieldAdapter<HasId, AppWizardFileSelector> fileSelCfa = new ConverterFieldAdapter<HasId, AppWizardFileSelector>(awFileSel, new SplittableToHasIdConverter());
                field = applyDiskResourceValidators(argument, fileSelCfa);
                break;

            case FolderInput:
                AppWizardFolderSelector awFolderSel = new AppWizardFolderSelector();
                ConverterFieldAdapter<HasId, AppWizardFolderSelector> folderSelCfa = new ConverterFieldAdapter<HasId, AppWizardFolderSelector>(awFolderSel, new SplittableToHasIdConverter());
                field = applyDiskResourceValidators(argument, folderSelCfa);
                break;

            case MultiFileSelector:
                AppWizardMultiFileSelector awMultFileSel = new AppWizardMultiFileSelector();
                ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector> multFileSelCfa = new ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector>(awMultFileSel,
                        new SplittableToHasIdListConverter());
                field = applyDiskResourceListValidators(argument, multFileSelCfa);
                break;

            case Text:
                ConverterFieldAdapter<String, TextField> textCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                field = applyStringValidators(argument, textCfa);
                break;

            case EnvironmentVariable:
                ConverterFieldAdapter<String, TextField> envCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                field = applyStringValidators(argument, envCfa);
                break;

            case MultiLineText:
                ConverterFieldAdapter<String, TextArea> mltCfa = new ConverterFieldAdapter<String, TextArea>(new TextArea(), new SplittableToStringConverter());
                field = applyStringValidators(argument, mltCfa);
                break;

            case Number:
                field = getNumberField(argument);
                break;

            case Double:
                ConverterFieldAdapter<Double, NumberField<Double>> dblCfa = new ConverterFieldAdapter<Double, NumberField<Double>>(dblNumField, new SplittableToDoubleConverter());
                field = applyDoubleValidators(argument, dblCfa);
                break;

            case Integer:
                ConverterFieldAdapter<Integer, NumberField<Integer>> intCfa = new ConverterFieldAdapter<Integer, NumberField<Integer>>(intNumField, new SplittableToIntegerConverter());
                field = applyIntegerValidators(argument, intCfa);
                break;

            case Flag:
                CheckBox cb = new CheckBox();
                field = new ConverterFieldAdapter<Boolean, CheckBox>(cb, new SplittableToBooleanConverter());
                break;

            case TextSelection:
                field = new AppWizardComboBox(argument);
                break;

            case IntegerSelection:
                // TBI JDS Currently returning the textual combobox until an appropriate one is developed
                field = new AppWizardComboBox(argument);
                break;

            case DoubleSelection:
                // TBI JDS Currently returning the textual combobox until an appropriate one is developed
                field = new AppWizardComboBox(argument);
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
                field = new AppWizardComboBox(argument);
                break;

            case ValueSelection:
                // TODO JDS Map this to either IntegerSelection or DoubleSelection
                // Currently returning the textual combobox until an appropriate one is developed
                field = new AppWizardComboBox(argument);
                break;

            case Output:
                ConverterFieldAdapter<String, TextField> outputCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                field = applyStringValidators(argument, outputCfa);
                break;

            default:
                GWT.log(AppWizardFieldFactory.class.getName() + ": Unknown " + ArgumentType.class.getName() + " type.");
                field = null;
                break;
        }
        
        setDefaultValue(argument);

        if (field != null) {

            if ((argument.getDescription() != null) && !argument.getDescription().isEmpty()) {
                ToolTipConfig ttCon = new ToolTipConfig(argument.getDescription());
                field.setToolTipConfig(ttCon);
            }
        }

        return field;
    }

    private static void printGwtLogInfo(Argument argument) {
        List<ArgumentValidator> validators = argument.getValidators();
        if (validators != null) {
            GWT.log("Property: " + argument.getLabel() + "; " + argument.getType());
            for (ArgumentValidator v : validators) {
                GWT.log("\tValidator: " + v.getType());
            }
        }        
    }

    /**
     * Returns a NumberField based on any applicable validators. If the property contains Integer
     * validators, then an Integer number field will be returned. Otherwise, a Double number field will
     * be returned.
     * 
     * @param argument
     * @return
     */
    private static ArgumentField getNumberField(Argument argument) {

        NumberField<Double> dblNumField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
        NumberField<Integer> intNumField = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());

        ArgumentField field = null;
        List<ArgumentValidator> validators = argument.getValidators();
        if (validators != null) {
            for (ArgumentValidator validator : validators) {
                if (validator.getType().equals(ArgumentValidatorType.IntAbove) || validator.getType().equals(ArgumentValidatorType.IntBelow) || validator.getType().equals(ArgumentValidatorType.IntRange)) {
                    ConverterFieldAdapter<Integer, NumberField<Integer>> cfa = new ConverterFieldAdapter<Integer, NumberField<Integer>>(intNumField, new SplittableToIntegerConverter());
                    field = applyIntegerValidators(argument, cfa);
                    break;
                }
            }
        }
        if (field == null) {
            ConverterFieldAdapter<Double, NumberField<Double>> cfa = new ConverterFieldAdapter<Double, NumberField<Double>>(dblNumField, new SplittableToDoubleConverter());
            field = applyDoubleValidators(argument, cfa);
        }

        return field;
    }

    private static void setDefaultValue(Argument argument) {
        String defaultValue = argument.getDefaultValue();
        if ((defaultValue != null) && !defaultValue.isEmpty()) {
            try {
                Splittable create = StringQuoter.split(defaultValue);
                argument.setValue(create);
            } catch (Exception e) {
                // If we couldn't parse as a JSON value, create a string splittable.
                Splittable create = StringQuoter.create(defaultValue);
                argument.setValue(create);
            }
        }
    }

    public static SafeHtml createFieldLabelText(Argument argument){
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        if (argument.isRequired()) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired());
        }
        ArgumentType type = argument.getType();
        // JDS Remove the trailing colon. The FieldLabels will apply it automatically.
        SafeHtml label = SafeHtmlUtils.fromString(argument.getLabel().replaceFirst(":$", ""));

        switch (type) {
            case FileInput:
            case FolderInput:
            case MultiFileSelector:
            case Info:
            case Text:
            case EnvironmentVariable:
            case MultiLineText:
            case Integer:
            case Double:
            case Flag:
            case TextSelection:
            case IntegerSelection:
            case DoubleSelection:
            case TreeSelection:
            default:
                labelText.append(templates.fieldLabel(label, argument.getDescription()));
                break;
        }

        return labelText.toSafeHtml();
    }

    static ArgumentField applyIntegerValidators(Argument argument, ConverterFieldAdapter<Integer, ?> field) {
        List<ArgumentValidator> validators = argument.getValidators();
        if (validators != null) {
            for (ArgumentValidator argValidator : validators) {
                switch (argValidator.getType()) {

                    case IntAbove:
                    case IntBelow:
                    case IntRange:
                        field.addValidator(createIntegerValidator(argValidator));
                        break;
                    default:
                        GWT.log("AppWizardFieldFactory.applyIntegerValidators", new UnsupportedOperationException("Unsupported Integer validator type: " + argValidator.getType().toString()));
                        break;
                }
            }
        }
        return field;
    }

    static ArgumentField applyDoubleValidators(Argument argument, ConverterFieldAdapter<Double, ?> field) {
        List<ArgumentValidator> validators = argument.getValidators();
        if (validators != null) {
            for (ArgumentValidator argValidator : validators) {
                switch (argValidator.getType()) {

                    case DoubleAbove:
                    case DoubleBelow:
                    case DoubleRange:
                        field.addValidator(createDoubleValidator(argValidator));
                        break;
                    default:
                        GWT.log("AppWizardFieldFactory.applyDoubleValidators", new UnsupportedOperationException("Unsupported Double validator type: " + argValidator.getType().toString()));
                        break;
                }
            }
        }
        return field;
    }

    static ArgumentField applyStringValidators(Argument argument, ConverterFieldAdapter<String, ?> field) {
        List<ArgumentValidator> validators = argument.getValidators();
        if (validators != null) {
            for (ArgumentValidator argValidator : validators) {
                switch (argValidator.getType()) {
                    case CharacterLimit:
                    case FileName:
                    case Regex:
                        field.addValidator(createStringValidator(argValidator, field));
                        break;
                    default:
                        GWT.log("AppWizardFieldFactory.applyStringValidators", new UnsupportedOperationException("Unsupported String validator type: " + argValidator.getType().toString()));
                        break;
                }
            }
        }
        return field;
    }

    static ArgumentField applyDiskResourceValidators(Argument argument, ConverterFieldAdapter<HasId, ? extends AppWizardDiskResourceSelector<?>> field) {
        // TBI JDS Feature not yet supported
        return field;
    }

    private static ArgumentField applyDiskResourceListValidators(Argument argument, ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector> field) {
        // TBI JDS Feature not yet supported
        return field;
    }

    static Validator<Integer> createIntegerValidator(ArgumentValidator tv) {
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

    static Validator<Double> createDoubleValidator(ArgumentValidator tv) {
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

    static Validator<String> createStringValidator(ArgumentValidator tv, ConverterFieldAdapter<String,?> cfa) {
        Validator<String> validator;
        switch (tv.getType()) {
            case CharacterLimit:
                // Array containing single integer
                int min = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                // Add key down handler to prevent entry once limit is hit.
                cfa.addKeyDownHandler(new PreventEntryAfterLimitHandler(cfa.getField(), min));
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
    
    static class PreventEntryAfterLimitHandler implements KeyDownHandler {
        private final TakesValue<String> hasText;
        private final int limit;

        public PreventEntryAfterLimitHandler(TakesValue<String> hasText, int limit) {
            this.hasText = hasText;
            this.limit = limit;
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (!Strings.isNullOrEmpty(hasText.getValue()) && hasText.getValue().length() > limit) {
                event.preventDefault();
            }
        }
    }
}
