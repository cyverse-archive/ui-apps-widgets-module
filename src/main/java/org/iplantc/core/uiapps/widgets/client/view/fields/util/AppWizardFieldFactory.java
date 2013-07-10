package org.iplantc.core.uiapps.widgets.client.view.fields.util;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidatorType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenomeProperties;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
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
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToReferenceGenomeConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.validators.NumberRangeValidator;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
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
    
    private static ListStore<ReferenceGenome> refGenStore = null;

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
                field = new AppWizardComboBox(presenter);
                break;

            case TreeSelection:
                field = new SelectionItemTreePanel(presenter);
                break;
            default:
                break;
        }
        return field;
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
    public static <T extends ArgumentValueField> T createArgumentValueField(Argument argument, boolean editingMode, final AppMetadataServiceFacade appMetadataService) {
        ConverterFieldAdapter<?, ?> field = null;
        TextField tf = new TextField();
        SpinnerField<Double> dblSpinnerField = new SpinnerField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
        SpinnerField<Integer> intSpinnerField = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        dblSpinnerField.setMinValue(-Double.MAX_VALUE);
        intSpinnerField.setMinValue(Integer.MIN_VALUE);
        NameValidator3 nameValidator = new NameValidator3();
        switch (argument.getType()) {
            case FileInput:
                AppWizardFileSelector awFileSel = new AppWizardFileSelector();
                if (editingMode) {
                    awFileSel.disableBrowseButton();
                }
                ConverterFieldAdapter<HasId, AppWizardFileSelector> fileSelCfa = new ConverterFieldAdapter<HasId, AppWizardFileSelector>(awFileSel, new SplittableToHasIdConverter());
                field = applyDiskResourceValidators(argument, fileSelCfa);
                break;

            case FolderInput:
                AppWizardFolderSelector awFolderSel = new AppWizardFolderSelector();
                if (editingMode) {
                    awFolderSel.disableBrowseButton();
                }
                ConverterFieldAdapter<HasId, AppWizardFolderSelector> folderSelCfa = new ConverterFieldAdapter<HasId, AppWizardFolderSelector>(awFolderSel, new SplittableToHasIdConverter());
                field = applyDiskResourceValidators(argument, folderSelCfa);
                break;

            case MultiFileSelector:
                AppWizardMultiFileSelector awMultFileSel = new AppWizardMultiFileSelector();
                if (editingMode) {
                    awMultFileSel.disableAddDeleteButtons();
                }
                ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector> multFileSelCfa = new ConverterFieldAdapter<List<HasId>, AppWizardMultiFileSelector>(awMultFileSel,
                        new SplittableToHasIdListConverter());
                field = applyDiskResourceListValidators(argument, multFileSelCfa);
                break;

            case Text:
                ConverterFieldAdapter<String, TextField> textCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                textCfa.addValidator(nameValidator);
                field = applyStringValidators(argument, textCfa);
                break;

            case EnvironmentVariable:
                ConverterFieldAdapter<String, TextField> envCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                envCfa.addValidator(nameValidator);
                field = applyStringValidators(argument, envCfa);
                break;

            case MultiLineText:
                ConverterFieldAdapter<String, TextArea> mltCfa = new ConverterFieldAdapter<String, TextArea>(new TextArea(), new SplittableToStringConverter());
                mltCfa.addValidator(nameValidator);
                field = applyStringValidators(argument, mltCfa);
                break;

            case Number:
                field = getNumberField(argument);
                break;

            case Double:
                ConverterFieldAdapter<Double, SpinnerField<Double>> dblCfa = new ConverterFieldAdapter<Double, SpinnerField<Double>>(dblSpinnerField, new SplittableToDoubleConverter());
                field = applyDoubleValidators(argument, dblCfa);
                break;

            case Integer:
                ConverterFieldAdapter<Integer, SpinnerField<Integer>> intCfa = new ConverterFieldAdapter<Integer, SpinnerField<Integer>>(intSpinnerField, new SplittableToIntegerConverter());
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
            case MultiFileOutput:
            case Output:
                ConverterFieldAdapter<String, TextField> outputCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                outputCfa.addValidator(nameValidator);
                field = applyStringValidators(argument, outputCfa);
                break;

            case ReferenceAnnotation:
            case ReferenceGenome:
            case ReferenceSequence:
                ReferenceGenomeProperties props = appMetadataService.getReferenceGenomeProperties();
                final ListStore<ReferenceGenome> store = getReferenceGenomeStore(appMetadataService);
                ComboBox<ReferenceGenome> comboBox = new ComboBox<ReferenceGenome>(store, props.name());
                comboBox.setTriggerAction(TriggerAction.ALL);
                ConverterFieldAdapter<ReferenceGenome, ComboBox<ReferenceGenome>> refGenCfa = new ConverterFieldAdapter<ReferenceGenome, ComboBox<ReferenceGenome>>(comboBox,
                        new SplittableToReferenceGenomeConverter());
                field = refGenCfa;
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

    private static ListStore<ReferenceGenome> getReferenceGenomeStore(final AppMetadataServiceFacade appMetadataService) {
        if (refGenStore == null) {
            final ReferenceGenomeProperties referenceGenomeProperties = appMetadataService.getReferenceGenomeProperties();
            refGenStore = new ListStore<ReferenceGenome>(referenceGenomeProperties.id());

            appMetadataService.getReferenceGenomes(new AsyncCallback<List<ReferenceGenome>>() {

                @Override
                public void onSuccess(List<ReferenceGenome> result) {
                    if (refGenStore.getAll().isEmpty()) {
                        refGenStore.addAll(result);
                        refGenStore.addSortInfo(new StoreSortInfo<ReferenceGenome>(referenceGenomeProperties.nameValue(), SortDir.ASC));
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(caught);
                }
            });
        }
        return refGenStore;
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

    static ConverterFieldAdapter<Integer, ?> applyIntegerValidators(Argument argument, ConverterFieldAdapter<Integer, ?> field) {
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

                    /*
                     * JDS Special handling for old Double arguments which may contain Integer
                     * validators. So, we simply pass these on.
                     */
                    case IntAbove:
                    case IntBelow:
                    case IntRange:
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
        AutoBean<ArgumentValidator> ab = AutoBeanUtils.getAutoBean(tv);
        ab.setTag(ArgumentValidator.VALIDATOR, validator);
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
    
            /*
             * JDS Special handling for old Double arguments which may contain Integer validators. So,
             * let's convert them.
             */
            case IntRange:
                NumberRangeValidator<Integer> intRngValidator = (NumberRangeValidator<Integer>)createIntegerValidator(tv);
                validator = new NumberRangeValidator<Double>(intRngValidator.getMinNumber().doubleValue(), intRngValidator.getMaxNumber().doubleValue());
                break;
            case IntAbove:
                MinNumberValidator<Integer> intMinValidator = (MinNumberValidator<Integer>)createIntegerValidator(tv);
                validator = new MinNumberValidator<Double>(intMinValidator.getMinNumber().doubleValue());
                break;
            case IntBelow:
                MaxNumberValidator<Integer> intMaxValidator = (MaxNumberValidator<Integer>)createIntegerValidator(tv);
                validator = new MaxNumberValidator<Double>(intMaxValidator.getMaxNumber().doubleValue());
                break;
            default:
                throw new UnsupportedOperationException("Given validator type is not a Double validator type.");
    
        }
        AutoBean<ArgumentValidator> ab = AutoBeanUtils.getAutoBean(tv);
        ab.setTag(ArgumentValidator.VALIDATOR, validator);
        return validator;
    }

    static Validator<String> createStringValidator(ArgumentValidator tv, ConverterFieldAdapter<String,?> cfa) {
        AutoBean<ArgumentValidator> ab = AutoBeanUtils.getAutoBean(tv);
        Validator<String> validator;
        switch (tv.getType()) {
            case CharacterLimit:
                // Array containing single integer
                int min = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                // Add key down handler to prevent entry once limit is hit.
                PreventEntryAfterLimitHandler handler = new PreventEntryAfterLimitHandler(cfa.getField(), min);
                HandlerRegistration addKeyDownHandlerReg = cfa.addKeyDownHandler(handler);

                validator = new MaxLengthValidator(min);

                // JDS Add ArgumentValidator metadata
                ab.setTag(ArgumentValidator.KEY_DOWN_HANDLER, handler);
                ab.setTag(ArgumentValidator.KEY_DOWN_HANDLER_REG, addKeyDownHandlerReg);
                ab.setTag(ArgumentValidator.VALIDATOR, validator);
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
        ab.setTag(ArgumentValidator.VALIDATOR, validator);
        return validator;
    }
    
    public static Validator<?> createValidator(ArgumentValidator av) {
        AutoBean<ArgumentValidator> ab = AutoBeanUtils.getAutoBean(av);
        Validator<?> validator;
        switch (av.getType()) {
            case IntRange:
                // array of two integers
                int minInt = Double.valueOf(av.getParams().get(0).asNumber()).intValue();
                int maxInt = Double.valueOf(av.getParams().get(1).asNumber()).intValue();
                validator = new NumberRangeValidator<Integer>(minInt, maxInt);
                break;
            case IntAbove:
                // array of one integer
                int min2 = Double.valueOf(av.getParams().get(0).asNumber()).intValue();
                validator = new MinNumberValidator<Integer>(min2);
                break;
            case IntBelow:
                // array of one integer
                int max2 = Double.valueOf(av.getParams().get(0).asNumber()).intValue();
                validator = new MaxNumberValidator<Integer>(max2);
                break;
            case DoubleRange:
                // Array of two doubles
                double minDbl = Double.valueOf(av.getParams().get(0).asNumber());
                double maxDbl = Double.valueOf(av.getParams().get(1).asNumber());
                validator = new NumberRangeValidator<Double>(minDbl, maxDbl);
                break;
            case DoubleAbove:
                // Array of one double
                double minDbl2 = Double.valueOf(av.getParams().get(0).asNumber());
                validator = new MinNumberValidator<Double>(minDbl2);
                break;
            case DoubleBelow:
                // Array of one double
                double maxDbl2 = Double.valueOf(av.getParams().get(0).asNumber());
                validator = new MaxNumberValidator<Double>(maxDbl2);
                break;
            case CharacterLimit:
                // Array containing single integer
                int maxCharLimit = Double.valueOf(av.getParams().get(0).asNumber()).intValue();
                validator = new MaxLengthValidator(maxCharLimit);

                break;
            case Regex:
                // Array containing one string
                String regex = av.getParams().get(0).asString();
                validator = new RegExValidator(regex, "Input must match the Regular Expression: " + regex);
                break;
            case FileName:
                validator = new NameValidator3();
                break;
            default:
                throw new UnsupportedOperationException("Given validator type is not a String validator type.");

        }
        // JDS Add ArgumentValidator metadata
        ab.setTag(ArgumentValidator.VALIDATOR, validator);
        return validator;
    }
}
