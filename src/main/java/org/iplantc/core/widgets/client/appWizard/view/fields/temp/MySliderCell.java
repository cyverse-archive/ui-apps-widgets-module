package org.iplantc.core.widgets.client.appWizard.view.fields.temp;

import com.sencha.gxt.cell.core.client.SliderCell;
import com.sencha.gxt.core.client.util.Format;

public class MySliderCell extends SliderCell {
    /*
     * (non-Javadoc)
     * 
     * @see com.sencha.gxt.cell.core.client.SliderCell#onFormatValue(int)
     * JDS This is where you will format the ToolTip text
     */
    @Override
    protected String onFormatValue(int value) {
        return Format.substitute(getMessage(), value);
    }
}