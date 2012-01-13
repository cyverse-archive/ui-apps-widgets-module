package org.iplantc.core.client.widgets.events;

/**
 * A simple listener for validation events.
 * @author hariolf
 *
 */
public interface ValidationListener {

    /**
     * Called after an input component is validated.
     * @param pass whether validation passed or failed
     */
    void onValidation(boolean pass);
}
