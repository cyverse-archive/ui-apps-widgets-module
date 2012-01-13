package org.iplantc.core.client.widgets.utils;

/**
 * Base formatter interface
 * 
 * @author amuir
 * 
 */
public interface ComponentValueExportFormatter {
    /**
     * Format a string for export.
     * 
     * @param value string to be formatted.
     * @return formatted string for export.
     */
    String format(String value);
}
