package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResource;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceModelKeyProvider;
import org.iplantc.core.uidiskresource.client.models.autobeans.DiskResourceProperties;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * TODO JDS Implement drag and drop
 * XXX Must support multi select.
 * 
 * @author jstroot
 * 
 */
public class AppWizardMultiFileSelector extends Composite implements TemplatePropertyEditorBase {

    private static AppWizardMultiFileSelectorUiBinder BINDER = GWT.create(AppWizardMultiFileSelectorUiBinder.class);

    interface AppWizardMultiFileSelectorUiBinder extends UiBinder<Widget, AppWizardMultiFileSelector> {}

    @UiField
    ToolBar toolbar;

    @UiField
    TextButton addButton;

    @UiField
    TextButton deleteButton;

    @UiField
    Grid<DiskResource> grid;

    @UiField
    GridView<DiskResource> gridView;

    @UiField
    ListStore<DiskResource> listStore;

    @UiField()
    ColumnModel<DiskResource> cm;

    public AppWizardMultiFileSelector() {
        initWidget(BINDER.createAndBindUi(this));

        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<DiskResource>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<DiskResource> event) {
                List<DiskResource> selection = event.getSelection();
                deleteButton.setEnabled((selection != null) && !selection.isEmpty());
            }
        });
    }

    @UiFactory
    ColumnModel<DiskResource> createColumnModel() {
        List<ColumnConfig<DiskResource, ?>> list = Lists.newArrayList();
        DiskResourceProperties props = GWT.create(DiskResourceProperties.class);

        ColumnConfig<DiskResource, String> name = new ColumnConfig<DiskResource, String>(props.name(), 130, I18N.DISPLAY.name());
        list.add(name);
        return new ColumnModel<DiskResource>(list);
    }

    @UiFactory
    ListStore<DiskResource> createListStore() {
        return new ListStore<DiskResource>(new DiskResourceModelKeyProvider());
    }

    @UiHandler("addButton")
    void onAddButtonSelected(SelectHandler handler) {

    }

    @UiHandler("deleteButton")
    void onDeleteButtonSelected(SelectHandler handler) {
        for (DiskResource dr : grid.getSelectionModel().getSelectedItems()) {
            listStore.remove(dr);
        }
    }

    @Override
    public void setValue(Splittable value) {
        // Assume the incoming value is a JSON array

    }

    @Override
    public Splittable getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    // // Validators are intentionally not implemented for this editor.
    // @Override
    // public void addValidator(Validator<String> validator) {}
    //
    // @Override
    // public void removeValidator(Validator<String> validator) {}
    //
    // @Override
    // public List<Validator<String>> getValidators() {return null;}

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {return null;}

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {return null;}

}
