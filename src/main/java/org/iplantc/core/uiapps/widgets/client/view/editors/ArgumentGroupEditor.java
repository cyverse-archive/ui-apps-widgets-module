package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * This class contains a {@link ContentPanel} whose header text is updated when the bound
 * <code>ArgumentGroup</code> is set/updated.
 * 
 * @author jstroot
 * 
 */
class ArgumentGroupEditor implements AppTemplateWizard.IArgumentGroupEditor, IsWidget, ValueAwareEditor<ArgumentGroup> {
    interface HeaderTextTemplates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml fieldLabel(String textClassName, SafeHtml name, String textToolTip, String elementName);

        @SafeHtmlTemplates.Template("<span style=\"color: red; float: left;\">*&nbsp</span>")
        SafeHtml fieldLabelRequired();
    }

    private final ContentPanel groupField;
    private final HeaderTextTemplates templates = GWT.create(HeaderTextTemplates.class);
    ArgumentListEditor argumentsEditor;

    @Path("")
    ArgumentGroupPropertyEditor argGrpPropEditor;

    public ArgumentGroupEditor(final EventBus eventBus, AppTemplateWizardPresenter presenter) {
        groupField = new ContentPanel(){
            @Override
            protected void assertPreRender() {
                // KLUDGE JDS Do nothing. This is a workaround for the following bug (which was submitted to the GXT forums); http://www.sencha.com/forum/showthread.php?261470-Adding-new-ContentPanel-to-AccordionLayoutContainer-at-runtime-issue
            }

            @Override
            protected void onClick(Event ce) {
                if (header.getElement().isOrHasChild(ce.getEventTarget().<Element> cast())) {
                    eventBus.fireEvent(new ArgumentGroupSelectedEvent(ArgumentGroupEditor.this));
                }
            }
        };
        groupField.sinkEvents(Event.ONCLICK);
        argumentsEditor = new ArgumentListEditor(eventBus, presenter);
        groupField.add(argumentsEditor);
        if (presenter.isEditingMode()) {
            argGrpPropEditor = new ArgumentGroupPropertyEditor(presenter);
        }
    }
    
    /**
     * This method must return a <code>ContentPanel</code> in order to be added to the
     * <code>AccordionLayoutContainer</code> used in the {@link ArgumentGroupListEditor#groupsContainer}.
     */
    @Override
    public Widget asWidget() {
        return groupField;
    }

    @Override
    public void setValue(ArgumentGroup value) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        for (Argument property : value.getArguments()) {
            if (property.getRequired()) {
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
    public void flush() {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<ArgumentGroup> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public IsWidget getArgumentGroupPropertyEditor() {
        return argGrpPropEditor;
    }

}