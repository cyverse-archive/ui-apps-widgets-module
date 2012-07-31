package org.iplantc.core.client.widgets;

import com.google.gwt.i18n.client.Messages;

public interface WidgetDisplayStrings extends Messages {

    /**
     * Message shown when there are no files to display.
     * 
     * @return a string representing the localized text.
     */
    String noFiles();

    /**
     * Localized display text for when File Name needs to be displayed.
     * 
     * @return a string representing the localized text.
     */
    String fileName();

    /**
     * Localized display text for delete buttons.
     * 
     * @return a string representing the localized text.
     */
    String delete();

    /**
     * Localized text for display as button text for adding or inserting data.
     * 
     * @return a string representing the localized text.
     */
    String add();

    /**
     * Localized text for display in a file selection dialog as a caption.
     * 
     * @return a string representing the localized text.
     */
    String selectFile();

    /**
     * Localized display text for a list describing the option of importing a barcode file.
     * 
     * A barcode file is a file containing genomic sequences that are used in sequence files as
     * identifiers.
     * 
     * @return a string representing the localized text.
     */
    String importBarcode();

    /**
     * Localized display text for a list describing the option of creating a new barcode file.
     * 
     * A barcode file is a file containing genomic sequences that are used in sequence files as
     * identifiers.
     * 
     * @return a string representing the localized text.
     */
    String newBarcode();

    /**
     * Localized display text for wizard custom component label.
     * 
     * @return a string representing the localized text.
     */
    String createBarcodeFilename();

    /**
     * Localized display text for wizard custom component label.
     * 
     * @return a string representing the localized text.
     */
    String createBarcodeFileData();

    /**
     * Localized display text for wizard custom component label.
     * 
     * @return a string representing the localized text.
     */
    String browseBarcodeFiles();

    /**
     * Localized text for display as a label when importing a clipper entry.
     * 
     * "Clipper" is a tool from the FASTX toolkit for removing sequencing adapters/linkers.
     * 
     * @return a string representing the localized text.
     */
    String importClipper();

    /**
     * Localized text for display as a label when creating a clipper entry.
     * 
     * "Clipper" is a tool from the FASTX toolkit for removing sequencing adapters/linkers.
     * 
     * @return a string representing the localized text.
     */
    String newClipper();

    /**
     * Localized text for display as a label with the clipper filename textfield.
     * 
     * "Clipper" is a tool from the FASTX toolkit for removing sequencing adapters/linkers.
     * 
     * @return a string representing the localized text.
     */
    String clipperFileName();

    /**
     * Localized text for display with widgets to define clipper filenames
     * 
     * "Clipper" is a tool from the FASTX toolkit for removing sequencing adapters/linkers.
     * 
     * @return a string representing the localized text.
     */
    String createClipperFilename();

    /**
     * Localized text for display with widgets for creating clipper files.
     * 
     * "Clipper" is a tool from the FASTX toolkit for removing sequencing adapters/linkers.
     * 
     * @return a string representing the localized text.
     */
    String createClipperFileData();

    /**
     * Localized text for display with the browse files widget for the clipper tool.
     * 
     * "Clipper" is a tool from the FASTX toolkit for removing sequencing adapters/linkers.
     * 
     * @return a string representing the localized text.
     */
    String browseClipperFiles();

    /**
     * Localized text for display as text in the browse button for a file selector.
     * 
     * @return a string representing the localized text.
     */
    String browse();

    String selectFolder();

}
