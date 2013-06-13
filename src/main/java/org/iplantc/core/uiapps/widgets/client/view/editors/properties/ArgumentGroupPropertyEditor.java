package org.iplantc.core.uiapps.widgets.client.view.editors.properties;


import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ArgumentGroupPropertyEditor extends Composite implements ValueAwareEditor<ArgumentGroup> {

    private static ArgumentGroupPropertyEditorUiBinder BINDER = GWT.create(ArgumentGroupPropertyEditorUiBinder.class);

    interface ArgumentGroupPropertyEditorUiBinder extends UiBinder<Widget, ArgumentGroupPropertyEditor> {}

    @UiField
    TextField label;

    private final AppTemplateWizardPresenter presenter;

    private final EventBus eventBus;

    private ArgumentGroup model;

    public ArgumentGroupPropertyEditor(final EventBus eventBus, final AppTemplateWizardPresenter presenter) {
        this.eventBus = eventBus;
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiHandler("label")
    void onStringValueChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }

    @UiHandler("deleteButton")
    void deleteButtonSelectHandler(SelectEvent event) {
        eventBus.fireEvent(new RequestArgumentGroupDeleteEvent(model));
    }

    @Override
    public void setValue(ArgumentGroup value) {
        this.model = value;
    }

    @Override
    public void setDelegate(EditorDelegate<ArgumentGroup> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
