package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.util.AppWizardFieldFactory;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardCheckbox;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class TemplatePropertyEditorAdapter extends Composite implements CompositeEditor<TemplateProperty, Splittable, TemplatePropertyEditorBase>, ValueAwareEditor<TemplateProperty> {

    interface TemplatePropertyEditorAdapterUiBinder extends UiBinder<FieldLabel, TemplatePropertyEditorAdapter> {}

    private static TemplatePropertyEditorAdapterUiBinder BINDER = GWT.create(TemplatePropertyEditorAdapterUiBinder.class);
    private CompositeEditor.EditorChain<Splittable, TemplatePropertyEditorBase> chain;

    @Ignore
    @UiField
    FieldLabel propertyLabel;
    
    private TemplatePropertyEditorBase subEditor = null;
    

    public TemplatePropertyEditorAdapter() {
        initWidget(BINDER.createAndBindUi(this));
    }
    

    @Override
    public void setValue(TemplateProperty value) {
        // attach it to the chain. Attach the formvalue  
        subEditor = AppWizardFieldFactory.createPropertyField(value);
        
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);
        propertyLabel.setWidget(subEditor);
        if (subEditor instanceof AppWizardCheckbox) {
            propertyLabel.setHTML("");
            propertyLabel.setLabelSeparator("");
            ((AppWizardCheckbox)subEditor).setBoxLabel(fieldLabelText.asString());
        }
        
        Splittable formValue = value.getValue();
        chain.attach(formValue, subEditor);
    }

    @Override
    public void setDelegate(EditorDelegate<TemplateProperty> delegate) {}


    @Override
    public void flush() {}


    @Override
    public void onPropertyChange(String... paths) {}


    @Override
    public TemplatePropertyEditorBase createEditorForTraversal() {
        return new AppWizardTextField();
    }


    @Override
    public String getPathElement(TemplatePropertyEditorBase subEditor) {
        return "";
    }


    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Splittable, TemplatePropertyEditorBase> chain) {
        this.chain = chain;
    }

}
