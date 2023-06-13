package com.drewapps.ai.AAChartCoreLib.AAChartCreator;

import  com.drewapps.ai.AAChartCoreLib.AATools.AAJSStringPurer;

public class AAChartEvents {
    public String load;
    public String redraw;
    public String render;
    public String selection;


    public AAChartEvents load(String prop) {
        load = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }

    public AAChartEvents redraw(String prop) {
        redraw = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }

    public AAChartEvents render(String prop) {
        render = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }

    public AAChartEvents selection(String prop) {
        selection = AAJSStringPurer.pureAnonymousJSFunctionString(prop);
        return this;
    }
}
