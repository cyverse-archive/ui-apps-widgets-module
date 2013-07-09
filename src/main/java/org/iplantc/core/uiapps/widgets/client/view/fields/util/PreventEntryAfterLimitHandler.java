package org.iplantc.core.uiapps.widgets.client.view.fields.util;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.TakesValue;

public class PreventEntryAfterLimitHandler implements KeyDownHandler {
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