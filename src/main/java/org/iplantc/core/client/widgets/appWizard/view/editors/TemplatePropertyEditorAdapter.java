package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.util.AppWizardFieldFactory;
import org.iplantc.core.client.widgets.appWizard.view.fields.AppWizardTextField;
import org.iplantc.core.client.widgets.appWizard.view.fields.TemplatePropertyEditorBase;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

/**
 * Interface definition for <code>TemplateProperty</code> editors. 
 * Implementing classes should contain at least one {@link LeafValueEditor}, most likely a {@link Field} derived class. 
 * 
 * TODO JDS Rename this class to TemplatePropertyEditorAdaptor?
 * @author jstroot
 *
 */
public class TemplatePropertyEditorAdapter implements CompositeEditor<TemplateProperty, TemplateProperty, TemplatePropertyEditorBase>, ValueAwareEditor<TemplateProperty>, IsWidget {
    
    private final FieldLabel propertyLabel = new FieldLabel();
    private TemplatePropertyEditorBase subEditor;

    private TemplateProperty currentValue;
    
    private CompositeEditor.EditorChain<TemplateProperty, TemplatePropertyEditorBase> chain;
    
    
    @Override
    public void setValue(TemplateProperty value) {
        
        currentValue = value;
        
        // Create the TemplateProperty.type specific sub editor
        subEditor = AppWizardFieldFactory.createPropertyField(value);

        propertyLabel.setHTML(AppWizardFieldFactory.createFieldLabelText(value));
        propertyLabel.setWidget(subEditor.getField());
        
        // Attach our subEditor to this editor chain.
        if(value != null){
            chain.attach(value, subEditor);
        }
        
        
    }

    @Override
    public TemplatePropertyEditorBase createEditorForTraversal() {
        return new AppWizardTextField();
    }

    @Override
    public String getPathElement(TemplatePropertyEditorBase subEditor) {
        return "";
    }

    @Override
    public void setEditorChain(CompositeEditor.EditorChain<TemplateProperty, TemplatePropertyEditorBase> chain) {
        this.chain = chain;
    }

    @Override
    public void flush() {
        currentValue = chain.getValue(subEditor);
        
    }

    @Override
    public Widget asWidget() {
        return propertyLabel;
    }

    @Override
    public void onPropertyChange(String... paths) {
    }

    @Override
    public void setDelegate(EditorDelegate<TemplateProperty> delegate) {
    }

}
