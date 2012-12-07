package org.iplantc.core.client.widgets.appWizard.models;


public enum TemplatePropertyType {
    FileInput("FileInput"),
    FolderInput("FolderInput"),
    Info("Info"),
    Text("Text"),
    QuotedText("QuotedText"),
    EnvironmentVariable("EnvironmentVariable"),
    MultiLineText("MultiLineText"),
    Number("Number"),
    Flag("Flag"),
    SkipFlag("SkipFlag"),
    XBasePairs("XBasePairs"),
    XBasePairsText("XBasePairsText"),
    MultiFileSelector("MultiFileSelector"),
    BarcodeSelector("BarcodeSelector"),
    ClipperSelector("ClipperSelector"),
    Selection("Selection"), // For selecting from a list of string values.
    ValueSelection("ValueSelection"), // For selecting from a list of numbers
    TreeSelection("TreeSelection"),
    Percentage("Percentage"),
    DESCRIPTIVE_TEXT("Descriptive Text");

    /**
     * The value which is received in JSON.
     */
    private String valueType;

    private TemplatePropertyType(String valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return valueType;
    }

    // @Override
    // public String toString() {
    // return this.getValue();
    // }
}
