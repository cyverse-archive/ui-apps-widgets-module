package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uidiskresource.client.views.dialogs.FileSelectDialog;
import org.iplantc.core.uidiskresource.client.views.dialogs.FolderSelectDialog;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasText;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * XXX JDS May want to turn this into a selector of either files or folders
 * TODO JDS All Diskresource selectors (incl multi) need to have a "file_info_type". This will be passed
 * to the DiskResource presenter, which will filter outputs.
 * 
 * @author jstroot
 * 
 */
public class AppWizardDiskResourceSelector extends Component implements TemplatePropertyEditorBase {

    interface FileFolderSelectorStyle extends CssResource {
        String buttonWrap();

        String wrap();
    }

    interface Resources extends ClientBundle {
        @Source("FileFolderSelector.css")
        FileFolderSelectorStyle style();
    }

    public interface FileUploadTemplate extends XTemplates {
        @XTemplate("<div class='{style.wrap}'></div>")
        SafeHtml render(FileFolderSelectorStyle style);
    }

    private final TextButton button;
    private final TextField input;

    private final Resources res = GWT.create(Resources.class);
    private final FileUploadTemplate template = GWT.create(FileUploadTemplate.class);
    private final boolean fileSelector;

    public static AppWizardDiskResourceSelector asFileSelector() {
        return new AppWizardDiskResourceSelector(true);
    }

    public static AppWizardDiskResourceSelector asFolderSelector() {
        return new AppWizardDiskResourceSelector(false);
    }

    private AppWizardDiskResourceSelector(boolean fileSelector) {
        this.fileSelector = fileSelector;
        res.style().ensureInjected();
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.append(template.render(res.style()));
        setElement(XDOM.create(builder.toSafeHtml()));

        input = new TextField();
        input.setReadOnly(true);
        getElement().appendChild(input.getElement());

        sinkEvents(Event.ONCHANGE | Event.ONCLICK | Event.MOUSEEVENTS);

        button = new TextButton(I18N.DISPLAY.browse());
        button.getElement().addClassName(res.style().buttonWrap());
        getElement().appendChild(button.getElement());
        button.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                onBrowseSelected();
            }
        });

    }

    private void onBrowseSelected() {
        if (fileSelector) {
            FileSelectDialog fileSD = FileSelectDialog.singleSelect();
            fileSD.addHideHandler(new DialogHideHandler(fileSD, input));
            fileSD.show();
        } else {
            FolderSelectDialog folderSD = new FolderSelectDialog();
            folderSD.addHideHandler(new DialogHideHandler(folderSD, input));
            folderSD.show();
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        // Forward the event
        button.onBrowserEvent(event);
    }

    // @Override
    // public void addValidator(Validator<String> validator) {
    // input.addValidator(validator);
    // }
    //
    // @Override
    // public void removeValidator(Validator<String> validator) {
    // input.removeValidator(validator);
    // }
    //
    // @Override
    // public List<Validator<String>> getValidators() {
    // return input.getValidators();
    // }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return input.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return input.addValidHandler(handler);
    }

    @Override
    public void setValue(Splittable value) {
        String payload = value.getPayload();
        JSONArray jsonArray = JsonUtil.getObject(payload).isArray();
        List<String> list = JsonUtil.buildStringList(jsonArray);
        input.setValue(Joiner.on(",").join(list));
    }

    @Override
    public Splittable getValue() {
        // Parse text field into a list
        // Convert the list to a JSONArray
        // Create Splittable from JSONArray payload.
        List<String> split = Lists.newArrayList(Splitter.on(",").split(input.getValue()));
        return StringQuoter.create(JsonUtil.buildArrayFromStrings(split).toString());
    }

    private final class DialogHideHandler implements HideHandler {
        private final TakesValue<List<String>> takesValue;
        private final HasText tf;
    
        private DialogHideHandler(TakesValue<List<String>> dlg, HasText tf) {
            this.takesValue = dlg;
            this.tf = tf;
        }
    
        @Override
        public void onHide(HideEvent event) {
            tf.setText(Joiner.on(",").join(takesValue.getValue()));
        }
    }

}
