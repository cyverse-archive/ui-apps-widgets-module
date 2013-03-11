package org.iplantc.core.uiapps.widgets.client.view.editors;

import java.util.Iterator;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentField;
import org.iplantc.core.uiapps.widgets.client.view.fields.ConverterFieldAdapter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.AppWizardFieldFactory;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToStringConverter;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ArgumentEditorAdapter extends Composite implements HasWidgets, CompositeEditor<Argument, Splittable, ArgumentField>, ValueAwareEditor<Argument> {

    interface ArgumentEditorAdapterUiBinder extends UiBinder<FieldLabel, ArgumentEditorAdapter> {}

    private static ArgumentEditorAdapterUiBinder BINDER = GWT.create(ArgumentEditorAdapterUiBinder.class);
    private CompositeEditor.EditorChain<Splittable, ArgumentField> chain;

    @Ignore
    @UiField
    FieldLabel propertyLabel;
    
    private ArgumentField subEditor = null;
    private Argument argument;

    public ArgumentEditorAdapter() {
        initWidget(BINDER.createAndBindUi(this));
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

        propertyLabel.addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // GWT.log("Click on " + propertyLabel.getText());

            }
        }, ClickEvent.getType());
    }
    

    @Override
    public void setValue(Argument value) {
        this.argument = value;
        subEditor = AppWizardFieldFactory.createPropertyField(value);
        SafeHtml fieldLabelText = AppWizardFieldFactory.createFieldLabelText(value);
        propertyLabel.setHTML(fieldLabelText);
        
        if (subEditor != null) {
            propertyLabel.setWidget(subEditor);
            Object o = subEditor.asWidget();
            if (o instanceof CheckBox) {
                propertyLabel.setHTML("");
                propertyLabel.setLabelSeparator("");
                ((CheckBox)o).setBoxLabel(fieldLabelText.asString());
           }
            // attach it to the chain. Attach the formvalue
            Splittable formValue = value.getValue();
            chain.attach(formValue, subEditor);

        }

        // Can explicitly add the detail panel to the editor chain here.
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {}


    @Override
    public void flush() {
        Splittable split = chain.getValue(subEditor);
        // For now, manually put the value into the argument
        if (split != null) {
            argument.setValue(split);
        }
    }


    @Override
    public void onPropertyChange(String... paths) {}


    @Override
    public ArgumentField createEditorForTraversal() {
        return new ConverterFieldAdapter<String, TextField>(new TextField(), new SplittableToStringConverter());
    }


    @Override
    public String getPathElement(ArgumentField subEditor) {
        return "";
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

}
