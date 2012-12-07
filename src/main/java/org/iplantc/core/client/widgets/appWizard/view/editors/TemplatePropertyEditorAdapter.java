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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

public class TemplatePropertyEditorAdapter extends Composite implements CompositeEditor<TemplateProperty, String, TemplatePropertyEditorBase>, ValueAwareEditor<TemplateProperty>{

    interface TemplatePropertyEditorAdapterUiBinder extends UiBinder<Widget, TemplatePropertyEditorAdapter> {}

    private static TemplatePropertyEditorAdapterUiBinder BINDER = GWT.create(TemplatePropertyEditorAdapterUiBinder.class);
    private CompositeEditor.EditorChain<String, TemplatePropertyEditorBase> chain;

    @Ignore
    @UiField
    FieldLabel propertyLabel;
    
    private TemplatePropertyEditorBase subEditor = null;
    

    public TemplatePropertyEditorAdapter() {
        initWidget(BINDER.createAndBindUi(this));
    }
    

    @Override
    public void setValue(TemplateProperty value) {
        // Use value to get label and set widget on label.
        
        // attach it to the chain. Attach the formvalue  
        String formValue = value.getFormValue();
        subEditor = AppWizardFieldFactory.createPropertyField(value);
        
        propertyLabel.setHTML(AppWizardFieldFactory.createFieldLabelText(value));
        propertyLabel.setWidget(subEditor);
        
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
    public void setEditorChain(CompositeEditor.EditorChain<String, TemplatePropertyEditorBase> chain) {
        this.chain = chain;
    }

}
