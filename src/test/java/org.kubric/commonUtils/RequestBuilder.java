package org.kubric.commonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.testng.Reporter;

import java.io.File;
import java.util.Map;
import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.MultiPartConfig.multiPartConfig;

/**
 * // TODO Comment
 */
public class RequestBuilder {


    public RequestSpecification request;
    public PropertyReader pReader;
    String baseUrl;

    public RequestBuilder(String filePath)
    {
            pReader = new PropertyReader(filePath);
            String server = (String) pReader.get("url");
            baseUrl = "http://" + server;
            String origin = (String) pReader.get("origin");
            String baseUrl= "http://"+server;
//                    if (baseUrl == "https://studio.kubric.io/api")
//                        {
                             request = new RequestSpecBuilder()
                            .addHeader("Authorization", "q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                            .setBaseUri(baseUrl)
                            .build();
                        }
//                    else
//                        {
//                            request = new RequestSpecBuilder().setContentType(ContentType.URLENC.withCharset("UTF-8"))
//                            .addHeader("Authorization","oMc6Zb6WyRBA9INjTvarmAibosLW9d4Mx76ZIkBICqDipzFx5Vo4/bZhVLzHffqiX85A1xbecbQdHkbSVwHB5Sy4hItBE19uWN6QACg1oo/ZJ4E5XLH8xk9AlDW5BYfB")
//                            .addHeader("X-Kubric-Workspace-ID", "d38ae70c-a1c5-486f-b076-9da83c8b6fb6")
//                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                            .setBaseUri(baseUrl)
//                            .build();
//                       }
  //  }

    public JsonObject createRequestBody(JsonObject jsonFileObject)
    {

        String env = pReader.prop.getProperty("env");
        return jsonFileObject;
    }

    public JsonObject addPropertyToRequest(JsonObject jsonReqBody, String propertyName, String propertyValue)
    {
        JsonObject JsonObject = jsonReqBody.getAsJsonObject();
        JsonObject.addProperty(propertyName, propertyValue);
        return jsonReqBody;
    }

    public JsonObject addJsonObjectToRequest(JsonObject jsonReqBody, String propertyName, JsonObject propertyValue)
    {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.add(propertyName, propertyValue);
        return jsonReqBody;
    }


    public JsonObject removePropertyFromRequest(JsonObject jsonReqBody, String propertyName)
    {
        JsonObject data = jsonReqBody.get("data").getAsJsonObject();
        data.remove(propertyName);

        return jsonReqBody;
    }



    public ValidatableResponse sendRequest(JsonObject jsonReqBody, String pathUri)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        Reporter.log("\n" + jsonReqBody, 1, true);

        ValidatableResponse response =
                         given()
                        .spec(request)
                        .body(jsonReqBody.toString())
                        .when()
                        .post(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;
    }



    public ValidatableResponse sendRequestAssets(JsonObject jsonReqBody, String pathUri)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        Reporter.log("\n" + jsonReqBody, 1, true);

        ValidatableResponse response =
                given()

                        .spec(request)
                        .body(jsonReqBody.toString())
                        .header("Content-Type" , "application/json")
                        .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .header("X-Kubric-Workspace-ID","b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .when()
                        .post(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;
    }


    public ValidatableResponse sendRequestUploadConfirm(JsonObject jsonReqBody1, String pathUri)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        Reporter.log("\n" + jsonReqBody1, 1, true);

        ValidatableResponse response =
                given()

                        .spec(request)
                        .body(jsonReqBody1.toString())
                        .header("Content-Type" , "application/json")
                        .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .header("X-Kubric-Workspace-ID","b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .when()
                        .post(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;
    }
    public ValidatableResponse sendRequestUploadUrl( String urlToUpload, String folderpath)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);


        ValidatableResponse response =
                given()
                        .contentType("multipart/binary")
                        .spec(request)
                        .body(new File(folderpath))
                        .post(urlToUpload)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;
    }



    public ValidatableResponse sendGenericPostRequestUploadUrl(String uri, JsonObject jsonReqBody)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + uri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        Reporter.log("\n" + jsonReqBody, 1, true);

        ValidatableResponse response =
                given()
                        .contentType("")
                        .spec(request)
                        .body(jsonReqBody.toString())
                        .when()
                        .header("Content-Type" , "application/json")
                        .header("X-Kubric-Workspace-ID","b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")


                        .post(uri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }


    public ValidatableResponse sendformRequestCreateAStoryBoard(String pathUri)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                         given()
                        .spec(request)
                        .formParam("shots", "276956fe-ec0f-403c-820b-a1383c57f034")
                        .formParam("content_type", "marketing")
                        .formParam("{  \"name\": \"Reimagining Kashmir\",  \"template\": {    \"objects\": [{      \"duration\": \"200\",      \"url\": {        \"id\": \"8cafdca6-dd0c-4fd6-b30a-8cd5b17f5fbf\"      },      \"type\": \"background\"    }, {      \"position\": {        \"y\": \"420\",        \"x\": \"680\"      },      \"size\": {        \"w\": \"102\",        \"h\": \"55\"      },      \"url\": {        \"id\": \"60a1b8e6-5817-4ea6-a3f1-9014e54ada95\"      },      \"type\": \"image\"    }, {      \"effects\": [{        \"type\": \"text.appear.4\"      }],      \"text\": {        \"id\": \"9272d47a-d09f-4806-9aba-d926c2ca0ab7\"      },      \"type\": \"text\"    }],    \"size\": {      \"w\": \"800\",      \"h\": \"500\"    },    \"duration\": {      \"id\": \"ca6d5cd7-8ec8-4a14-82ab-3ec3eaa171c5\"    }  },  \"parameters\": {    \"9272d47a-d09f-4806-9aba-d926c2ca0ab7\": {      \"default\": \"REIMAGINING KASHMIR\",      \"title\": \"Title caption of shot\",      \"type\": \"text\"    },    \"60a1b8e6-5817-4ea6-a3f1-9014e54ada95\": {      \"default\": \"https://storeo.io/assets/images/logo_color.png\",      \"title\": \"Logo of your company\",      \"type\": \"image\"    },    \"8cafdca6-dd0c-4fd6-b30a-8cd5b17f5fbf\": {      \"default\": \"https://storage.googleapis.com/storeo-dev/assets/orf_1.png\",      \"title\": \"Background of your shot\",      \"type\": \"image\"    },    \"ca6d5cd7-8ec8-4a14-82ab-3ec3eaa171c5\": {      \"default\": \"2000\",      \"title\": \"Duration of clip\",      \"type\": \"number\"    }  },  \"isDisabled\": false}")
                        .formParam("parameters", "[{    \"9272d47a-d09f-4806-9aba-d926c2ca0ab7\": {      \"default\": \"REIMAGINING KASHMIR\",      \"title\": \"Title caption of shot\",      \"type\": \"text\"    },    \"60a1b8e6-5817-4ea6-a3f1-9014e54ada95\": {      \"default\": \"https://storeo.io/assets/images/logo_color.png\",      \"title\": \"Logo of your company\",      \"type\": \"image\"    },    \"8cafdca6-dd0c-4fd6-b30a-8cd5b17f5fbf\": {      \"default\": \"https://storage.googleapis.com/storeo-dev/assets/orf_1.png\",      \"title\": \"Background of your shot\",      \"type\": \"image\"    },    \"ca6d5cd7-8ec8-4a14-82ab-3ec3eaa171c5\": {      \"default\": \"2000\",      \"title\": \"Duration of clip\",      \"type\": \"number\"    }  }]")
                        .formParam("name", "test")
                        .formParam("shots", "276956fe-ec0f-403c-820b-a1383c57f034")
                        .formParam("preview", "2")
                        .formParam("workspace_id", "8f0f814f-c698-438c-a632-d575ffee85d3")
                        .formParam("status", "0")
                        .formParam("public", "0")
                        .when()
                        .post(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);
        return response;

    }


