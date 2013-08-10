package org.iplantc.core.uiapps.widgets.client.view.editors.properties;


import org.iplantc.core.uiapps.widgets.client.events.RequestArgumentGroupDeleteEvent;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardPropertyContentPanelAppearance;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ArgumentGroupPropertyEditor extends Composite implements ValueAwareEditor<ArgumentGroup> {

    private static ArgumentGroupPropertyEditorUiBinder BINDER = GWT.create(ArgumentGroupPropertyEditorUiBinder.class);

    interface ArgumentGroupPropertyEditorUiBinder extends UiBinder<Widget, ArgumentGroupPropertyEditor> {}

    @UiField 
    ContentPanel cp;
    
    @UiField
    TextField label;

    @Ignore
    @UiField
    TextButton deleteButton;

    private final AppTemplateWizardPresenter presenter;

    private ArgumentGroup model;

    public ArgumentGroupPropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiFactory
    ContentPanel createContentPanel() {
        return new ContentPanel(new AppTemplateWizardPropertyContentPanelAppearance());
    }

    @UiHandler("label")
    void onStringValueChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }

    @UiHandler("deleteButton")
    void deleteButtonSelectHandler(SelectEvent event) {
        fireEvent(new RequestArgumentGroupDeleteEvent(model));
    }

    @Override
    public void setValue(ArgumentGroup value) {
    	if(value == null){
    		return;
    	}
    	
        this.model = value;
        if (presenter.isOnlyLabelEditMode()) {
            deleteButton.disable();
        }
        cp.setHeadingHtml(presenter.getAppearance().getPropertyPanelLabels().detailsPanelHeader(value.getLabel()));
    }

    @Override
    public void setDelegate(EditorDelegate<ArgumentGroup> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
