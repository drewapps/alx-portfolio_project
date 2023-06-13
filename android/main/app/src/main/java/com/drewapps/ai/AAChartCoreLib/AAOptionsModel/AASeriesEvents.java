package com.drewapps.ai.AAChartCoreLib.AAOptionsModel;

import com.drewapps.ai.AAChartCoreLib.AATools.AAJSStringPurer;

public class AASeriesEvents {
    public String legendItemClick;

    public AASeriesEvents legendItemClick(String prop) {
        legendItemClick = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }

}
