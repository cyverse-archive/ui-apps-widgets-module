package org.iplantc.core.uiapps.widgets.client.view.deployedComponents;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.services.DCSearchRPCProxy;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterConfigBean;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class DCSearchField implements IsWidget{

    private final DCSearchRPCProxy searchProxy;

    private ComboBox<DeployedComponent> combo;

    
    public DCSearchField() {
        searchProxy = new DCSearchRPCProxy();
        PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DeployedComponent>> loader = buildLoader();

        ListStore<DeployedComponent> store = buildStore();
        
        loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, DeployedComponent, PagingLoadResult<DeployedComponent>>(
                store));

        final DCTemplate template = GWT.create(DCTemplate.class);

        ListView<DeployedComponent, DeployedComponent> view = buildView(store, template);

        ComboBoxCell<DeployedComponent> cell = buildComboCell(store, view);
        initCombo(loader, cell);
        
    }
    
    private void initCombo(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DeployedComponent>> loader, ComboBoxCell<DeployedComponent> cell) {
        combo = new ComboBox<DeployedComponent>(cell);
        combo.setLoader(loader);
        combo.setMinChars(3);
        combo.setWidth(250);
        combo.setHideTrigger(true);
        combo.setEmptyText(I18N.DISPLAY.searchEmptyText());
        combo.addSelectionHandler(new SelectionHandler<DeployedComponent>() {

            @Override
            public void onSelection(SelectionEvent<DeployedComponent> event) {
                
            }
        });
    }
    
    public void setValue(DeployedComponent value) {
        combo.setValue(value);
    }
    
    public DeployedComponent getValue() {
        return combo.getValue();
    }
    
    public void clear() {
        combo.clear();
    }

    private ComboBoxCell<DeployedComponent> buildComboCell(ListStore<DeployedComponent> store,
            ListView<DeployedComponent, DeployedComponent> view) {
        ComboBoxCell<DeployedComponent> cell = new ComboBoxCell<DeployedComponent>(store,
                new StringLabelProvider<DeployedComponent>() {

                    @Override
                    public String getLabel(DeployedComponent c) {
                        return c.getName() +  " " + c.getVersion();
                    }

                }, view) {
            @Override
            protected void onEnterKeyDown(Context context, Element parent, DeployedComponent value,
                    NativeEvent event, ValueUpdater<DeployedComponent> valueUpdater) {
                if (isExpanded()) {
                    super.onEnterKeyDown(context, parent, value, event, valueUpdater);
                }
            }

        };

        return cell;
    }
    
    private ListView<DeployedComponent, DeployedComponent> buildView(ListStore<DeployedComponent> store,
            final DCTemplate template) {
        ListView<DeployedComponent, DeployedComponent> view = new ListView<DeployedComponent, DeployedComponent>(store,
                new IdentityValueProvider<DeployedComponent>());

        view.setCell(new AbstractCell<DeployedComponent>() {

            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, DeployedComponent value,
                    SafeHtmlBuilder sb) {
                sb.append(template.render(value));
            }

        });
        return view;
    }
    

    private ListStore<DeployedComponent> buildStore() {
        ListStore<DeployedComponent> store = new ListStore<DeployedComponent>(
                new ModelKeyProvider<DeployedComponent>() {

                    @Override
                    public String getKey(DeployedComponent item) {
                        return item.getId();
                    }

                });
        return store;
    }


    private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DeployedComponent>> buildLoader() {
        final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DeployedComponent>> loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DeployedComponent>>(
                searchProxy);
        loader.useLoadConfig(new FilterPagingLoadConfigBean());
        loader.addBeforeLoadHandler(new BeforeLoadHandler<FilterPagingLoadConfig>() {

            @Override
            public void onBeforeLoad(BeforeLoadEvent<FilterPagingLoadConfig> event) {
                String query = combo.getText();
                if (query != null && !query.equals("")) {
                    FilterPagingLoadConfig config = loader.getLastLoadConfig();
                    if(config == null) {
                        config = new FilterPagingLoadConfigBean();
                    }
                    List<FilterConfig> filters = config.getFilters();
                    if (filters.size() == 0) {
                        FilterConfigBean filter = new FilterConfigBean();
                        filter.setValue(query);
                        filters.add(filter);
                    } 
                    
                    filters.get(0).setValue(query);
                    
                    
                }

            }
        });
        return loader;
    }


    interface DCTemplate extends XTemplates {
        @XTemplate(source = "DCSearchResult.html")
        SafeHtml render(DeployedComponent c);
    }


    @Override
    public Widget asWidget() {
       return combo;
    }

}
