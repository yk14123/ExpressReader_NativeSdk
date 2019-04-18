package com.cnpeak.expressreader.analytics;

import com.cnpeak.expressreader.model.remote.IssueUnitClickEventFactory;
import com.cnpeak.expressreader.model.remote.MagazineBrandClickEventFactory;
import com.cnpeak.expressreader.model.remote.NewsClickEventFactory;

public class AnalyticsAgent implements IAnalyticsEvent {

    private AnalyticsAgent() {

    }

    public static AnalyticsAgent builder() {
        return Holder.sAnalyticsAgent;
    }


    private static class Holder {
        private static AnalyticsAgent sAnalyticsAgent = new AnalyticsAgent();
    }

    @Override
    public void onIssueUnitClickEvent(String MnId, String Latitude, String Longitude, String LCID) {
        IssueUnitClickEventFactory.sendEvent(MnId, Latitude, Longitude, LCID);
    }

    @Override
    public void onNewsClickEvent(String NID, String Latitude, String Longitude, String RealLocation, String LCID) {
        NewsClickEventFactory.sendEvent(NID, Latitude, Longitude, RealLocation, LCID);
    }

    @Override
    public void onMagazineBrandClickEvent(String Id, String brandCode, String userName) {
        MagazineBrandClickEventFactory.sendEvent(Id, brandCode, userName);

    }

}
