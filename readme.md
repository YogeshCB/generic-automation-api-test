# Generic-Api-Test-Framework 
To test kubric backend api



# To run the project form Circle ci 

Go to the latest job and re run it - Circle ci will run all the test cases.

https://app.circleci.com/pipelines/github/YogeshScrapy/-generic-automation-Kubric-api-test


# Prerequisites- to run locally

```
For this project you will nedd to have Java vesrion 8.
```

# Api test to run locally


To run the locally-Assets and studio

1. Go to edit configuration.
2. Add a test ng config.
3. Specify the class that you have to run after selecting test kind as class. In case of assets it would be TestAssetService
4. After specifying class name provide the VM options -ea -DEnvironment=Production if you are running test in production. If you are running test in staging provide VM option as  -ea -DEnvironment=Staging```.


1. Go to edit configuration.
2. Add a test ng config.
3. Specify the class that you have to run after selecting test kind as class. In case of assets it would be TestStudio
4. After specifying class name provide the VM options -ea -DEnvironment=Production if you are running test in production. If you are running test in staging provide VM option as  -ea -DEnvironment=Staging.```


# High Level design of framework can be seen here 
https://docs.google.com/document/d/1_nxswOpnGldaDwGxHbZupCswovsXt6d9P22_dyS0GAc/edit
