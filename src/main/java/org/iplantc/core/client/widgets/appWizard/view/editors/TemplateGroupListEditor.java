package org.iplantc.core.client.widgets.appWizard.view.editors;

import org.iplantc.core.client.widgets.appWizard.models.TemplateGroup;

import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TemplateGroupListEditor extends Composite implements
        IsEditor<ListEditor<TemplateGroup, TemplateGroupEditor>> {
    
    /**
     * This is the panel to which all of the <code>TemplateGroup</code> editors will be added.
     */
    VerticalLayoutContainer groupsContainer;

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

    }
    
    private final ListEditor<TemplateGroup, TemplateGroupEditor> editor = ListEditor.of(new TemplateGroupEditorSource(this));
    
    // 

    @Override
    public ListEditor<TemplateGroup, TemplateGroupEditor> asEditor() {
        return editor;
    }
    
    protected VerticalLayoutContainer getGroupsContainer(){
        return groupsContainer;
    }

}
