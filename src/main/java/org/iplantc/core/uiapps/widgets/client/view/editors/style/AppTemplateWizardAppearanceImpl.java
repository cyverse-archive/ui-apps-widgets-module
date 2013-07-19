package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;

public class AppTemplateWizardAppearanceImpl implements AppTemplateWizardAppearance {

    private final Resources res;
    private final AppTemplateWizardTemplates templates;
    private final AppTemplateWizardDisplayStrings displayStrings;

    public AppTemplateWizardAppearanceImpl() {
        res = GWT.create(Resources.class);
        templates = GWT.create(AppTemplateWizardTemplates.class);
        displayStrings = GWT.create(AppTemplateWizardDisplayStrings.class);
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
    public AppTemplateWizardDisplayStrings getMessages() {
        return displayStrings;
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

}
