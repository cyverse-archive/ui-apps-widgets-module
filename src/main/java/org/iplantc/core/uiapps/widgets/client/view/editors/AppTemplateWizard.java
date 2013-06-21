package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.dialog.DCListingDialog;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent.AppTemplateUpdatedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.AppTemplatePropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.ArgumentPropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.ContentPanelHoverHeaderSelectionAppearance;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;
import org.iplantc.de.client.UUIDService;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.impl.Refresher;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

/**
 * A wizard for editing <code>AppTemplate</code>s.
 * 
 * This view operates in one of two modes; editing or not editing.
 * While editing, selection of the {@link Argument} field within the the {@link ArgumentGroup}
 * <code>ContentPanel</code> is enabled. Selecting an Argument field will fire an
 * {@link ArgumentSelectedEvent} through the event bus. Then, the Argument
 * field's corresponding {@link ArgumentPropertyEditor} can be retrieved.
 * 
 * If editing is not enabled, the {@link ArgumentPropertyEditor} will not be available, and will not be
 * initialized with the editor hierarchy.
 * 
 * @author jstroot
 * 
 */
public class AppTemplateWizard extends Composite implements HasPropertyEditor, ValueAwareEditor<AppTemplate>, AppTemplateWizardPresenter {
    
    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppTemplateWizard> {}
    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);
    private final AppTemplateWizardPresenter.Resources res = GWT.create(AppTemplateWizardPresenter.Resources.class);
    
    private final ContentPanel con;
    ArgumentGroupListEditor argumentGroups;

    @Path("")
    AppTemplatePropertyEditor appTemplatePropEditor;

    private final boolean editingMode;
    private AppTemplate appTemplate;

    private Object valueChangeEventSource;
    
    public AppTemplateWizard(boolean editingMode) {
        res.selectionCss().ensureInjected();
        this.editingMode = editingMode;
        argumentGroups = new ArgumentGroupListEditor(this, GWT.<UUIDServiceAsync> create(UUIDService.class));

        ContentPanelAppearance cpAppearance;
        if (editingMode) {
            cpAppearance = new ContentPanelHoverHeaderSelectionAppearance();
        } else {
            cpAppearance = GWT.create(ContentPanelAppearance.class);
        }
        con = new ContentPanel(cpAppearance) {
            @Override
            protected void onClick(Event ce) {
                if (header.getElement().isOrHasChild(ce.getEventTarget().<Element> cast())) {
                    AppTemplateWizard.this.fireEvent(new AppTemplateSelectedEvent(appTemplatePropEditor));
                }
            }
        };
        con.add(argumentGroups);

        if (editingMode) {
            con.getHeader().addStyleName(res.selectionCss().selectionTargetBg());
            appTemplatePropEditor = new AppTemplatePropertyEditor(this);
            con.sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
        } else {
            con.setHeaderVisible(false);
        }

        initWidget(con);
        editorDriver.initialize(this);
    }

    /**
     * This method binds this component with the given <code>AppTemplate</code> instance.
     * 
     * @see {@link EditorDriver#edit(AppTemplate)}
     * @param apptemplate
     */
    public void edit(AppTemplate apptemplate) {
        editorDriver.edit(apptemplate);
    }

    /**
     * This method updates the bound <code>AppTemplate</code> with the current
     * values from the editor.
     * 
     * @see {@link EditorDriver#flush()}
     * @return
     */
    public AppTemplate flushAppTemplate() {
        return editorDriver.flush();
    }

    @Override
    public void onArgumentPropertyValueChange() {
        AppTemplate atTmp = flushAppTemplate();
        editorDriver.accept(new Refresher());
        fireEvent(new AppTemplateUpdatedEvent(this, atTmp));
        valueChangeEventSource = null;
    }

    @Override
    public void onArgumentPropertyValueChange(Object source) {
        valueChangeEventSource = source;
        onArgumentPropertyValueChange();
    }

    public boolean hasErrors() {
        return editorDriver.hasErrors();
    }

    public List<EditorError> getErrors() {
        return editorDriver.getErrors();
    }

    @Override
    public boolean isEditingMode() {
        return editingMode;
    }

    public void collapseAllArgumentGroups() {
        argumentGroups.collapseAllArgumentGroups();
    }

    @Override
    public void setValue(AppTemplate value) {
        this.appTemplate = value;
        if (isEditingMode()) {
            SafeHtmlBuilder labelText = new SafeHtmlBuilder();
            labelText.append(SafeHtmlUtils.fromString(value.getName()));
            con.setHeadingHtml(labelText.toSafeHtml());
        }
    }

    @Override
    public IsWidget getPropertyEditor() {
        return appTemplatePropEditor;
    }

    @Override
    public void setDelegate(EditorDelegate<AppTemplate> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void showToolSearchDialog() {
        final DCListingDialog dialog = new DCListingDialog();
        dialog.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                DeployedComponent dc = dialog.getSelectedComponent();
                // Set the deployed component in the AppTemplate
                if ((dc != null) && (appTemplate != null)) {
                    appTemplate.setDeployedComponent(dc);
                    if (getValueChangeEventSource() != dialog) {
                        onArgumentPropertyValueChange(dialog);
                    }
                }
            }
        });
        dialog.show();
    }

    public void insertFirstInAccordion(IsWidget widget) {
        argumentGroups.insertFirstInAccordion(widget);
    }

    @Override
    public Object getValueChangeEventSource() {
        return valueChangeEventSource;
    }

    @Override
    public SelectionCss getSelectionCss() {
        return res.selectionCss();
    }

    public void addAppTemplateSelectedEventHandler(AppTemplateSelectedEventHandler handler) {
        addHandler(handler, AppTemplateSelectedEvent.TYPE);
    }

    public void addAppTemplateUpdatedEventHandler(AppTemplateUpdatedEventHandler handler) {
        addHandler(handler, AppTemplateUpdatedEvent.TYPE);
    }

    public void addArgumentSelectedEventHandler(ArgumentSelectedEventHandler handler) {
        addHandler(handler, ArgumentSelectedEvent.TYPE);
    }

    public void addArgumentGroupSelectedEventHandler(ArgumentGroupSelectedEventHandler handler) {
        addHandler(handler, ArgumentGroupSelectedEvent.TYPE);
    }
}
