package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Collapsible;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

/**
 * This class implements <code>HasWidgets</code> in order to support manual validation of view via
 * {@link FormPanelHelper#isValid(HasWidgets)}
 * 
 * @author jstroot
 * 
 */
public class ArgumentGroupEditor implements IsWidget, Collapsible, ValueAwareEditor<ArgumentGroup>, HasWidgets {

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

    private static HeaderTextTemplates templates = GWT.create(HeaderTextTemplates.class);

    public ArgumentGroupEditor() {
        BINDER.createAndBindUi(this);
    }
    
    @Override
    public Widget asWidget() {
        return groupField;
    }

    @Override
    public void setDelegate(EditorDelegate<ArgumentGroup> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setValue(ArgumentGroup value) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        for (Argument property : value.getArguments()) {
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
    public void collapse() {
        groupField.collapse();
    }

    @Override
    public void expand() {
        groupField.expand();
    }

    @Override
    public boolean isExpanded() {
        return groupField.isExpanded();
    }

    @Override
    public void add(Widget w) {
        argumentsEditor.add(w);
    }

    @Override
    public void clear() {
        argumentsEditor.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return argumentsEditor.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return argumentsEditor.remove(w);
    }

}
