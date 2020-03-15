package org.kubric.assets.service;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.ValidatableResponse;
import org.kubric.commonUtils.CSVParametersProvider;
import org.kubric.commonUtils.DataFileParameters;
import org.kubric.commonUtils.JSONFileReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestAssetService {
    String UrlToUpload = null;
    private static ValidatableResponse response;
    private static ValidatableResponse response2;
    private static ValidatableResponse response3;
    private static ValidatableResponse response4;
    private static ValidatableResponse response5;
    String responseJson;
    assetsServiceRequestBuilder arb;
    JSONFileReader jReader;
    String id = null;
    String task_id = null;
    String task_progress = null;


    @BeforeClass
    public void init() throws IOException {
        String env = System.getProperty("Environment");
        System.out.println("--------- Env name: " + env + " ---------");

        if (env.equalsIgnoreCase("Staging")) {
            arb = new assetsServiceRequestBuilder("/resources/config-files/staging/InferenceService.properties");
        } else if (env.equalsIgnoreCase("Production")) {
            arb = new assetsServiceRequestBuilder("/resources/config-files/Production/InferenceService.properties");
        }
        jReader = new JSONFileReader();

    }


    @Test(enabled = true, priority = 0, description = "Generic test - To check if upload is working or not", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "GenericUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testGenericUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload AI file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "AEPuploadtest.csv", path = "/resources/input-data/Assets-Service")
    public void testAepUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );
            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload AI file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "AIFileupload.csv", path = "/resources/input-data/Assets-Service")
    public void testAIpUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );
            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload GIF file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "GIFFileUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testGIFUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*

             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload JPE file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "JpeFileUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testJPEUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload JPEG file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "JPEGUploadTest.csv", path = "/resources/input-data/Assets-Service")
    public void testJPEGUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload JPG file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "JPGUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testJPGUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload MKV file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "MKVFileupload.csv", path = "/resources/input-data/Assets-Service")
    public void testMKVUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "video");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(120000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload MOV file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "movFolder.csv", path = "/resources/input-data/Assets-Service")
    public void testMOVUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "video");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(120000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload MP3 file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "mp3Upload.csv", path = "/resources/input-data/Assets-Service")
    public void testMP3Upload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "audio");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(120000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload MP4 file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "Mp4Upload.csv", path = "/resources/input-data/Assets-Service")
    public void testMP4Upload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "audio");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload OTF file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "OTFUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testOTFUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "font");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload PDF file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "PDFUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testPDFUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload PNG file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "PNGUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testPNGUload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload PSD file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "PSDFileUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testPSDUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload SKETCH file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "Sketchfile.csv", path = "/resources/input-data/Assets-Service")
    public void testSKETCHUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload SRT file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "SrtUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testSRTUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload SVG file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "SvgUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testSVGUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload TITF file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "TITfupload.csv", path = "/resources/input-data/Assets-Service")
    public void testTITFUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "font");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload TTF file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "TTFUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testTTFUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "font");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload five JPEG file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "UploadJpeg.csv", path = "/resources/input-data/Assets-Service")
    public void testJPEG5Upload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload WAV file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "WAVUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testWAVpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload WEBM file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "WEBMUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testWEBMUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "blob");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can upload ZIP file", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "ZipFileUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testZIPUpload(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "archive");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );

            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can create a folder inside the Assets System", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "CreateFolder.csv", path = "/resources/input-data/Assets-Service")
    public void testCreateFolder(String uri, String Foldername) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/CreateFolder.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            jsonReqBody.addProperty("name", Foldername);
            System.out.println(jsonReqBody);
            System.out.println(uri);

            response = arb.sendGenericPostRequestUploadUrl(uri, jsonReqBody);
            // Assert.assertTrue(task_progress.equals(Foldername));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = true, priority = 1, description = "Test case- To check if  API can create a folder inside the Assets System", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "CreateFolder.csv", path = "/resources/input-data/Assets-Service")
    public void testCreateNestedFolder(String uri, String Foldername) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/nestedFoldercheck.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            jsonReqBody.addProperty("name", Foldername);
            System.out.println(jsonReqBody);
            System.out.println(uri);

            response = arb.sendGenericPostRequestUploadUrl(uri, jsonReqBody);
            // Assert.assertTrue(task_progress.equals(Foldername));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(enabled = true, priority = 1, description = "Test case- To upload assets inside folder", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "JPGUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testUploadAssetsInsideNewlyCreatedFolder(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            temp.addProperty("path", "/root/3911ce73-bd08-41c8-9489-da1d4854588c");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );


            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(enabled = true, priority = 1, description = "Test case- To check if asset can be uploaded inside nested folder", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "JPGUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testUploadAssetsInsideNestedCreatedFolder(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            temp.addProperty("path", "root/3911ce73-bd08-41c8-9489-da1d4854588c/420a16a8-098c-4c03-aafe-14d875c17888");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);

            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );


            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test(enabled = true, priority = 1, description = "Test case- To check if ZIP can be uploaded inside nested folder", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "ZipFileUpload.csv", path = "/resources/input-data/Assets-Service")
    public void testUploadZipInsideNestedCreatedFolder(String uri, String checkFileName, String japthFilename, String uri2, String uri3, String folderpath, String Jpath_ID,String uri4) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/Genericupload.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            JsonArray details = new JsonArray();
            JsonObject temp = new JsonObject();
            temp.addProperty("filename", (checkFileName.replaceAll("[\\[\\]\\(\\)]", "")));
            temp.addProperty("type", "image");
            temp.addProperty("path", "root/3911ce73-bd08-41c8-9489-da1d4854588c/420a16a8-098c-4c03-aafe-14d875c17888");
            details.add(temp);
            jsonReqBody.add("details", details);
            response = arb.sendRequestAssets(jsonReqBody, uri);
            String id_value= response.extract().jsonPath().getString(Jpath_ID);
            System.out.println("id_value"+id_value);


            responseJson = response.extract().jsonPath().getString("filename");
            String UrlToUpload = response.extract().jsonPath().getString("url").replaceAll("[\\[\\]\\(\\)]", "");
            Assert.assertTrue(responseJson.equals(checkFileName));
            id = response.extract().jsonPath().getString("id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            if (responseJson.equals(checkFileName)) {
                System.out.println("The Url for the file name has been generated");
            } else {
                System.out.println("Failed to upload url");
            }

            /*
             * Upload image to url
             * */
            response2 = arb.sendRequestUploadUrl(UrlToUpload, folderpath);
            JsonObject jsonFileObject1 = jReader.readJSONFiles("/resources/request-json/Assets-Service/uploadConfirm.json");
            JsonObject jsonReqBody1 = arb.createRequestBody(jsonFileObject1);
            jsonReqBody1.addProperty("id", id);
            jsonReqBody1.addProperty("filename",(checkFileName.replaceAll("[\\[\\]\\(\\)]", "")) );


            /*
             * Check for confirm upload
             */
            response = arb.sendRequestAssets(jsonReqBody1, uri2);
            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url = uri3 + task_id;
            Thread.sleep(60000);
            response3 = arb.getTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

            /*
             * Check for assets upload
             * */
            response3 = arb.getUnzipTaskID(task_url);
            task_progress = response3.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));

            String Id_Japth =id_value.replaceAll("[\\[\\]\\(\\)]", "");
            String unzip_uri =uri4+Id_Japth;
            response4 = arb.getTaskID(unzip_uri);

            task_id = response.extract().jsonPath().getString("task_id").replaceAll("[\\[\\]\\(\\)]", "");
            ;
            String task_url2 = uri3 + task_id;
            Thread.sleep(60000);
            /*
             * Check for assets upload
             * */
            response5 = arb.getUnzipTaskID(task_url2);
            task_progress = response5.extract().jsonPath().getString("progress");
            System.out.println(task_progress);

            Assert.assertTrue(task_progress.equals(String.valueOf(100)));




        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test(enabled = true, priority = 1, description = "Test Case - to download assets. This test will check api - (api/v1/assets/folder/zip)", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "DownloadAssets.csv", path = "/resources/input-data/Assets-Service")
    public void testDownloadAsset(String uri) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/DownloadAssets.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            System.out.println(jsonReqBody);
            response = arb.sendGenericPostRequestUploadUrl(uri, jsonReqBody);
            String s2= "success";
            String s1= response.extract().jsonPath().getString("status");
            Thread.sleep(30000);
            Assert.assertTrue(s2.equals(s1));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Test(enabled = true, priority = 1, description = "Test Case - to download mutiple  Folder. This test will check api - (api/v1/assets/folder/zip)", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "DownloadMutipleFolder.csv", path = "/resources/input-data/Assets-Service")
    public void testDownloadMutipleFolder(String uri) {

        try {
            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Assets-Service/mutiplefolder.json");
            JsonObject jsonReqBody = arb.createRequestBody(jsonFileObject);
            System.out.println(jsonReqBody);
            response = arb.sendGenericPostRequestUploadUrl(uri, jsonReqBody);
            String s2= "success";
            String s1= response.extract().jsonPath().getString("status");
            Thread.sleep(30000);
            Assert.assertTrue(s2.equals(s1));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
