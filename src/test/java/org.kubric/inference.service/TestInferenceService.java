package org.kubric.inference.service;

import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.kubric.commonUtils.CSVParametersProvider;
import org.kubric.commonUtils.DataFileParameters;
import org.kubric.commonUtils.JSONFileReader;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestInferenceService {

    private static ValidatableResponse response;
    JsonPath responseJson;
    InferenceServiceRequestBuilder Irb;
    JSONFileReader jReader;


    @BeforeClass
    public void init() throws IOException {
        String env = System.getProperty("Environment");
       System.out.println("--------- Env name: " + env + " ---------");

        if (env.equalsIgnoreCase("Staging")) {
            Irb = new InferenceServiceRequestBuilder("/resources/config-files/staging/InferenceService.properties");
        } else if (env.equalsIgnoreCase("Production")) {
            Irb = new InferenceServiceRequestBuilder("/resources/config-files/prod/InferenceService.properties");
        }
        jReader = new JSONFileReader();


    }

    @Test(enabled = true, priority = 0, description = "Check to see if API is infering value and giving a task id   ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "inferenceservice.csv", path = "/resources/input-data/Inference-Service")
    public void testInferenceCampaign (String uri) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Inference_Service/InferenceReference.json");
            JsonObject jsonReqBody = Irb.createRequestBody(jsonFileObject);
            response = Irb.sendRequest(jsonReqBody, uri);
            responseJson = response.extract().jsonPath();
        } catch (Exception e) {

            e.printStackTrace();
        }

        System.out.println();
        {
           Reporter.log("Uri=" + uri );
        }

    }
    @Test(enabled = true, priority = 0, description = "Check to see if you are able to get task inference result  ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "inferenceTaskResult.csv", path = "/resources/input-data/Inference-Service")
   public void testInferenceTaskResult (String uri) {

        try {
            response = Irb.getiInfernceRequest(uri);
            responseJson = response.extract().jsonPath();
        } catch (Exception e) {

            e.printStackTrace();
        }
        Reporter.log("Uri=" + uri );

    }
}
