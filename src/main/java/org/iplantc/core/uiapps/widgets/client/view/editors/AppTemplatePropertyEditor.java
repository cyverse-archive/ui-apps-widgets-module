package org.iplantc.core.uiapps.widgets.client.view.editors;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AppTemplatePropertyEditor extends Composite implements Editor<AppTemplate> {

    private static AppTemplatePropertyEditorUiBinder BINDER = GWT.create(AppTemplatePropertyEditorUiBinder.class);

    interface AppTemplatePropertyEditorUiBinder extends UiBinder<Widget, AppTemplatePropertyEditor> {}

    /**
     * FIXME JDS This can be bound somehow.
     */
    // @Ignore
    // @UiField
    // TextField tool;

    @UiField
    @Path("name")
    TextField name;

    @UiField
    @Path("description")
    TextField description;

    private final AppTemplateWizardPresenter presenter;

    public AppTemplatePropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiHandler({"name", "description"})
    void onNameChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }
}
