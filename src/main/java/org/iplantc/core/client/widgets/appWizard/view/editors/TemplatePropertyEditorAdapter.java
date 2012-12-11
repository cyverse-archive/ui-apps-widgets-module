package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.util.AppWizardFieldFactory;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class TemplatePropertyEditorAdapter extends Composite implements CompositeEditor<TemplateProperty, String, TemplatePropertyEditorBase<String>>, ValueAwareEditor<TemplateProperty> {

    interface TemplatePropertyEditorAdapterUiBinder extends UiBinder<FieldLabel, TemplatePropertyEditorAdapter> {
    }

    private static TemplatePropertyEditorAdapterUiBinder BINDER = GWT.create(TemplatePropertyEditorAdapterUiBinder.class);
    private CompositeEditor.EditorChain<String, TemplatePropertyEditorBase<String>> chain;

    @Ignore
    @UiField
    FieldLabel propertyLabel;
    
    private TemplatePropertyEditorBase<String> subEditor = null;
    

    public TemplatePropertyEditorAdapter() {
        initWidget(BINDER.createAndBindUi(this));
    }
    

    @Override
    public void setValue(TemplateProperty value) {
        // attach it to the chain. Attach the formvalue  
        subEditor = AppWizardFieldFactory.createPropertyField(value);
        
        propertyLabel.setHTML(AppWizardFieldFactory.createFieldLabelText(value));
        propertyLabel.setWidget(subEditor);
        
        String formValue = value.getFormValue();
        chain.attach(formValue, subEditor);
    }

    @Override
    public void setDelegate(EditorDelegate<TemplateProperty> delegate) {}


    @Override
    public void flush() {}


    @Override
    public void onPropertyChange(String... paths) {}


    @Override
    public TemplatePropertyEditorBase<String> createEditorForTraversal() {
        return new AppWizardTextField();
    }


    @Override
    public String getPathElement(TemplatePropertyEditorBase<String> subEditor) {
        return "";
    }


    @Override
    public void setEditorChain(CompositeEditor.EditorChain<String, TemplatePropertyEditorBase<String>> chain) {
        this.chain = chain;
    }

}
