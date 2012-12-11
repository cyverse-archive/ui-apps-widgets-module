package org.iplantc.core.client.widgets.appWizard.models;


public enum TemplatePropertyType {
    FileInput("FileInput"),
    FolderInput("FolderInput"),
    MultiFileSelector("MultiFileSelector"),
    EnvironmentVariable("EnvironmentVariable"),
    Flag("Flag"),
    Info("Info"),
    MultiLineText("MultiLineText"),
    Number("Number"),
    Text("Text"),
    BarcodeSelector("BarcodeSelector"),
    ClipperSelector("ClipperSelector"),
    Selection("Selection"), // For selecting from a list of string values.
    ValueSelection("ValueSelection"), // For selecting from a list of numbers
    TreeSelection("TreeSelection"),
    Percentage("Percentage");

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
