package com.andall.firehazard.states;

import com.badlogic.gdx.Gdx;

public class Functions {
    public static String getPlatform() {
        switch (Gdx.app.getType()) {
            case Android:
                return "Android " + Gdx.app.getVersion();
            case Desktop:
                return "Desktop";
            case WebGL:
                return "WebGL";
            case iOS:
                return "iOS";
            case HeadlessDesktop:
                return "Headless Desktop";
            case Applet:
                return "Applet";
            default:
                return null;
        }
    }
}
