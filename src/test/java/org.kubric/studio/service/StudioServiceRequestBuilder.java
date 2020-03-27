package org.kubric.studio.service;




import org.kubric.commonUtils.RequestBuilder;

class StudioServiceRequestBuilder extends RequestBuilder {
    public StudioServiceRequestBuilder(String filePath) {
        super(filePath);
        System.out.println("---->>>>>> File is: " + filePath + " <<<<<<----");
    }
}
