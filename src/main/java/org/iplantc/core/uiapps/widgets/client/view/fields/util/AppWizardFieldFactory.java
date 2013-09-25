package org.iplantc.core.uiapps.widgets.client.view.fields.util;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenomeProperties;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentValueField;
import org.iplantc.core.uiapps.widgets.client.view.fields.CheckBoxAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToBooleanConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToDoubleConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToHasIdConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToHasIdListConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToIntegerConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToReferenceGenomeConverter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.validators.CmdLineArgCharacterValidator;
import org.iplantc.core.uicommons.client.validators.DiskResourceNameValidator;
import org.iplantc.core.uicommons.client.validators.DoubleAboveValidator;
import org.iplantc.core.uicommons.client.validators.DoubleBelowValidator;
import org.iplantc.core.uicommons.client.validators.IntAboveValidator;
import org.iplantc.core.uicommons.client.validators.IntBelowValidator;
import org.iplantc.core.uicommons.client.validators.NumberRangeValidator;
import org.iplantc.core.uicommons.client.widgets.IPlantSideErrorHandler;
import org.iplantc.core.uicommons.client.widgets.PreventEntryAfterLimitHandler;
import org.iplantc.core.uidiskresource.client.views.widgets.AbstractDiskResourceSelector;
import org.iplantc.core.uidiskresource.client.views.widgets.DiskResourceSelector;
import org.iplantc.core.uidiskresource.client.views.widgets.FileSelectorField;
import org.iplantc.core.uidiskresource.client.views.widgets.FolderSelectorField;
import org.iplantc.core.uidiskresource.client.views.widgets.MultiFileSelectorField;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

public class AppWizardFieldFactory {

    private static ListStore<ReferenceGenome> refGenStore = null;
    private static final AppsWidgetsPropertyPanelLabels labels = I18N.APPS_LABELS;
    private static ReferenceGenomeProperties props = GWT.create(ReferenceGenomeProperties.class);

    public static <T extends ArgumentValueField> T createArgumentValueField(Argument argument, boolean editingMode, final AppMetadataServiceFacade appMetadataService) {
        return createArgumentValueField(argument, editingMode, appMetadataService, false);
    }

