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
     * Localized text for display as a radio-button label for indicating if a file is a "single end"
     * read.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String singleEndRead();

    /**
     * Localized text for display as a radio-button label for indicating if a file as a "paired end"
     * concatenation.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String pairedEndConcat();

    /**
     * Localized text for display as a radio-button label for indicating if a file as a "paired end"
     * association.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String pairedEndAssoc();

    /**
     * Localized text for display as the field label for determine the read length for pre-processing.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String ppLengthOfRead();

    /**
     * Localized text for display as the first part of message for describing concatenated read for
     * selection of a mate pair in pre-processing.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String ppConcatDesc1();

    /**
     * Localized text for display as the second part of message for describing concatenated read for
     * selection of a mate pair in pre-processing.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String ppConcatDesc2();

    /**
     * Localized text for display as a label for selection of a mate pair in pre-processing.
     * 
     * This is used in a Genotype-to-Phenotype workflow for quality control pre-processing.
     * 
     * @return a string representing the localized text.
     */
    String ppSelectMatePair();

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
     * Localized text shown stating the expected inner distance in a standard deviation for a mate pair.
     * 
     * This is used with Genotype-to-Phenotype workflow for selecting next generation sequencing data.
     * 
     * @return a string representing the localized text.
     */
    String expectedInnerDistance();

    /**
     * Localized text shown stating the expected distance in a standard deviation for a mate pair.
     * 
     * This is used with Genotype-to-Phenotype workflow for selecting next generation sequencing data.
     * 
     * @return a string representing the localized text.
     */
    String expectedDistanceDeviation();

    /**
     * Localized text for display as a label when selection of a mate pair is required.
     * 
     * A mate pair is genomic sequence that corresponds to another sequence.
     * 
     * @return a string representing the localized text.
     */
    String matePairSelection();

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
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String authorAttributed();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String acceptedFamily();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String genusMatched();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String epithetMatched();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String authorMatched();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String annotation();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String scientificName();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String unmatched();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String overallMatch();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String more();

    /**
     * The TNRS editor column header.
     * 
     * @return a string representing the localized text.
     */
    String select();

    /**
     * Localized display text for upload file metadata selection.
     * 
     * @return a string representing the localized text.
     */
    String barcodeFile();

    /**
     * The text to display in IBP Single site column selector widget
     * 
     * @return a string containing the localized text.
     */
    String cova();

    /**
     * Localized text for display a context heading for a radio-button group that allows user's to
     * indicated if their file has a "single end" read, "paired end" concatenation, or "paired end"
     * association.
     * 
     * @return a string representing the localized text.
     */
    String fileContains();

    /**
     * Localized text for display as text in the browse button for a file selector.
     * 
     * @return a string representing the localized text.
     */
    String browse();

    String selectFolder();

}
