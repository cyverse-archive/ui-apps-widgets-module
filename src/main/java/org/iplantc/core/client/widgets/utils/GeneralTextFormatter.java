package org.iplantc.core.client.widgets.utils;

import org.iplantc.core.jsonutil.JsonUtil;

/**
 * General text formatter. This class escapes newlines.
 * 
 * @author amuir
 * 
 */
public class GeneralTextFormatter implements ComponentValueExportFormatter {
    /**
     * {@inheritDoc}
     */
    @Override
    public String format(String value) {
        return JsonUtil.escapeNewLine(value);
    }
}
