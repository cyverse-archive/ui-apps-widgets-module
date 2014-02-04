package org.iplantc.de.apps.widgets.client.models;

import org.iplantc.de.commons.client.validators.DoubleAboveValidator;
import org.iplantc.de.commons.client.validators.DoubleBelowValidator;
import org.iplantc.de.commons.client.validators.IntAboveValidator;
import org.iplantc.de.commons.client.validators.IntBelowValidator;
import org.iplantc.de.commons.client.validators.NumberRangeValidator;

/**
 * FIXME JDS This needs to have a corresponding Label for each validator.
 * 
 * @author jstroot
 * 
 */
public enum ArgumentValidatorType {
    IntRange, /** {@link NumberRangeValidator} */
    IntAbove, /** {@link IntAboveValidator} */
    IntBelow, /** {@link IntBelowValidator} */
    DoubleRange, /** {@link NumberRangeValidator} */
    DoubleAbove, /** {@link DoubleAboveValidator} */
    DoubleBelow, /** {@link DoubleBelowValidator} */
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
