package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;

/**
 * @author jstroot
 * 
 */
public class TemplateGroupListEditor extends Composite implements IsEditor<ListEditor<TemplateGroup, TemplateGroupEditor>>{

    interface GroupListEditorUiBinder extends UiBinder<AccordionLayoutContainer, TemplateGroupListEditor> {
    }

    private static GroupListEditorUiBinder BINDER = GWT.create(GroupListEditorUiBinder.class);

    private class TemplateGroupEditorSource extends EditorSource<TemplateGroupEditor> {

        private final AccordionLayoutContainer con;

        public TemplateGroupEditorSource(AccordionLayoutContainer con) {
            this.con = con;
        }

        @Override
        public TemplateGroupEditor create(int index) {
            TemplateGroupEditor subEditor = new TemplateGroupEditor();
            con.insert(subEditor, index);
            return subEditor;
        }
        
        @Override
        public void dispose(TemplateGroupEditor subEditor){
            subEditor.asWidget().removeFromParent();
        }
        
        @Override
        public void setIndex(TemplateGroupEditor editor, int index){
            con.insert(editor, index);
        }

    }
    
    @Ignore
    @UiField
    AccordionLayoutContainer groupsContainer;

    private final ListEditor<TemplateGroup, TemplateGroupEditor> editor;

    public TemplateGroupListEditor() {
        initWidget(BINDER.createAndBindUi(this));
        editor = ListEditor.of(new TemplateGroupEditorSource(groupsContainer));
    }

    @Override
    public ListEditor<TemplateGroup, TemplateGroupEditor> asEditor() {
        return editor;
    }
}
