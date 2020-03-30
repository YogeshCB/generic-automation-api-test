package org.kubric.Teams.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.ValidatableResponse;
import org.kubric.assets.service.AssetsServiceRequestBuilder;
import org.kubric.commonUtils.CSVParametersProvider;
import org.kubric.commonUtils.DataFileParameters;
import org.kubric.commonUtils.JSONFileReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class TeamServiceTest {
    String UrlToUpload = null;
    private static ValidatableResponse response;
    private static ValidatableResponse response2;
    private static ValidatableResponse response3;
    private static ValidatableResponse response4;
    private static ValidatableResponse response5;
    String responseJson;
    TeamServiceRequestBuilder trb;
    JSONFileReader jReader;
    String WS_id =null;
    String Team_id=null;


    @BeforeClass
    public void init() throws IOException {
        String env = System.getProperty("Environment");
        System.out.println("--------- Env name: " + env + " ---------");

        if (env.equalsIgnoreCase("Staging")) {
            trb = new TeamServiceRequestBuilder("/resources/config-files/staging/InferenceService.properties");
        } else if (env.equalsIgnoreCase("Production")) {
            trb = new TeamServiceRequestBuilder("/resources/config-files/Production/InferenceService.properties");
        }
        jReader = new JSONFileReader();

    }


    @Test(enabled = true, priority = 0, description = "Test Case - To get all worksapces ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "getAllWorkspaces.csv", path = "/resources/input-data/Teams-Service")
    public void getAllWorkspaces(String uri) {

        response = trb.getRequest(uri);
        String status = response.extract().jsonPath().getString("status");

        Assert.assertTrue(status.equals("200"));

    }


    @Test(enabled = true, priority = 0, description = "Test Case - To ADD user from the workspace ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "addUsersToWorkspace.csv", path = "/resources/input-data/Teams-Service")
    public void addUsersToWorkspace(String uri) {

        try {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/addUsers.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.sendRequestUsers(jsonReqBody, uri);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));




        } catch (Exception e) {
        e.printStackTrace();
    }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To remove user from the workspace ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "deleteUsersFromWorkspace.csv", path = "/resources/input-data/Teams-Service")
    public void deleteUsersToWorkspace(String uri) {

        try {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/deleteUser.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.sendRequestUsers(jsonReqBody, uri);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));




        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To create the workspace ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "createWorkspace.csv", path = "/resources/input-data/Teams-Service")
    public void createWorkspace(String uri) {

        try {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/createWorkspace.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.sendRequestWorkspace(jsonReqBody, uri);
            String status = response.extract().jsonPath().getString("status");
            WS_id = response.extract().jsonPath().getString("data.workspace_id");
            Assert.assertTrue(status.equals("200"));




        } catch (Exception e) {
            e.printStackTrace();
        }    }



    @Test(enabled = true, priority = 0, description = "Test Case - To delete the workspace ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "createWorkspace.csv", path = "/resources/input-data/Teams-Service")
    public void deleteWorkspace(String uri) {

        try {

            uri= uri+"/"+WS_id;
            response = trb.deleteRequestWorkspace(uri);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));




        } catch (Exception e) {
            e.printStackTrace();
        }    }



    @Test(enabled = true, priority = 0, description = "Test Case - To get the workspace invitation callback ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "getWorkspaceById.csv", path = "/resources/input-data/Teams-Service")
    public void getWorkspaceInvitationCallback(String uri) {

        try {
            response = trb.getWSByID(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To get the workspace upload Urls ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "Wsuploadurls.csv", path = "/resources/input-data/Teams-Service")
    public void getWorkspaceUploadsUrls(String uri) {
        try {


            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/WsUploadUrls.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.sendRequestWorkspace(jsonReqBody, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To get Teams of user ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "getTeams.csv", path = "/resources/input-data/Teams-Service")
    public void getTeams(String uri) {
        try {

            response = trb.getWSByID(uri);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To create a team ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "createTeam.csv", path = "/resources/input-data/Teams-Service")
    public void createTeam(String uri) {
        try {


            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/createTeam.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);

            response = trb.sendRequestWorkspace(jsonReqBody, uri);
            String Team_id = response.extract().jsonPath().getString("data.team_id");

        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To add users to a team ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "addUsersToTeam.csv", path = "/resources/input-data/Teams-Service")
    public void addUsersToTeam(String uri) {
        try {


            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/addUsersToTeams.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.patchsharecamps(uri,jsonReqBody);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }



    @Test(enabled = true, priority = 0, description = "Test Case - To remove users to a team ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "removeUsers.csv", path = "/resources/input-data/Teams-Service")
    public void removeUsersToTeam(String uri) {
        try {


            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/removeUsers.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.patchsharecamps(uri,jsonReqBody);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }



    @Test(enabled = true, priority = 0, description = "Test Case - To get team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "getTeambyId.csv", path = "/resources/input-data/Teams-Service")
    public void getTeambyID(String uri) {
        try {
            response = trb.getWSByID(uri);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));


        } catch (Exception e) {
            e.printStackTrace();
        }    }

    @Test(enabled = true, priority = 0, description = "Test Case - To update meta information of team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "updateTeam.csv", path = "/resources/input-data/Teams-Service")
    public void updateTeam(String uri) {

        try
        {
        JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/updateTeam.json");
        JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
        response = trb.putTeamsUpdate(uri,jsonReqBody);
        String status = response.extract().jsonPath().getString("status");
        Assert.assertTrue(status.equals("200"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To delete team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "updateTeam.csv", path = "/resources/input-data/Teams-Service")
    public void deleteTeam(String uri) {

        try
        {
             uri = uri+Team_id;
            response = trb.deleteRequestWorkspace(uri);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("200"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }

    @Test(enabled = true, priority = 0, description = "Test Case - To share assets team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "shareAssets.csv", path = "/resources/input-data/Teams-Service")
    public void shareAssets(String uri) {

        try
        {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/sahreAssets.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.patchshareAssets(uri,jsonReqBody);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To Un-share assets team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "shareAssets.csv", path = "/resources/input-data/Teams-Service")
    public void unshareAssets(String uri) {

        try
        {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/unshareAssets.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.patchshareAssets(uri,jsonReqBody);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }



    @Test(enabled = true, priority = 0, description = "Test Case - To  Un share folders with team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "shareFolder.csv", path = "/resources/input-data/Teams-Service")
    public void unshareAssetsFolders(String uri) {

        try
        {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/unsahreFolder.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.patchshareAssets(uri,jsonReqBody);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }


    @Test(enabled = true, priority = 0, description = "Test Case - To  share folders with team by ID ", dataProvider = "csv", dataProviderClass = CSVParametersProvider.class)
    @DataFileParameters(name = "shareFolder.csv", path = "/resources/input-data/Teams-Service")
    public void shareAssetsFolders(String uri) {

        try
        {

            JsonObject jsonFileObject = jReader.readJSONFiles("/resources/request-json/Teams-service/unsahreFolder.json");
            JsonObject jsonReqBody = trb.createRequestBody(jsonFileObject);
            response = trb.patchshareAssets(uri,jsonReqBody);
            String status = response.extract().jsonPath().getString("status");
            Assert.assertTrue(status.equals("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }    }
}


