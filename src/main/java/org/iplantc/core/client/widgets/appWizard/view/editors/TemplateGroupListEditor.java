package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TemplateGroupListEditor implements IsWidget, IsEditor<ListEditor<TemplateGroup, TemplateGroupEditor>>{

    @UiTemplate("TemplateGroupListEditor.ui.xml")
    interface GroupListEditorUiBinder extends UiBinder<VerticalLayoutContainer, TemplateGroupListEditor> {
    }

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
    
    @UiField
    VerticalLayoutContainer groupsContainer;


    public TemplateGroupListEditor() {
        BINDER.createAndBindUi(this);
    }
    
    

    public InsertResizeContainer getGroupsContainer() {
        return groupsContainer;
    }

    @Override
    public ListEditor<TemplateGroup, TemplateGroupEditor> asEditor() {
        return editor;
    }



    @Override
    public Widget asWidget() {
        return groupsContainer;
    }

}
