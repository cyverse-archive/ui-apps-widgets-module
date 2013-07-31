package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

public class AppTemplateWizardAppearanceImpl implements AppTemplateWizardAppearance {

    private final Resources res;
    private final AppTemplateWizardTemplates templates;
    private final AppsWidgetsPropertyPanelLabels labels;
    private final AppsWidgetsContextualHelpMessages help;

    public AppTemplateWizardAppearanceImpl() {
        res = GWT.create(Resources.class);
        templates = GWT.create(AppTemplateWizardTemplates.class);
        labels = GWT.create(AppsWidgetsPropertyPanelLabels.class);
        help = GWT.create(AppsWidgetsContextualHelpMessages.class);
    }

    @Override
    public int getAutoExpandOnHoverDelay() {
        return 500;
    }

    @Override
    public int getAutoScrollDelay() {
        return 200;
    }

    @Override
    public int getAutoScrollRegionHeight() {
        return 5;
    }

    @Override
    public int getAutoScrollRepeatDelay() {
        return 50;
    }

    @Override
    public int getDefaultArgListHeight() {
        return 200;
    }

    @Override
    public IconButton getArgListDeleteButton() {
        IconButton argDeleteBtn = new IconButton(new IconConfig(res.css().delete(), res.css().deleteHover()));
        argDeleteBtn.addStyleName(res.css().deleteBtn());
        return argDeleteBtn;
    }

    @Override
    public ImageElement getErrorIconImg() {
        ImageElement errIconImg = Document.get().createImageElement();
        errIconImg.setSrc(res.exclamation().getSafeUri().asString());
        return errIconImg;
    }

    @Override
    public AppTemplateWizardTemplates getTemplates() {
        return templates;
    }

    @Override
    public Style getStyle() {
        res.css().ensureInjected();
        return res.css();
    }

    @Override
    public int getDefaultTreeSelectionHeight() {
        return 200;
    }

    @Override
    public SafeHtml createArgumentLabel(Argument model) {
        SafeHtmlBuilder labelText = new SafeHtmlBuilder();
        if (model.getRequired()) {
            // If the field is required, it needs to be marked as such.
            labelText.append(templates.fieldLabelRequired());
        }
        // JDS Remove the trailing colon. The FieldLabels will apply it automatically.
        SafeHtml label = SafeHtmlUtils.fromString(model.getLabel().replaceFirst(":$", ""));
        if (Strings.isNullOrEmpty(model.getDescription()) || ((model.getId() != null) && model.getId().equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID))) {
            labelText.append(label);
        } else {
            labelText.append(templates.fieldLabelImgFloatRight(label, res.info().getSafeUri(), model.getDescription()));
        }
        return labelText.toSafeHtml();
    }

    @Override
    public SafeHtml createContextualHelpLabel(String labelToolTipText, String propertyToolTip) {
        return templates.fieldLabelImgFloatRight(SafeHtmlUtils.fromString(labelToolTipText), res.help().getSafeUri(), propertyToolTip);
    }

    @Override
    public SafeHtml createContextualHelpLabelNoFloat(String label, String toolTip) {
        return templates.fieldLabelImg(SafeHtmlUtils.fromString(label), res.help().getSafeUri(), toolTip);
    }

    @Override
    public AppsWidgetsPropertyPanelLabels getPropertyPanelLabels() {
        return labels;
    }

    @Override
    public AppsWidgetsContextualHelpMessages getContextHelpMessages() {
        return help;
    }

}
