package org.iplantc.core.client.widgets.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.ListRuleArgument;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentFactory;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentGroup;

import com.extjs.gxt.ui.client.widget.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.TreeView;

/**
 * A WizardSelectorPanel for displaying a hierarchical list selector widget in a wizard.
 * 
 * @author psarando
 * 
 */
public class WizardTreeSelectorPanel extends WizardSelectorPanel {

    private ListRuleArgumentFactory factory;
    private Tree<ListRuleArgument, String> tree;
    private Label caption;

    /**
     * Instantiate from a property, component value table.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public WizardTreeSelectorPanel(Property property, ComponentValueTable tblComponentVals) {
        super(property, tblComponentVals);
    }

    @Override
    protected void initInstanceVariables(Property property, List<String> params) {
        factory = GWT.create(ListRuleArgumentFactory.class);

        TreeStore<ListRuleArgument> store = new TreeStore<ListRuleArgument>(
                new ModelKeyProvider<ListRuleArgument>() {
                    @Override
                    public String getKey(ListRuleArgument item) {
                        return item.getId();
                    }
                });

        ValueProvider<ListRuleArgument, String> valueProvider = new ValueProvider<ListRuleArgument, String>() {

            @Override
            public String getValue(ListRuleArgument object) {
                return object.getDisplay();
            }

            @Override
            public void setValue(ListRuleArgument object, String value) {
                // intentionally do nothing; this is not an editor
            }

            @Override
            public String getPath() {
                return "display"; //$NON-NLS-1$
            }
        };

        tree = new Tree<ListRuleArgument, String>(store, valueProvider) {
            @Override
            protected SafeHtml renderChild(ListRuleArgument parent, ListRuleArgument child, int depth,
                    TreeView.TreeViewRenderMode renderMode) {
                SafeHtml html = super.renderChild(parent, child, depth, renderMode);

                String tooltip = child.getDescription();
                if (tooltip == null || tooltip.isEmpty()) {
                    return html;
                }

                SafeHtmlBuilder builder = new SafeHtmlBuilder();
                builder.appendHtmlConstant("<span title='" + tooltip + "'>");
                builder.append(html);
                builder.appendHtmlConstant("</span>");

                return builder.toSafeHtml();
            }
        };

        tree.addCheckChangedHandler(new CheckChangedHandler<ListRuleArgument>() {
            @Override
            public void onCheckChanged(CheckChangedEvent<ListRuleArgument> event) {
                updateComponentValueTable();
            }
        });

        tree.setCheckable(true);
        tree.setCheckStyle(CheckCascade.TRI);
        tree.setAutoLoad(true);
        tree.setBorders(true);
    }

    @Override
    protected void setWidgetId(String id) {
        tree.setId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        tblComponentVals.setValidator(property.getId(), buildValidator(property));
    }

    @Override
    protected void setInitialState(final Property property) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);

        List<String> items = getMustContainRuleItems(property);

        if (items != null) {
            TreeStore<ListRuleArgument> store = tree.getStore();
            List<ListRuleArgument> defaultSelection = new ArrayList<ListRuleArgument>();

            for (String item : items) {
                AutoBean<ListRuleArgumentGroup> bean = AutoBeanCodex.decode(factory,
                        ListRuleArgumentGroup.class, item);

                if (bean == null) {
                    continue;
                }

                ListRuleArgumentGroup root = bean.as();
                if (root != null) {
                    if (root.getGroups() != null) {
                        for (ListRuleArgumentGroup group : root.getGroups()) {
                            defaultSelection.addAll(addGroupToStore(null, group));
                        }
                    }
                    if (root.getArguments() != null) {
                        for (ListRuleArgument ruleArg : root.getArguments()) {
                            store.add(ruleArg);
                            if (ruleArg.isDefault()) {
                                defaultSelection.add(ruleArg);
                            }
                        }
                    }
                }
            }

            if (!defaultSelection.isEmpty()) {
                tree.setCheckedSelection(defaultSelection);
            }
        }
    }

    @Override
    protected void updateComponentValueTable() {
        List<ListRuleArgument> selected = tree.getCheckedSelection();
        JSONArray selectedValues = null;

        if (selected != null && !selected.isEmpty()) {
            selectedValues = new JSONArray();
            int jsonArrayIndex = 0;

            for (ListRuleArgument ruleArg : selected) {
                if (!(ruleArg instanceof ListRuleArgumentGroup)) {
                    AutoBean<ListRuleArgument> bean = AutoBeanUtils.getAutoBean(ruleArg);
                    selectedValues.set(jsonArrayIndex++,
                            JSONParser.parseStrict(AutoBeanCodex.encode(bean).getPayload()));
                }
            }
        }

        tblComponentVals.setValue(tree.getId(), selectedValues);
        tblComponentVals.validate();
    }

    @Override
    protected void compose() {
        add(caption);
        add(tree);
    }

    @Override
    protected void setValue(String value) {
        // not supported at this time
        // default values are set in setInitialState
    }

    /**
     * Adds the group to the parent, or as a root if parent is null, then adds group's children to the
     * store. Returns a list of any items that should be selected by default.
     * 
     * @param parent The parent of group.
     * @param group The group to add to the store.
     * @return A list of any items that should be selected by default.
     */
    private List<ListRuleArgument> addGroupToStore(ListRuleArgumentGroup parent,
            ListRuleArgumentGroup group) {
        List<ListRuleArgument> defaultSelection = new ArrayList<ListRuleArgument>();

        if (group != null) {
            TreeStore<ListRuleArgument> store = tree.getStore();

            if (parent != null) {
                store.add(parent, group);
            } else {
                store.add(group);
            }

            tree.setLeaf(group, false);
            store.update(group);

            if (group.getGroups() != null) {
                for (ListRuleArgumentGroup child : group.getGroups()) {
                    defaultSelection.addAll(addGroupToStore(group, child));
                }
            }

            if (group.getArguments() != null) {
                for (ListRuleArgument child : group.getArguments()) {
                    store.add(group, child);

                    if (child.isDefault()) {
                        defaultSelection.add(child);
                    }
                }
            }

            if (group.isDefault()) {
                defaultSelection.add(group);
            }
        }

        return defaultSelection;
    }
}
