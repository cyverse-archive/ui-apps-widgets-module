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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class TemplatePropertyListEditor extends Composite implements IsEditor<ListEditor<TemplateProperty, TemplatePropertyEditorAdapter>>{

    interface TemplatePropertyListEditorUiBinder extends UiBinder<VerticalLayoutContainer, TemplatePropertyListEditor> {}
    private static TemplatePropertyListEditorUiBinder BINDER = GWT.create(TemplatePropertyListEditorUiBinder.class);
    
    private class PropertyListEditorSource extends EditorSource<TemplatePropertyEditorAdapter>{
        private final VerticalLayoutContainer con;

        public PropertyListEditorSource(VerticalLayoutContainer con) {
            this.con = con;
        }

        @Override
        public TemplatePropertyEditorAdapter create(int index) {
            TemplatePropertyEditorAdapter subEditor = new TemplatePropertyEditorAdapter();
            con.add(subEditor, new VerticalLayoutData(1, -1));
            return subEditor;
        }
        
        @Override
        public void dispose(TemplatePropertyEditorAdapter subEditor){
            subEditor.removeFromParent();
        }
        
        @Override
        public void setIndex(TemplatePropertyEditorAdapter editor, int index){
            con.insert(editor, index, new VerticalLayoutData(1, -1));
        }
    }

    @Ignore
    @UiField
    VerticalLayoutContainer propertiesContainer;

    private final ListEditor<TemplateProperty, TemplatePropertyEditorAdapter> editor;

    public TemplatePropertyListEditor() {
        initWidget(BINDER.createAndBindUi(this));
        editor = ListEditor.of(new PropertyListEditorSource(propertiesContainer));
    }

    @Override
    public ListEditor<TemplateProperty, TemplatePropertyEditorAdapter> asEditor() {
        return editor;
    }

}
