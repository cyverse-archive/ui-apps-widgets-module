package org.iplantc.core.uiapps.widgets.client.models;

import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;

/**
 * FIXME JDS This needs to have a corresponding Label for each validator.
 * 
 * @author jstroot
 * 
 */
public enum ArgumentValidatorType {
    IntRange, /** {@link MinNumberValidator} and {@link MaxNumberValidator} */
    IntAbove, /** {@link MinNumberValidator} */
    IntBelow, /** {@link MaxNumberValidator} */
    DoubleRange, /** {@link MinNumberValidator} and {@link MaxNumberValidator} */
    DoubleAbove, /** {@link MinNumberValidator} */
    DoubleBelow, /** {@link MaxNumberValidator} */
    NonEmptyClass,
    GenotypeName,
    FileName,
    IntBelowField,
    IntAboveField,
    ClipperData,
    MustContain,
    SelectOneCheckbox,
    Regex, // TEXT
    CharacterLimit; // TEXT
    
}
