# Kubric-API-Test
To test kubric backend api

### Prerequisites


```
For this project you will nedd to have Java vesrion 8.
```


#To run the project form Circle ci 

Go to the latest job and re run it - Circle ci will run all the test cases.
```
https://app.circleci.com/pipelines/bitbucket/craftworkx/kubric-backend-test/83/workflows/2ea9d604-0245-47ae-b8da-608a725aeda7/jobs/87```



#Assets-api-test


To run the asset service locally-

```1. Go to edit configuration.
2. Add a test ng config.
3. Specify the class that you have to run after selecting test kind as class. In case of assets it would be TestAssetService
4. After specifying class name provide the VM options -ea -DEnvironment=Production if you are running test in production. If you are running test in staging provide VM option as  -ea -DEnvironment=Staging```.


# Studio-api-test


To run the asset service locally-
```1. Go to edit configuration.
2. Add a test ng config.
3. Specify the class that you have to run after selecting test kind as class. In case of assets it would be TestStudio
4. After specifying class name provide the VM options -ea -DEnvironment=Production if you are running test in production. If you are running test in staging provide VM option as  -ea -DEnvironment=Staging.```
