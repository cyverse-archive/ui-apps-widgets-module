package org.iplantc.core.uiapps.widgets.client.view.deployedComponents.cells;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOUT;
import static com.google.gwt.dom.client.BrowserEvents.MOUSEOVER;

import org.iplantc.core.uiapps.widgets.client.view.deployedComponents.DeployedComponentsListingView;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;

/**
 * This is a custom cell which is clickable hyper-link of an DC name.
 * 
 * @author sriram
 * 
 */
public class DCNameHyperlinkCell extends AbstractCell<DeployedComponent> {


    interface MyCss extends CssResource {
        String DCName();
    }

    interface Resources extends ClientBundle {
        @Source("DCNameHyperlinkCell.css")
        MyCss css();
    }

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template("<span name=\"{3}\" class=\"{0}\" qtip=\"{2}\">{1}</span>")
        SafeHtml cell(String textClassName, SafeHtml name, String textToolTip, String elementName);
    }

    private final Resources resources = GWT.create(Resources.class);
    private final Templates templates = GWT.create(Templates.class);
    private DeployedComponentsListingView view;
    private static final String ELEMENT_NAME = "DCName";

    public DCNameHyperlinkCell(DeployedComponentsListingView view) {
        super(CLICK, MOUSEOVER, MOUSEOUT);
        this.view = view;
        resources.css().ensureInjected();
    }

    @Override
    public void render(Cell.Context context, DeployedComponent value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        sb.appendHtmlConstant("&nbsp;");
        SafeHtml safeHtmlName = SafeHtmlUtils.fromString(value.getName());
        sb.append(templates.cell(resources.css().DCName(), safeHtmlName, "Click to view info",
                ELEMENT_NAME));

    }

    @Override
    public void onBrowserEvent(Cell.Context context, Element parent, DeployedComponent value,
            NativeEvent event, ValueUpdater<DeployedComponent> valueUpdater) {
        Element eventTarget = Element.as(event.getEventTarget());
        if ((value == null) && !parent.isOrHasChild(eventTarget)) {
            return;
        }

        if (eventTarget.getAttribute("name").equalsIgnoreCase(ELEMENT_NAME)) {

            switch (Event.as(event).getTypeInt()) {
                case Event.ONCLICK:
                    doOnClick(eventTarget, value);
                    break;
                case Event.ONMOUSEOVER:
                    doOnMouseOver(eventTarget, value);
                    break;
                case Event.ONMOUSEOUT:
                    doOnMouseOut(eventTarget, value);
                    break;
                default:
                    break;
            }
        }
    }

    private void doOnMouseOut(Element eventTarget, DeployedComponent value) {
        eventTarget.getStyle().setTextDecoration(TextDecoration.NONE);
    }

    private void doOnMouseOver(Element eventTarget, DeployedComponent value) {
        eventTarget.getStyle().setTextDecoration(TextDecoration.UNDERLINE);
    }

    private void doOnClick(final Element eventTarget, final DeployedComponent value) {
        view.showInfo(value);
    }
}
