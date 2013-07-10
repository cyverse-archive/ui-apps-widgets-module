package org.iplantc.core.uiapps.widgets.client.view.fields.util;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.TakesValue;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

public class PreventEntryAfterLimitHandler implements KeyDownHandler {
    private final TakesValue<String> hasText;
    private final int limit;

    public PreventEntryAfterLimitHandler(TakesValue<String> hasText, int limit) {
        this.hasText = hasText;
        this.limit = limit;
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        String value = hasText.getValue();
        if (hasText instanceof ValueBaseField<?>) {
            value = ((ValueBaseField<String>)hasText).getCurrentValue();
        }

        int length = 0;
        if (value != null) {
            length = value.length();
        }
        boolean nullOrEmpty = Strings.isNullOrEmpty(value);
        boolean aboveLimit = length >= limit;
        boolean isDeleteOrBackSpace = (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) || (event.getNativeKeyCode() == KeyCodes.KEY_DELETE);
        if (!nullOrEmpty && aboveLimit && !isDeleteOrBackSpace) {
            event.preventDefault();
        }
    }
}