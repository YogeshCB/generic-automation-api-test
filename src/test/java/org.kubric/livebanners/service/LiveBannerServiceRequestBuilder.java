package org.kubric.livebanners.service;

import org.kubric.commonUtils.RequestBuilderLivebanners;

public class LiveBannerServiceRequestBuilder extends RequestBuilderLivebanners {
    public LiveBannerServiceRequestBuilder(String filePath){
        super(filePath);
        System.out.println("---->>>>>> File is: "+ filePath + " <<<<<<----");
    }
}