    /**
     * Creates an ArgumentValueField for use in the default argument editors. These default fields are
     * special cases where all specified validators are applied to the field except the required
     * validator.
     * 
     * @param argument
     * @param appMetadataService
     * @return
     */
    public static <T extends ArgumentValueField> T createDefaultArgumentValueField(Argument argument, final AppMetadataServiceFacade appMetadataService) {
        return createArgumentValueField(argument, false, appMetadataService, true);
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
    protected static <T extends ArgumentValueField> T createArgumentValueField(Argument argument, boolean editingMode, final AppMetadataServiceFacade appMetadataService, boolean isDefault) {
        ConverterFieldAdapter<?, ?> field = null;
        TextField tf = new TextField();
        SpinnerField<Double> dblSpinnerField = new SpinnerField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
        SpinnerField<Integer> intSpinnerField = new SpinnerField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
        tf.setErrorSupport(new IPlantSideErrorHandler(tf));
        dblSpinnerField.setErrorSupport(new IPlantSideErrorHandler(dblSpinnerField));
        intSpinnerField.setErrorSupport(new IPlantSideErrorHandler(intSpinnerField));
        dblSpinnerField.setMinValue(-Double.MAX_VALUE);
        intSpinnerField.setMinValue(Integer.MIN_VALUE);
        switch (argument.getType()) {
            case FileInput:
                FileSelectorField awFileSel = new FileSelectorField();
                if (editingMode) {
                    awFileSel.disableBrowseButton();
                }
                ConverterFieldAdapter<HasId, FileSelectorField> fileSelCfa = new ConverterFieldAdapter<HasId, FileSelectorField>(awFileSel, new SplittableToHasIdConverter());
                field = fileSelCfa;
                if (!editingMode || isDefault) {
                    applyDiskResourceValidators(argument, fileSelCfa);
                }
                break;

            case FolderInput:
                FolderSelectorField awFolderSel = new FolderSelectorField();
                if (editingMode) {
                    awFolderSel.disableBrowseButton();
                }
                ConverterFieldAdapter<HasId, FolderSelectorField> folderSelCfa = new ConverterFieldAdapter<HasId, FolderSelectorField>(awFolderSel, new SplittableToHasIdConverter());
                field = folderSelCfa;
                if (!editingMode || isDefault) {
                    applyDiskResourceValidators(argument, folderSelCfa);
                }
                break;

            case MultiFileSelector:
                MultiFileSelectorField awMultFileSel = new MultiFileSelectorField();
                if (editingMode) {
                    awMultFileSel.setEmptyText(labels.multiFileWidgetEmptyEditText());
                } else {
                    awMultFileSel.setEmptyText(labels.multiFileWidgetEmptyText());
                }
                if (editingMode) {
                    awMultFileSel.disableAddDeleteButtons();
                }
                ConverterFieldAdapter<List<HasId>, MultiFileSelectorField> multFileSelCfa = new ConverterFieldAdapter<List<HasId>, MultiFileSelectorField>(awMultFileSel,
                        new SplittableToHasIdListConverter());
                field = multFileSelCfa;
                if (!editingMode || isDefault) {
                    applyDiskResourceListValidators(argument, multFileSelCfa);
                }
                break;

            case Text:
                ConverterFieldAdapter<String, TextField> textCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                if (editingMode) {
                    tf.setEmptyText(labels.textInputWidgetEmptyEditText());
                } else {
                    tf.setEmptyText(labels.textInputWidgetEmptyText());
                }
                field = textCfa;
                if (!editingMode || isDefault) {
                    tf.addValidator(new CmdLineArgCharacterValidator());
                    applyStringValidators(argument, textCfa);
                }
                break;

            case EnvironmentVariable:
                ConverterFieldAdapter<String, TextField> envCfa = new ConverterFieldAdapter<String, TextField>(tf, new SplittableToStringConverter());
                if (editingMode) {
                    tf.setEmptyText(labels.envVarWidgetEmptyEditText());
                } else {
                    tf.setEmptyText(labels.envVarWidgetEmptyText());
                }
                field = envCfa;
                if (!editingMode || isDefault) {
                    envCfa.addValidator(new CmdLineArgCharacterValidator());
                    applyStringValidators(argument, envCfa);
                }
                break;

            case MultiLineText:
                TextArea textArea = new TextArea();
                ConverterFieldAdapter<String, TextArea> mltCfa = new ConverterFieldAdapter<String, TextArea>(textArea, new SplittableToStringConverter());
                if (editingMode) {
                    textArea.setEmptyText(labels.textInputWidgetEmptyEditText());
                } else {
                    textArea.setEmptyText(labels.textInputWidgetEmptyText());
                }
                field = mltCfa;
                if (!editingMode || isDefault) {
                    textArea.addValidator(new CmdLineArgCharacterValidator(true));
                    applyStringValidators(argument, mltCfa);
                }
                break;

            case Double:
                ConverterFieldAdapter<Double, SpinnerField<Double>> dblCfa = new ConverterFieldAdapter<Double, SpinnerField<Double>>(dblSpinnerField, new SplittableToDoubleConverter());
                if (editingMode) {
                    dblSpinnerField.setEmptyText(labels.doubleInputWidgetEmptyEditText());
                } else {
                    dblSpinnerField.setEmptyText(labels.doubleInputWidgetEmptyText());
                }
                field = dblCfa;
                if (!editingMode || isDefault) {
                    applyDoubleValidators(argument, dblCfa);
                }
                break;

            case Integer:
                ConverterFieldAdapter<Integer, SpinnerField<Integer>> intCfa = new ConverterFieldAdapter<Integer, SpinnerField<Integer>>(intSpinnerField, new SplittableToIntegerConverter());
                if (editingMode) {
                    intSpinnerField.setEmptyText(labels.integerInputWidgetEmptyEditText());
                } else {
                    intSpinnerField.setEmptyText(labels.integerInputWidgetEmptyText());
                }
                field = intCfa;
                if (!editingMode || isDefault) {
                    applyIntegerValidators(argument, intCfa);
                }
                break;

            case Flag:
                CheckBoxAdapter cbAdapter = new CheckBoxAdapter();
                field = new ConverterFieldAdapter<Boolean, CheckBoxAdapter>(cbAdapter, new SplittableToBooleanConverter());
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
                field = outputCfa;
                if (!editingMode || isDefault) {
                    tf.addValidator(new DiskResourceNameValidator());
                    applyStringValidators(argument, outputCfa);
                }
                break;

            case ReferenceAnnotation:
            case ReferenceGenome:
            case ReferenceSequence:
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
        if ((argument.getValidators() != null)) {
            field.applyValidators(argument.getValidators());
        }

        if (!editingMode && !isDefault) {
            setRequiredValidator(argument, field);
        }

        @SuppressWarnings("unchecked")
        T ret = (T)field;
        return ret;
    }

    /**
     * FIXME JDS This should be moved into the AppMetadataServiceFacade
     * 
     * @param appMetadataService
     * @return
     */
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
    static <T> void setRequiredValidator(Argument argument, IsField<T> field) {
        // Exit if the argument is not required, or the field is not an instance of something with an
        // "addValidator" method
        if (!((field instanceof Field<?>) || (field instanceof ConverterFieldAdapter<?, ?>) || (field instanceof ValueBaseField<?>))) {
            return;
        }

        if (argument.getRequired()) {
            EmptyValidator<T> emptyValidator = new EmptyValidator<T>();
            if (field instanceof ValueBaseField<?>) {
                ((ValueBaseField<T>)field).setAllowBlank(false);
            } else if (field instanceof ConverterFieldAdapter<?, ?>) {
                ConverterFieldAdapter<T, ?> converterFieldAdapter = (ConverterFieldAdapter<T, ?>)field;
                if (converterFieldAdapter.getField() instanceof DiskResourceSelector) {
                    ((DiskResourceSelector)converterFieldAdapter.getField()).setRequired(true);
                } else {
                    converterFieldAdapter.addValidator(emptyValidator);
                }
            } else if (field instanceof Field<?>) {
                Field<T> castField = (Field<T>)field;
                castField.addValidator(emptyValidator);
            }

        } else {
            // JDS Clear required validators
            if (field instanceof ValueBaseField<?>) {
                ((ValueBaseField<T>)field).setAllowBlank(true);
            } else if (field instanceof ConverterFieldAdapter<?, ?>) {
                ConverterFieldAdapter<T, ?> converterFieldAdapter = (ConverterFieldAdapter<T, ?>)field;
                if (converterFieldAdapter.getField() instanceof DiskResourceSelector) {
                    ((DiskResourceSelector)converterFieldAdapter.getField()).setRequired(false);
                } else {
                    List<Validator<T>> validators = converterFieldAdapter.getValidators();
                    List<Validator<T>> validatorsToRemove = Lists.newArrayList();
                    for (Validator<T> v : validators) {
                        if (v instanceof EmptyValidator<?>) {
                            validatorsToRemove.add(v);
                        }
                    }
                    for (Validator<?> v : validatorsToRemove) {
                        converterFieldAdapter.removeValidator((Validator<T>)v);
                    }
                }
            } else if (field instanceof Field<?>) {
                Field<T> castField = (Field<T>)field;
                List<Validator<T>> validators = castField.getValidators();
                List<Validator<?>> validatorsToRemove = Lists.newArrayList();
                for (Validator<T> v : validators) {
                    if (v instanceof EmptyValidator<?>) {
                        validatorsToRemove.add(v);
                    }
                }
                for (Validator<?> v : validatorsToRemove) {
                    castField.removeValidator((Validator<T>)v);
                }

            }
        }

    }

    public static void setDefaultValue(Argument argument) {
        argument.setValue(argument.getDefaultValue());
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

    static ConverterFieldAdapter<HasId, ? extends AbstractDiskResourceSelector<?>> applyDiskResourceValidators(Argument argument,
            ConverterFieldAdapter<HasId, ? extends AbstractDiskResourceSelector<?>> field) {
        // TBI JDS Feature not yet supported
        return field;
    }

    static ConverterFieldAdapter<List<HasId>, MultiFileSelectorField> applyDiskResourceListValidators(Argument argument, ConverterFieldAdapter<List<HasId>, MultiFileSelectorField> field) {
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
                validator = new IntAboveValidator(min2);
                break;
            case IntBelow:
                // array of one integer
                int max2 = Double.valueOf(tv.getParams().get(0).asNumber()).intValue();
                validator = new IntBelowValidator(max2);
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
                validator = new DoubleAboveValidator(min2);
                break;
            case DoubleBelow:
                // Array of one double
                double max2 = Double.valueOf(tv.getParams().get(0).asNumber());
                validator = new DoubleBelowValidator(max2);
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
                IntAboveValidator intMinValidator = (IntAboveValidator)createIntegerValidator(tv);
                validator = new DoubleAboveValidator(intMinValidator.getMinNumber().doubleValue());
                break;
            case IntBelow:
                IntBelowValidator intMaxValidator = (IntBelowValidator)createIntegerValidator(tv);
                validator = new DoubleBelowValidator(intMaxValidator.getMaxNumber().doubleValue());
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
                // FIXME CORE-4632
                Splittable regexSplittable = tv.getParams().get(0);
                String regex;
                if (regexSplittable.isNumber()) {
                    Double asNumber = regexSplittable.asNumber();
                    regex = String.valueOf(asNumber.intValue());
                } else {
                    regex = regexSplittable.asString();
                }
                // Array containing one string
                validator = new RegExValidator(regex, "Input must match the Regular Expression: " + regex);
                break;
            case FileName:
                validator = new DiskResourceNameValidator();
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
                validator = new IntAboveValidator(min2);
                break;
            case IntBelow:
                // array of one integer
                int max2 = Double.valueOf(av.getParams().get(0).asNumber()).intValue();
                validator = new IntBelowValidator(max2);
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
                validator = new DoubleAboveValidator(minDbl2);
                break;
            case DoubleBelow:
                // Array of one double
                double maxDbl2 = Double.valueOf(av.getParams().get(0).asNumber());
                validator = new DoubleBelowValidator(maxDbl2);
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
                validator = new DiskResourceNameValidator();
                break;
            default:
                throw new UnsupportedOperationException("Given validator type is not a String validator type.");

        }
        // JDS Add ArgumentValidator metadata
        ab.setTag(ArgumentValidator.VALIDATOR, validator);
        return validator;
    }
}
