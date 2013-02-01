package org.iplantc.core.widgets.client.appWizard.view.fields.temp;

import org.iplantc.core.widgets.client.appWizard.models.Argument;
import org.iplantc.core.widgets.client.appWizard.view.fields.ArgumentField;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.ConverterEditorAdapter;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class SliderAdapterthing extends ConverterEditorAdapter<Splittable, Integer, Slider> implements ArgumentField {

    private static MySliderCell sliderCell = new MySliderCell();
    private final Slider slider;

    public SliderAdapterthing(Converter<Splittable, Integer> converter) {
        super(new Slider(sliderCell), converter);

        this.slider = createEditorForTraversal();
    }

    @Override
    public void initialize(Argument property) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {return null;}

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {return null;}

    @Override
    public Widget asWidget() {
        return slider;
    }

    @Override
    public void setToolTip(ToolTipConfig toolTip) {

    }

}
