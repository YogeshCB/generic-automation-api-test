package org.kubric.Teams.service;

import org.kubric.commonUtils.RequestBuilder;

public class TeamServiceRequestBuilder extends RequestBuilder{

    public TeamServiceRequestBuilder(String filePath){

        super(filePath);
        System.out.println("---->>>>>> File is: " + filePath + " <<<<<<----");
    }}