    public ValidatableResponse sendformRequestCreateShot(String pathUri)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                given()
                        .spec(request)
                        .formParam("name", "AutomationTestShot")
                        .formParam("template", "{ \"duration\": 1000, \"objects\": [ { \"color\": \"{{bg_color}}\", \"position\": { \"y\": 0, \"x\": 0 }, \"kind\": \"rectangle\", \"type\": \"shape\", \"size\": { \"h\": \"{{global_height}}\", \"w\": \"[[{{global_width}}/2]]\" } }, { \"url\": \"http://lh3.googleusercontent.com/PExCKPRh0mTv51xfQuZF0-xZ9KxEeVvjJ5MFqUDJZp-d3hxLuR27tl-hqPDaRwX0kgG8YkTjaHZoAe2osjAFxsmv=s0\", \"position\": { \"y\": 0, \"x\": \"[[{{global_width}}/2]]\" }, \"type\": \"image\", \"size\": { \"h\": \"{{global_height}}\", \"w\": \"[[{{global_width}}/2]]\" } }, { \"color\": \"white\", \"text\": \"{{text1}}\", \"fontSize\": 50, \"type\": \"text\", \"position\": { \"y\": \"[[({{global_height}}/2)  - 100]]\", \"x\": 100 } }, { \"color\": \"white\", \"text\": \"{{text2}}\", \"fontSize\": 70, \"type\": \"text\", \"position\": { \"y\": \"[[({{global_height}}/2) ]]\", \"x\": 100 } }, { \"color\": \"white\", \"position\": { \"y\": \"[[({{global_height}}/2)-50]]\", \"x\": 100 }, \"kind\": \"rectangle\", \"type\": \"shape\", \"size\": { \"h\": 4, \"w\": 400 } } ], \"size\": { \"h\": \"{{global_height}}\", \"w\": \"{{global_width}}\" } }")
                        .formParam("parameters", "{ \"global_width\": { \"default\": 1200, \"type\": \"number\", \"title\": \"Overall Width\" }, \"bg_color\": { \"default\": \"#232323\", \"type\": \"color\", \"title\": \"BG Color\" }, \"text1\": { \"default\": \"This Summer\", \"type\": \"text\", \"title\": \"Heading\" }, \"global_height\": { \"default\": 700, \"type\": \"number\", \"title\": \"Overall Height\" }, \"text2\": { \"default\": \"Lets Roll\", \"type\": \"text\", \"title\": \"Sub Heading\" } }")
                        .formParam("status", "1")
                        .header("Authorization", "Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .header("X-Kubric-Workspace-ID", "b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .when()
                        .post(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);
        return response;

    }
    public ValidatableResponse getRequest(String pathUri)
    {

        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                         given()
                        .spec(request)
                        .queryParam("content_type","feed,marketing")
                                 .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                                 .header("X-Kubric-Workspace-ID","b98b4bf4-989c-49cd-9597-b287cb8436df")

                        .when()
                        .get(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }

    public ValidatableResponse getiInfernceRequest(String pathUri)
    {

        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                given()
                        .spec(request)
                        .when()
                        .get(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }
    public ValidatableResponse patchRequest(String pathUri)
    {

        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                given()
                        .spec(request)
                        .when()
                        .patch(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }

    public ValidatableResponse delteRequest(String pathUri)
    {

        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + pathUri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                given()
                        .spec(request)
                        .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .header("X-Kubric-Workspace-ID","b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .when()
                        .delete(pathUri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }

    public ValidatableResponse getTaskID(String task_url)
    {

        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + task_url, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                given()
                        .spec(request)
                        .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .header("X-Kubric-Workspace-ID","b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .when()
                        .get(task_url)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }



    public ValidatableResponse getUnzipTaskID(String task_url2) {

        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + task_url2, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        ValidatableResponse response =
                given()
                        .spec(request)
                        .header("Authorization", "Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .header("X-Kubric-Workspace-ID", "b98b4bf4-989c-49cd-9597-b287cb8436df")
                        .when()
                        .get(task_url2)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;


    }
        public ValidatableResponse getassetFilter(String uri) {

            Reporter.log("Base URL: " + baseUrl, 1, true);
            Reporter.log("Endpoint: " + uri, 1, true);
            Reporter.log("\nFINAL REQUEST: ", 1, true);
            Reporter.log("--------------", 1, true);
            ValidatableResponse response =
                    given()
                            .spec(request)
                            .header("X-Kubric-Workspace-ID", "b98b4bf4-989c-49cd-9597-b287cb8436df")
                            .header("Authorization", "Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                            .when()
                            .get(uri)
                            .then().log().ifError();
            Reporter.log("RESPONSE : ", 1, true);
            Reporter.log(response.extract().body().asString(), 1, true);

            return response;
        }

            public ValidatableResponse patchformRequestCreateShot(String pathUri) {
                Reporter.log("Base URL: " + baseUrl, 1, true);
                Reporter.log("Endpoint: " + pathUri, 1, true);
                Reporter.log("\nFINAL REQUEST: ", 1, true);
                Reporter.log("--------------", 1, true);
                ValidatableResponse response =
                        given()
                                .spec(request)
                                .formParam("template", "{ \"duration\": 1000, \"objects\": [ { \"color\": \"{{bg_color}}\", \"position\": { \"y\": 0, \"x\": 0 }, \"kind\": \"rectangle\", \"type\": \"shape\", \"size\": { \"h\": \"{{global_height}}\", \"w\": \"[[{{global_width}}/2]]\" } }, { \"url\": \"https://lh3.googleusercontent.com/kcRJEvRnL6iIvRNqzAQeiTtah12tZjxUxtwnnlvCl6z9GKVfh1p-NRI1u_YKfRLOS5lPtKhOCN4O5x3zVg_w2CI=s0\", \"position\": { \"y\": 0, \"x\": \"[[{{global_width}}/2]]\" }, \"type\": \"image\", \"size\": { \"h\": \"{{global_height}}\", \"w\": \"[[{{global_width}}/2]]\" } }, { \"color\": \"white\", \"text\": \"{{text1}}\", \"fontSize\": 50, \"type\": \"text\", \"position\": { \"y\": \"[[({{global_height}}/2)  - 100]]\", \"x\": 100 } }, { \"color\": \"white\", \"text\": \"{{text2}}\", \"fontSize\": 70, \"type\": \"text\", \"position\": { \"y\": \"[[({{global_height}}/2) ]]\", \"x\": 100 } }, { \"color\": \"white\", \"position\": { \"y\": \"[[({{global_height}}/2)-50]]\", \"x\": 100 }, \"kind\": \"rectangle\", \"type\": \"shape\", \"size\": { \"h\": 4, \"w\": 400 } } ], \"size\": { \"h\": \"{{global_height}}\", \"w\": \"{{global_width}}\" } }")

                                .header("Authorization", "Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                                .when()
                                .put(pathUri)
                                .then().log().ifError();
                Reporter.log("RESPONSE : ", 1, true);
                Reporter.log(response.extract().body().asString(), 1, true);
                return response;
            }

                public ValidatableResponse sendCreateCampaigns(String pathUri, String content_type)
                {
                    Reporter.log("Base URL: " + baseUrl, 1, true);
                    Reporter.log("Endpoint: " + pathUri, 1, true);
                    Reporter.log("\nFINAL REQUEST: ", 1, true);
                    Reporter.log("--------------", 1, true);
                    ValidatableResponse response =
                            given()
                                    .spec(request)
                                    .formParam("name", "AutomationTestCamp")
                                    .formParam("desc", "test")
                                    .formParam("display_name", "test camp")
                                    .formParam("intent","Discovery")
                                    .formParam("audiences", "6676884")
                                    .formParam("channels","{\"type\":\"fb\", \"meta\":{\"fb_id\":\"100012\"}}")
                                    .formParam("template","e57de14b-205b-495e-99e4-fa5425895c20")
                                    .formParam("workspace_id","b98b4bf4-989c-49cd-9597-b287cb8436df")
                                    .formParam("content_type",content_type)
                                    .header("Authorization", "Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                                    .header("X-Kubric-Workspace-ID", "b98b4bf4-989c-49cd-9597-b287cb8436df")
                                    .when()
                                    .post(pathUri)
                                    .then().log().ifError();
                    Reporter.log("RESPONSE : ", 1, true);
                    Reporter.log(response.extract().body().asString(), 1, true);
                    return response;

                }

    public ValidatableResponse patchsharecamps(String uri, JsonObject jsonReqBody)
    {
        Reporter.log("Base URL: " + baseUrl, 1, true);
        Reporter.log("Endpoint: " + uri, 1, true);
        Reporter.log("\nFINAL REQUEST: ", 1, true);
        Reporter.log("--------------", 1, true);
        Reporter.log("\n" + jsonReqBody, 1, true);

        ValidatableResponse response =
                given()
                        .contentType("")
                        .header("Authorization","Bearer q4VF6j48uVZgCFk+hJjwUzwdvCScvBUxXqjwZ9qbj09KFMh1iqoz3I8CRUYk2VJD7Eb1w90ii3QkKLaK+8iG2sYujW2vT9hnO4rf12YZhvM=")
                        .spec(request)
                        .body(jsonReqBody.toString())
                        .header("Content-Type" , "application/json")
                        .when()
                        .patch(uri)
                        .then().log().ifError();
        Reporter.log("RESPONSE : ", 1, true);
        Reporter.log(response.extract().body().asString(), 1, true);

        return response;

    }

            }

