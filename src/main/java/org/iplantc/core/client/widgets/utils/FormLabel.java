package org.iplantc.core.client.widgets.utils;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * A label that looks like a FormPanel label.
 */
public class FormLabel extends Label {

    /**
     * Creates a new FormLabel with a given text.
     * 
     * @param text the label text
     */
    public FormLabel(String text) {
        super(text + ":"); //$NON-NLS-1$
        setStyleName("x-form-item"); //$NON-NLS-1$
    }
}
