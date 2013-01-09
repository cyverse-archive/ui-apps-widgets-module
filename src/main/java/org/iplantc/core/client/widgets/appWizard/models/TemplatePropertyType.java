package org.iplantc.core.client.widgets.appWizard.models;

public enum TemplatePropertyType {
    FileInput,
    FolderInput,
    MultiFileSelector,
    EnvironmentVariable,
    Flag,
    Info,
    MultiLineText,
    Integer,
    Double,
    Text,
    TextSelection, // For selecting from a list of string values.
    IntegerSelection, // For selecting from a list of integers
    DoubleSelection, // For selecting from a list of doubles
    TreeSelection,
    // Legacy
    Selection, 
    ValueSelection,
    Number;
    // Input
    // Output

}
