package org.iplantc.core.client.widgets.appWizard.models;


public enum TemplatePropertyType {
    FILE_INPUT("FileInput"),
    FOLDER_INPUT("FolderInput"),
    INFO("Info"),
    TEXT("Text"),
    QUOTED_TEXT("QuotedText"),
    ENV_VARIABLE("EnvironmentVariable"),
    MULTI_LINE_TEXT("MultiLineText"),
    NUMBER("Number"),
    FLAG("Flag"),
    SKIP_FLAG("SkipFlag"),
    X_BASE_PAIRS("XBasePairs"),
    X_BASE_PAIRS_TEXT("XBasePairsText"),
    MULTI_FILE_SELECTOR("MultiFileSelector"),
    BARCODE_SELECTOR("BarcodeSelector"),
    CLIPPER_SELECTOR("ClipperSelector"),
    SELECTION("Selection"),
    VALUE_SELECTION("ValueSelection"),
    TREE_SELECTION("TreeSelection"),
    PERCENTAGE("Percentage"),
    DESCRIPTIVE_TEXT("Descriptive Text");

    /**
     * The value which is received in JSON.
     */
    private String valueType;

    private TemplatePropertyType(String valueType) {
        this.valueType = valueType;
    }

    @Override
    public String toString() {
        return valueType;
    }
}
