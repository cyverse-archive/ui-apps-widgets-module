package org.iplantc.core.uiapps.widgets.client.view.editors.properties;


import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ArgumentGroupPropertyEditor extends Composite implements Editor<ArgumentGroup> {

    private static ArgumentGroupPropertyEditorUiBinder BINDER = GWT.create(ArgumentGroupPropertyEditorUiBinder.class);

    interface ArgumentGroupPropertyEditorUiBinder extends UiBinder<Widget, ArgumentGroupPropertyEditor> {}

    @UiField
    TextField label;

    private final AppTemplateWizardPresenter presenter;

    public ArgumentGroupPropertyEditor(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        initWidget(BINDER.createAndBindUi(this));
    }

    @UiHandler("label")
    void onStringValueChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange();
    }
}
