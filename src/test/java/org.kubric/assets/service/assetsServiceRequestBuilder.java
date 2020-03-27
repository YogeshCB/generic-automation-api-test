package org.kubric.assets.service;

import org.kubric.commonUtils.RequestBuilder;

public class AssetsServiceRequestBuilder extends RequestBuilder {
        public AssetsServiceRequestBuilder(String filePath){
        super(filePath);
        System.out.println("---->>>>>> File is: "+ filePath + " <<<<<<----");
    }
}