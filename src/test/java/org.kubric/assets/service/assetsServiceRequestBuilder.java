package org.kubric.assets.service;

import org.kubric.commonUtils.RequestBuilder;

public class assetsServiceRequestBuilder extends RequestBuilder {
        public assetsServiceRequestBuilder(String filePath){
        super(filePath);
        System.out.println("---->>>>>> File is: "+ filePath + " <<<<<<----");
    }
}