package org.iplantc.core.uiapps.widgets.client.view.fields.util;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidatorType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardComboBox;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardDiskResourceSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardFileSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardFolderSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardMultiFileSelector;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentSelectionField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.treeSelector.SelectionItemTreePanel;
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
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.TakesValue;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class AppWizardFieldFactory {

    interface FieldLabelTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span qtip=\"{1}\">{0}</span>")
        SafeHtml fieldLabel(SafeHtml name, String textToolTip);

        @SafeHtmlTemplates.Template("<span style=\"color: red;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }
    
    private static FieldLabelTextTemplates templates = GWT.create(FieldLabelTextTemplates.class); 
    
    /**
     * Returns an {@link Editor} which contains sub-editors which are bound to an {@link Argument}'s
     * {@link Argument#getSelectionItems()} and {@link Argument#getValue()} properties.
     * 
     * @param argument used to determine the type of "Selector" editor to create and return;
     * @param editingMode a flag which represents whether the return object needs to be created for an
     *            App editor, or not.
     * @return an {@linkplain Editor} whose sub-editors are bound to an {@linkplain Argument}'s
     *         "selectionItem" and "value" properties.
     */
    public static ArgumentSelectionField createArgumentListField(Argument argument, AppTemplateWizardPresenter presenter) {
        if (argument.getSelectionItems() == null) {
            argument.setSelectionItems(Lists.<SelectionItem> newArrayList());
        }
        ArgumentSelectionField field = null;
        switch (argument.getType()) {
            case TextSelection:

            case IntegerSelection:
                // TBI JDS Currently returning the textual combobox until an appropriate one is developed
            case DoubleSelection:
                // TBI JDS Currently returning the textual combobox until an appropriate one is developed

                // Legacy Types
            case Selection:

            case ValueSelection:
                // TODO JDS Map this to either IntegerSelection or DoubleSelection
                field = new AppWizardComboBox();
                break;

            case TreeSelection:
                field = new SelectionItemTreePanel(presenter);
                break;
            default:
                break;
        }
        return field;
    }
    
    public static boolean isSelectionArgumentType(Argument argument){
        ArgumentType t = argument.getType();
        return t.equals(ArgumentType.TextSelection)
                || t.equals(ArgumentType.IntegerSelection)
                || t.equals(ArgumentType.DoubleSelection)
                || t.equals(ArgumentType.Selection)
                || t.equals(ArgumentType.ValueSelection)
                || t.equals(ArgumentType.TreeSelection);
    }

    /**
     * Returns an {@link ArgumentValueField}-derived class based on the given argument's type. This object is
     * meant to be bound with a splittable.
     * 
     * @param argument the argument which is used to determine which Field class to create and return.
     * @param editingMode a flag which represents whether the return object needs to be created for an
     *            App editor, or not.
     * @return an {@link ArgumentValueField}-derived class which can be bound with a {@link Splittable}.
     */
    public static <T extends ArgumentValueField> T createArgumentValueField(Argument argument, boolean editingMode) {
        ConverterFieldAdapter<?, ?> field = null;
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

            /*
             * All Selection types are handled separately, since they need to be bound not only to the
             * Argument's value, but to its list of selectionItems as well.
             */
            case TextSelection:

            case IntegerSelection:
            case DoubleSelection:
            case Selection:
            case ValueSelection:
            case TreeSelection:
                field = null;
                break;

            case Info:
                field = null;
                break;

            case FileOutput:
            case FolderOutput:
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

        if (editingMode) {
            // Remove Field errorsupport
            if ((field != null) && (field.getField() instanceof Field<?>)) {
                Field<?> field2 = (Field<?>)field.getField();

                field2.setValidateOnBlur(false);
                field2.setAutoValidate(false);
                field2.setErrorSupport(null);
                field2.getValidators().clear();

                if (field2 instanceof ValueBaseField<?>) {
                    ((ValueBaseField<?>)field2).setAllowBlank(true);
                }
            }

        }

        if (field != null) {

            if ((argument.getDescription() != null) && !argument.getDescription().isEmpty()) {
                ToolTipConfig ttCon = new ToolTipConfig(argument.getDescription());
                field.setToolTipConfig(ttCon);
            }
        }

        @SuppressWarnings("unchecked")
        T ret = (T)field;
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static <T> void setRequiredValidator(Argument argument, IsField<T> field) {
        // Exit if the argument is not required, or the field is not an instance of something with an
        // "addValidator" method
        if (!argument.getRequired() || !((field instanceof Field<?>) || (field instanceof ConverterFieldAdapter<?, ?>) || (field instanceof ValueBaseField<?>))) {
            return;
        }

        EmptyValidator<T> emptyValidator = new EmptyValidator<T>();
        if (field instanceof ValueBaseField<?>) {
            ((ValueBaseField<T>)field).setAllowBlank(false);
        } else if (field instanceof Field<?>) {
            ((Field<T>)field).addValidator(emptyValidator);
        } else if (field instanceof ConverterFieldAdapter<?, ?>) {
            ((ConverterFieldAdapter<T, ?>)field).addValidator(emptyValidator);
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
    private static ConverterFieldAdapter<?, ?> getNumberField(Argument argument) {

        NumberField<Double> dblNumField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
        NumberField<Integer> intNumField = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());

        ConverterFieldAdapter<?, ?> field = null;
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

    public static void setDefaultValue(Argument argument) {
        argument.setValue(argument.getDefaultValue());
//        String defaultValue = argument.getDefaultValue();
//        if ((defaultValue != null) && !defaultValue.isEmpty()) {
//            try {
//                Splittable create = StringQuoter.split(defaultValue);
//                argument.setValue(create);
//            } catch (Exception e) {
//                // If we couldn't parse as a JSON value, create a string splittable.
//                Splittable create = StringQuoter.create(defaultValue);
//                argument.setValue(create);
//            }
//        }
    }

    public static SafeHtml createFieldLabelText(Argument argument){
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        if (argument.getRequired()) {
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

    static ConverterFieldAdapter<Integer, NumberField<Integer>> applyIntegerValidators(Argument argument, ConverterFieldAdapter<Integer, NumberField<Integer>> field) {
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

    static ConverterFieldAdapter<Double, ?> applyDoubleValidators(Argument argument, ConverterFieldAdapter<Double, ?> field) {
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

    static ConverterFieldAdapter<String, ?> applyStringValidators(Argument argument, ConverterFieldAdapter<String, ?> field) {
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

    static ConverterFieldAdapter<HasId, ? extends AppWizardDiskResourceSelector<?>> applyDiskResourceValidators(Argument argument,
            ConverterFieldAdapter<HasId, ? extends AppWizardDiskResourceSelector<?>> field) {
        // TBI JDS Feature not yet supported
        return field;
    }

    static ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector> applyDiskResourceListValidators(Argument argument, ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector> field) {
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
                validator = new RegExValidator(regex, "Input must match the Regular Expression: " + regex);
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
