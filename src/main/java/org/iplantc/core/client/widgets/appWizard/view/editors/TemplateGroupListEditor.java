package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TemplateGroupListEditor extends Composite implements IsEditor<ListEditor<TemplateGroup, TemplateGroupEditor>>{

    interface GroupListEditorUiBinder extends UiBinder<VerticalLayoutContainer, TemplateGroupListEditor> {}

    private static GroupListEditorUiBinder BINDER = GWT.create(GroupListEditorUiBinder.class);

    private class TemplateGroupEditorSource extends EditorSource<TemplateGroupEditor> {

        private final TemplateGroupListEditor templateGroupListEditor;

        public TemplateGroupEditorSource(TemplateGroupListEditor templateGroupListEditor) {
            this.templateGroupListEditor = templateGroupListEditor;
        }

        @Override
        public TemplateGroupEditor create(int index) {
            TemplateGroupEditor subEditor = new TemplateGroupEditor();
            templateGroupListEditor.getGroupsContainer().insert(subEditor, index);
            return subEditor;
        }
        
        @Override
        public void dispose(TemplateGroupEditor subEditor){
            subEditor.removeFromParent();
        }
        
        @Override
        public void setIndex(TemplateGroupEditor editor, int index){
            templateGroupListEditor.getGroupsContainer().insert(editor, index);
        }

    }
    
    private final ListEditor<TemplateGroup, TemplateGroupEditor> editor = ListEditor.of(new TemplateGroupEditorSource(this));
    
    @Ignore
    @UiField
    VerticalLayoutContainer groupsContainer;


    public TemplateGroupListEditor() {
        initWidget(BINDER.createAndBindUi(this));
    }
    
    

    public InsertResizeContainer getGroupsContainer() {
        return groupsContainer;
    }

    @Override
    public ListEditor<TemplateGroup, TemplateGroupEditor> asEditor() {
        return editor;
    }
}
