package org.iplantc.core.uiapps.widgets.client.models.util;

import java.util.Collections;
import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

public class AppTemplateUtils {
    private static final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);

    private static final String SELECTION_ITEM_GROUP_ARGUMENTS = "arguments";
    private static final String SELECTION_ITEM_GROUP_GROUPS = "groups";
    private static final String SELECTION_ITEM_GROUP_SINGLE_SELECT = "isSingleSelect";
    private static final String SELECTION_ITEM_GROUP_CASCASE = "selectionCascade";

    public static AppTemplate copyAppTemplate(AppTemplate value) {
        AutoBean<AppTemplate> argAb = AutoBeanUtils.getAutoBean(value);
        Splittable splitCopy = AutoBeanCodex.encode(argAb);

        return AutoBeanCodex.decode(factory, AppTemplate.class, splitCopy.getPayload()).as();
    }

    public static Argument copyArgument(Argument value) {
        AutoBean<Argument> argAb = AutoBeanUtils.getAutoBean(value);
        Splittable splitCopy = AutoBeanCodex.encode(argAb);

        return AutoBeanCodex.decode(factory, Argument.class, splitCopy.getPayload()).as();
    }

    public static ArgumentGroup copyArgumentGroup(ArgumentGroup value) {
        AutoBean<ArgumentGroup> argGrpAb = AutoBeanUtils.getAutoBean(value);
        Splittable splitCopy = AutoBeanCodex.encode(argGrpAb);

        return AutoBeanCodex.decode(factory, ArgumentGroup.class, splitCopy).as();
    }

    public static Argument reserializeTreeSelectionArguments(Argument arg) {
        if (!arg.getType().equals(ArgumentType.TreeSelection)) {
            return arg;
        }
        // JDS Sanity check, there should only be one item in the list, and it should have the following
        // keys. Also, fail fast.
        Splittable firstItemCheck = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(arg.getSelectionItems().get(0)));
        if ((arg.getSelectionItems().size() == 1)
                && firstItemCheck.getPropertyKeys().contains(SELECTION_ITEM_GROUP_CASCASE)
                && firstItemCheck.getPropertyKeys().contains(SELECTION_ITEM_GROUP_SINGLE_SELECT)) {
            arg.setSelectionItems(recastSelectionItems(arg.getSelectionItems()));
        }
        return arg;
    }

    /**
     * Determines if the given items are equal by serializing them and comparing their
     * <code>Splittable</code> payloads.
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean areEqual(SelectionItem a, SelectionItem b) {
        Splittable aSplit = getSplittable(AutoBeanUtils.getAutoBean(a));
        Splittable bSplit = getSplittable(AutoBeanUtils.getAutoBean(b));
        return aSplit.getPayload().equals(bSplit.getPayload());
    }

    private static Splittable getSplittable(AutoBean<?> autoBean) {
        return AutoBeanCodex.encode(autoBean);
    }

    /**
     * Takes a list of {@link SelectionItem}s and re-serializes them to {@link SelectionItemGroup}s as
     * necessary.
     * This is to provide some level of type safety for the operations in Hierarchical List Fields and
     * Editors (e.g. for instanceof checks).
     * 
     * @param selectionItems the list of {@linkplain SelectionItem}s to be operated on.
     * @return
     */
    private static List<SelectionItem> recastSelectionItems(List<SelectionItem> selectionItems) {
        if ((selectionItems == null) || selectionItems.isEmpty()) {
            return Collections.<SelectionItem> emptyList();
        }

        List<SelectionItem> selectionItemsRet = Lists.newArrayList();
        for (SelectionItem si : selectionItems) {
            Splittable siSplit = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(si));
            // JDS Check keys.
            List<String> propertyKeys = siSplit.getPropertyKeys();
            if (propertyKeys.contains(SELECTION_ITEM_GROUP_ARGUMENTS)
                    || propertyKeys.contains(SELECTION_ITEM_GROUP_GROUPS)
                    || propertyKeys.contains(SELECTION_ITEM_GROUP_SINGLE_SELECT)
                    || propertyKeys.contains(SELECTION_ITEM_GROUP_CASCASE)) {
                // JDS Re-serialize the node as a SelectionItemGroup and add it to the return list.
                SelectionItemGroup sig = AutoBeanCodex.decode(factory, SelectionItemGroup.class, siSplit).as();
                selectionItemsRet.add(sig);
            } else {
                selectionItemsRet.add(si);
            }
        }

        // JDS Sanity check. Not sure what to do if this is actually met.
        if (selectionItemsRet.size() != selectionItems.size()) {
            GWT.log("HOUSTON!!! WE HAVE A PROBLEM!!!");
            return null;
        }
        return selectionItemsRet;

    }
}
