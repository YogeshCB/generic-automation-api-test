package org.kubric.livebanners.service;

import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.kubric.commonUtils.CSVParametersProvider;
import org.kubric.commonUtils.DataFileParameters;
import org.kubric.commonUtils.JSONFileReader;
import org.kubric.inference.service.InferenceServiceRequestBuilder;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestLiveBannerService {


    private static ValidatableResponse response;
    JsonPath responseJson;
    LiveBannerServiceRequestBuilder Lrb;
    JSONFileReader jReader;


    @BeforeClass
    public void init() throws IOException {
        String env = System.getProperty("Environment");
        System.out.println("--------- Env name: " + env + " ---------");

        if (env.equalsIgnoreCase("Staging")) {
            Lrb = new LiveBannerServiceRequestBuilder("/resources/config-files/staging/LiveBanner.properties");
        } else if (env.equalsIgnoreCase("Production")) {
            Lrb = new LiveBannerServiceRequestBuilder("/resources/config-files/prod/LiveBanner.properties");
        }
        jReader = new JSONFileReader();


    }


    @Test(enabled = true, priority = 0, description = " Check to if the api is able to give response for all config ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "GetUserConfig.csv", path = "/resources/input-data/Live-Banner")
    public void testInferenceCampaign (String uri) {

        try {
            response = Lrb.getLiveRequest(uri);
            System.out.println(uri);
            responseJson = response.extract().jsonPath();
        } catch (Exception e) {

            e.printStackTrace();
        }
        Reporter.log("Uri=" + uri );


    }

    @Test(enabled = true, priority = 0, description = " Check to see if the count of enable banners ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "Count-Enable_banners.csv", path = "/resources/input-data/Live-Banner")
        public void testEnableBanners(String uri) {

        try {
            response = Lrb.getLiveRequest(uri);
            System.out.println(uri);
            responseJson = response.extract().jsonPath();
        } catch (Exception e) {

            e.printStackTrace();
        }
        Reporter.log("Uri=" + uri );


    }

    @Test(enabled = true, priority = 0, description = " Check to see if the count of enable banners ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "Count-Disable-banner.csv", path = "/resources/input-data/Live-Banner")
    public void testDisableBanners(String uri) {

        try {
            response = Lrb.getLiveRequest(uri);
            System.out.println(uri);
            responseJson = response.extract().jsonPath();
        } catch (Exception e) {

            e.printStackTrace();
        }
        Reporter.log("Uri=" + uri );


    }
}
