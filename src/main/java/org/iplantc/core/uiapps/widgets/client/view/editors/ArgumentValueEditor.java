package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * This <code>Editor</code> is responsible for dynamically binding <code>Splittable</code> sub-editors
 * based on the {@link Argument#getType()}.
 * 
 * It is necessary to do this within the scope of a <code>ValueAwareEditor</code> since GWT needs class
 * literal definitions at compile time.
 * 
 * @author jstroot
 * 
 */
class ArgumentValueEditor extends Composite implements HasWidgets, CompositeEditor<Argument, Splittable, ArgumentField>, ValueAwareEditor<Argument> {

    private CompositeEditor.EditorChain<Splittable, ArgumentField> chain;

    private final FieldLabel propertyLabel;
    
    private ArgumentField subEditor = null;
    private Argument argument;

    private ClickHandler clickHandler;

    public ArgumentValueEditor(final EventBus eventBus) {
        propertyLabel = new FieldLabel();
        initWidget(propertyLabel);
        propertyLabel.setLabelAlign(LabelAlign.TOP);
        
        // Temporary handlers to visually show mouseovers
        propertyLabel.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                 propertyLabel.setBorders(true);

            }
        }, MouseOverEvent.getType());

        propertyLabel.addDomHandler(new MouseOutHandler() {

            @Override
            public void onMouseOut(MouseOutEvent event) {
                 propertyLabel.setBorders(false);
            }
        }, MouseOutEvent.getType());
    }

    /**
     * Adds a click handler to this class' field label.
     * @param clickHandler
     */
    public void addArgumentClickHandler(ClickHandler clickHandler) {
        this.clickHandler = clickHandler;
        propertyLabel.addDomHandler(clickHandler, ClickEvent.getType());
        // If the subEditor's field is a CheckBox, add the click handler to it, since it won't have a
        // field label.
        if ((subEditor != null) && (subEditor.getField() instanceof CheckBox)) {
            subEditor.getField().asWidget().addDomHandler(clickHandler, ClickEvent.getType());
        }
    }

    @Ignore
    public ArgumentField getArgumentField() {
        return subEditor;
    }

    @Override
    public void setValue(Argument value) {
        this.argument = value;
        subEditor = AppWizardFieldFactory.createArgumentField(value);
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);
        
        if (subEditor != null) {
            propertyLabel.setWidget(subEditor);
            Widget o = subEditor.asWidget();
            if (o instanceof CheckBox) {
                if(clickHandler != null){
                    o.addDomHandler(clickHandler, ClickEvent.getType());
                }
                propertyLabel.setHTML("");
                propertyLabel.setLabelSeparator("");
                ((CheckBox)o).setBoxLabel(fieldLabelText.asString());
           }
            // attach it to the chain. Attach the formvalue
            Splittable formValue = value.getValue();
            chain.attach(formValue, subEditor);
    
        }
    }

    @Override
    public void flush() {
        Splittable split = chain.getValue(subEditor);
        // Manually put the value into the argument
        if (split != null) {
            argument.setValue(split);
        }
    }

    @Override
    public ArgumentField createEditorForTraversal() {
        return new ConverterFieldAdapter<String, TextField>(new TextField(), new SplittableToStringConverter());
    }

    @Override
    public void setEditorChain(CompositeEditor.EditorChain<Splittable, ArgumentField> chain) {
        this.chain = chain;
    }

    @Override
    public void add(Widget w) {
        propertyLabel.add(w);
    }

    @Override
    public void clear() {
        propertyLabel.clear();
    }

    @Override
    public Iterator<Widget> iterator() {
        return propertyLabel.iterator();
    }

    @Override
    public boolean remove(Widget w) {
        return propertyLabel.remove(w);
    }

    @Override
    public String getPathElement(ArgumentField subEditor) {
        return "";
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
