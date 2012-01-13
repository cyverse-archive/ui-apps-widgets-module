package org.iplantc.core.client.widgets.utils;

import com.extjs.gxt.ui.client.widget.form.TextArea;

/**
 * Text area without spell check enabled.
 * 
 * @author amuir
 * 
 */
public class UsefulTextArea extends TextArea {
    /**
     * {@inheritDoc}
     */
    @Override
    protected void afterRender() {
        super.afterRender();
        el().setElementAttribute("spellcheck", "false"); //$NON-NLS-1$ //$NON-NLS-2$
    }
}
