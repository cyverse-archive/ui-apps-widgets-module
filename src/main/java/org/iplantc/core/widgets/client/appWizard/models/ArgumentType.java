package org.iplantc.core.widgets.client.appWizard.models;

public enum ArgumentType {
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
    Output,
    // Legacy
    Selection, 
    ValueSelection,
    Number;
    // Input
    // Output

}
