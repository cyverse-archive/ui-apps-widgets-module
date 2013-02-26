package org.iplantc.core.uiapps.widgets.client.appWizard.view.fields;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.HasText;

public class PreventEntryAfterLimitHandler implements KeyDownHandler {
    private final HasText hasText;
    private final int limit;

    public PreventEntryAfterLimitHandler(HasText hasText, int limit) {
        this.hasText = hasText;
        this.limit = limit;
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (hasText.getText().length() > limit) {
            event.preventDefault();
        }
    }
}