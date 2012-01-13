package org.iplantc.core.client.widgets.utils;

import com.google.gwt.http.client.URL;

public class UrlEncodeFormatter extends GeneralTextFormatter {
    /**
     * {@inheritDoc}
     */
    @Override
    public String format(String value) {
        if (value != null) {
            return super.format(URL.encode(value));
        } else {
            return value;
        }
    }

}
