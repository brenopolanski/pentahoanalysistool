package org.pentaho.pat.client.util.factory.charts.axis;

import java.util.Map;

import com.rednels.ofcgwt.client.model.axis.XAxis;
import com.rednels.ofcgwt.client.model.axis.YAxis;

public class ChartAxis {

    public YAxis createYAxis(Map<String, Object> chartOptions) {
        final YAxis ya = new YAxis();
        // ya.setSteps(16);

        if (chartOptions != null) {
            if (chartOptions.containsKey("yaxisColor")) //$NON-NLS-1$
                ya.setColour((String) chartOptions.get("yaxisColor")); //$NON-NLS-1$

            if (chartOptions.containsKey("yaxisGridColor")) //$NON-NLS-1$
                ya.setGridColour((String) chartOptions.get("yaxisGridColor")); //$NON-NLS-1$

            if (chartOptions.containsKey("yaxisMin")) //$NON-NLS-1$
                ya.setMin(Integer.parseInt((String) chartOptions.get("yaxisMin"))); //$NON-NLS-1$

            if (chartOptions.containsKey("yaxisMax")) //$NON-NLS-1$
                ya.setMax(Integer.parseInt((String) chartOptions.get("yaxisMax"))); //$NON-NLS-1$
        }
        return ya;

    }

    public XAxis createXAxis(Map<String, Object> chartOptions) {
        final XAxis xa = new XAxis();
        // xa.setSteps(16);
        if (chartOptions != null) {
            if (chartOptions.containsKey("xaxisColor")) //$NON-NLS-1$
                xa.setColour((String) chartOptions.get("xaxisColor")); //$NON-NLS-1$

            if (chartOptions.containsKey("xaxisGridColor")) //$NON-NLS-1$
                xa.setGridColour((String) chartOptions.get("xaxisGridColor")); //$NON-NLS-1$

            if (chartOptions.containsKey("xaxisMin")) //$NON-NLS-1$
                xa.setMin(Integer.parseInt((String) chartOptions.get("xaxisMin"))); //$NON-NLS-1$

            if (chartOptions.containsKey("xaxisMax")) //$NON-NLS-1$
                xa.setMin(Integer.parseInt((String) chartOptions.get("xaxisMax"))); //$NON-NLS-1$
        }
        return xa;

    }
}
