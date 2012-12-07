package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TemplatePropertyListEditor extends Composite implements IsEditor<ListEditor<TemplateProperty, TemplatePropertyEditorAdapter>>{

    interface TemplatePropertyListEditorUiBinder extends UiBinder<Widget, TemplatePropertyListEditor> {}
    private static TemplatePropertyListEditorUiBinder BINDER = GWT.create(TemplatePropertyListEditorUiBinder.class);
    private final ListEditor<TemplateProperty, TemplatePropertyEditorAdapter> editor = ListEditor.of(new PropertyListEditorSource(this));
    
    private class PropertyListEditorSource extends EditorSource<TemplatePropertyEditorAdapter>{
        private final TemplatePropertyListEditor propertyListEditor;

        public PropertyListEditorSource(TemplatePropertyListEditor propertyListEditor) {
            this.propertyListEditor = propertyListEditor; 
        }

        @Override
        public TemplatePropertyEditorAdapter create(int index) {
            TemplatePropertyEditorAdapter subEditor = new TemplatePropertyEditorAdapter();
            propertyListEditor.getPropertiesContainer().insert(subEditor, index);
            return subEditor;
        }
        
        @Override
        public void dispose(TemplatePropertyEditorAdapter subEditor){
            subEditor.removeFromParent();
        }
        
        @Override
        public void setIndex(TemplatePropertyEditorAdapter editor, int index){
            propertyListEditor.getPropertiesContainer().insert(editor, index);
        }
    }
    
    @Ignore
    @UiField
    VerticalLayoutContainer propertiesContainer;
    
    public TemplatePropertyListEditor() {
        initWidget(BINDER.createAndBindUi(this));
    }

    public InsertResizeContainer getPropertiesContainer() {
        return propertiesContainer;
    }

    @Override
    public ListEditor<TemplateProperty, TemplatePropertyEditorAdapter> asEditor() {
        return editor;
    }

}
