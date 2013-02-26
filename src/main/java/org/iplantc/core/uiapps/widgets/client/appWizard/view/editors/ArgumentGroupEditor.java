package org.iplantc.core.uiapps.widgets.client.appWizard.view.editors;

import org.iplantc.core.uiapps.widgets.client.appWizard.models.Argument;
import org.iplantc.core.uiapps.widgets.client.appWizard.models.ArgumentGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Collapsible;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class ArgumentGroupEditor implements IsWidget, ValueAwareEditor<ArgumentGroup>, Collapsible {

    interface HeaderTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml fieldLabel(String textClassName, SafeHtml name, String textToolTip, String elementName);

        @SafeHtmlTemplates.Template("<span style=\"color: red; float: left;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }
    
    interface ArgumentGroupEditorUiBinder extends UiBinder<ContentPanel, ArgumentGroupEditor> {
    }
    
    private static ArgumentGroupEditorUiBinder BINDER = GWT.create(ArgumentGroupEditorUiBinder.class);

    @Ignore
    @UiField
    ContentPanel groupField;

    @UiField
    ArgumentListEditor argumentsEditor;

    private final ContentPanel conPanel;
    private static HeaderTextTemplates templates = GWT.create(HeaderTextTemplates.class);

    // Have to remove the sublist of group editor for the time being until I figure out how to get past GWT complaining about cycles in the editor hierarchy.
    // @UiField
    // ArgumentGroupListEditor groupsEditor;
    
    public ArgumentGroupEditor() {
        conPanel = BINDER.createAndBindUi(this);
    }

    @Override
    public void setDelegate(EditorDelegate<ArgumentGroup> delegate) {
    }

    @Override
    public void flush() {
    }

    @Override
    public void onPropertyChange(String... paths) {
    }

    @Override
    public void setValue(ArgumentGroup value) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        for(Argument property : value.getArguments()){
            if (property.isRequired()) {
                // If any field is required, it needs to be marked as such.
                labelText.append(templates.fieldLabelRequired());
                break;
            }
        }
        // When the value is set, update the FieldSet header text
        // groupField.setHeadingText(value.getLabel());
        labelText.appendEscaped(value.getLabel());
        groupField.setHeadingHtml(labelText.toSafeHtml());

    }

    @Override
    public Widget asWidget() {
        return conPanel;
    }

    @Override
    public void collapse() {
        conPanel.collapse();
    }

    @Override
    public void expand() {
        conPanel.expand();
    }

    @Override
    public boolean isExpanded() {
        return conPanel.isExpanded();
    }

}
