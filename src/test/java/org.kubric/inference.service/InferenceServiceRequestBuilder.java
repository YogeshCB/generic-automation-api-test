package org.kubric.inference.service;

import org.kubric.commonUtils.RequestBuilder;

public class InferenceServiceRequestBuilder extends RequestBuilder {
    public InferenceServiceRequestBuilder(String filePath){
        super(filePath);
        System.out.println("---->>>>>> File is: "+ filePath + " <<<<<<----");
    }
}
