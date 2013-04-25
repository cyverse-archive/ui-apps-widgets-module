package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AppTemplatePropertyEditor extends Composite implements ValueAwareEditor<AppTemplate> {

    private static AppTemplatePropertyEditorUiBinder BINDER = GWT.create(AppTemplatePropertyEditorUiBinder.class);

    interface AppTemplatePropertyEditorUiBinder extends UiBinder<Widget, AppTemplatePropertyEditor> {}

    @Ignore
    @UiField
    TextField tool;

    @Ignore
    @UiField
    TextButton searchBtn;

    @UiField
    @Path("name")
    TextField name;

    @UiField
    @Path("description")
    TextArea description;

    private final AppTemplateWizardPresenter presenter;

    public AppTemplatePropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiHandler({"name", "description"})
    void onNameChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }

    @UiHandler("searchBtn")
    public void onSearchBtnClick(SelectEvent event) {
        presenter.showToolSearchDialog();
    }

    @Override
    public void setValue(AppTemplate value) {
        if (value == null) {
            return;
        }

        if (value.getDeployedComponent() != null) {
            tool.setValue(value.getDeployedComponent().getName());
        }
    }

    @Override
    public void setDelegate(EditorDelegate<AppTemplate> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}
}
