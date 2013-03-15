package org.iplantc.core.uiapps.widgets.client.view.fields.temp;

import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentField;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class SliderAdapterthing extends ConverterEditorAdapter<Splittable, Integer, Slider> implements ArgumentField {

    private static MySliderCell sliderCell = new MySliderCell();
    private final Slider slider;

    public SliderAdapterthing(Converter<Splittable, Integer> converter) {
        super(new Slider(sliderCell), converter);

        this.slider = createEditorForTraversal();
    }

    @Override
    public Widget asWidget() {
        return slider;
    }

    @Override
    public void setToolTipConfig(ToolTipConfig toolTip) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void clearInvalid() {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean isValid(boolean preventMark) {
        return false;
    }

    @Override
    public boolean validate(boolean preventMark) {
        return false;
    }

    @Override
    public IsField<?> getField() {
        return slider;
    }

}
