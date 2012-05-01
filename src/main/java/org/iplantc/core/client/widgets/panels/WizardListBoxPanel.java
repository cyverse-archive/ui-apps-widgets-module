package org.iplantc.core.client.widgets.panels;

import java.util.Arrays;
import java.util.List;

import org.iplantc.core.client.widgets.factory.IPlantRuleFactory;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.validator.rules.IPlantRule;
import org.iplantc.core.client.widgets.validator.rules.MustContainRule;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.google.gwt.json.client.JSONObject;

public class WizardListBoxPanel extends WizardWidgetPanel {
    private Label caption;
    private ComboBox<ListItem> selection;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public WizardListBoxPanel(Property property, ComponentValueTable tblComponentVals) {
        super(property, tblComponentVals, null);
    }

    private List<String> getListBoxItems(final MetaDataValidator validator) {
        List<String> ret = null; // assume failure

        // do we have a validator?
        if (validator != null) {
            List<MetaDataRule> rules = validator.getRules();

            for (MetaDataRule mdr : rules) {
                IPlantRule first = IPlantRuleFactory.build(mdr);

                // is the first rule the type we are looking for?
                if (first != null && first instanceof MustContainRule) {
                    MustContainRule source = (MustContainRule)first;
                    ret = source.getItems();
                    break;
                }
            }
        }

        return ret;
    }

    private void initListBox(final Property property) {
        selection = new ComboBox<ListItem>();
        selection.setWidth(350);   // this is the same width DiskResourceSelector uses

        // enable auto-complete
        selection.setEditable(true);
        selection.setTypeAhead(true);
        selection.setQueryDelay(1000);

        selection.setFireChangeEventOnSetValue(true);
        selection.setForceSelection(true);
        selection.setTriggerAction(TriggerAction.ALL);

        // make sure we update our value table when the user changes selections
        selection.addSelectionChangedListener(new SelectionChangedListener<ListItem>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<ListItem> se) {
                updateComponentValueTable();
            }
        });

        ListStore<ListItem> store = new ListStore<ListItem>();
        selection.setStore(store);

        ListItem selectedItem = null;

        // do we have any items to add
        MetaDataValidator validator = property.getValidator();

        List<String> items = getListBoxItems(validator);
        if (items != null) {
            for (String item : items) {
                JSONObject jsonItem = JsonUtil.getObject(item);
                if (jsonItem != null) {
                    // display may be an integer or a string
                    String display = JsonUtil.getRawValueAsString(jsonItem.get("display"));

                    ListItem comboItem = new ListItem(display, jsonItem);

                    store.add(comboItem);

                    if (JsonUtil.getBoolean(jsonItem, "isDefault", false)) {
                        selectedItem = comboItem;
                    }
                }
            }
        }

        if (selectedItem != null) {
            selection.setSelection(Arrays.asList(selectedItem));
        }

        String toolTip = property.getDescription();
        if (toolTip != null && !toolTip.isEmpty()) {
            selection.setTitle(toolTip);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(Property property, List<String> params) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);

        initListBox(property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFormatters(final Property property) {
        String id = selection.getElement().getId();

        tblComponentVals.setFormatter(id, new GeneralTextFormatter());
    }

    /**
     * Retrieve a reference to the widget's underlying list box.
     * 
     * @return a reference to the underlying list box
     */
    protected ComboBox<ListItem> getSelection() {
        return selection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        selection.setId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        IPlantValidator validator = buildValidator(property);

        if (validator != null) {
            selection.setValidator(validator);
            selection.setAutoValidate(true);
            selection.setAllowBlank(!validator.isRequired());

            tblComponentVals.setValidator(property.getId(), validator);
        }
    }

    private IPlantValidator buildValidator(final Property property) {
        IPlantValidator ret = null; // assume failure
        MetaDataValidator mdv = property.getValidator();

        if (mdv != null) {
            ret = new IPlantValidator(tblComponentVals, mdv);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        String id = selection.getId();
        JSONObject selectedValue = null;

        ListItem item = selection.getValue();
        if (item != null) {
            selectedValue = item.getValue();
        }

        tblComponentVals.setValue(id, selectedValue);
        tblComponentVals.validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInitialState(final Property property) {
        String value = property.getValue();

        if (ValidatorHelper.isInteger(value)) {
            int numItems = selection.getStore().getCount();
            int defaultSelection = Integer.parseInt(value);

            if (numItems > 0) {
                if (defaultSelection > numItems) {
                    defaultSelection = 0;
                }

                selection.setSelection(Arrays.asList(selection.getStore().getAt(defaultSelection)));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(caption);
        add(selection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        selection.validate();
    }

    protected class ListItem extends BaseModelData {
        private static final long serialVersionUID = 1927811446049670672L;

        public ListItem(String display, JSONObject json) {
            set("text", display);
            set("value", json);
        }

        public String getDisplay() {
            return get("text");
        }

        public JSONObject getValue() {
            return get("value");
        }
    }

    @Override
    protected void setValue(String value) {
        if (value != null && !value.isEmpty()) {
            JSONObject obj = JsonUtil.getObject(value);
            if (obj != null && obj.get("display") != null) {
                String display = JsonUtil.getRawValueAsString(obj.get("display"));
                for (ListItem item : selection.getStore().getModels()) {
                    if (item.getDisplay().equals(display)) {
                        selection.setSelection(Arrays.asList(item));
                        break;
                    }
                }
            }
        }
    }
}
