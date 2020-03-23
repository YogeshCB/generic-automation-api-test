package org.kubric.studio.service;

import org.kubric.commonUtils.RequestBuilder;

public class studioServiceRequestBuilder extends RequestBuilder {
    public studioServiceRequestBuilder(String filePath) {
        super(filePath);
        System.out.println("---->>>>>> File is: " + filePath + " <<<<<<----");
    }
}