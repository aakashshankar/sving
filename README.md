# Medical dictation transcriber - Client using Java Swing

## Prerequisites
1. JDK 17 or greater
2. Gradle 8 or greater

<strong>Preferably, open this project in IntelliJ Idea, which autodetects the Gradle project structure.
</strong>

## Instructions
1. This Swing based client will be run using the command line, i.e, via `gradlew` which is wrapper on top of gradle
2. We can choose to run it in 2 ways, the default way is for the client to communicate with the Railway deployment, and passing a local flag will make it run locally.
3. To run the client, run the following command:
```shell
./gradlew run
```
To connect to the local server:
```shell
./gradlew run --args='local'
```
## Screens
### Login
![Screenshot 2023-12-18 at 11.29.59 PM.png](src/main/resources/login-page.png)

### Register a new clinic
![Screenshot 2023-12-18 at 11.36.50 PM.png](src/main/resources/register-page.png)

### Tenant dashboard
![Screenshot 2023-12-18 at 11.37.17 PM.png](src/main/resources/tenant.png)

### Adding a new patient (click on the + button)
![Screenshot 2023-12-18 at 11.37.52 PM.png](src/main/resources/add-page.png)

### Recording audio
![Screenshot 2023-12-18 at 11.38.13 PM.png](src/main/resources/record.png)

### After transcribing & summarizing (click on transcribe and then summarize)
![Screenshot 2023-12-18 at 11.38.53 PM.png](src/main/resources/transcribe.png)

### Patient Dashboard
![Screenshot 2023-12-18 at 11.39.45 PM.png](src/main/resources/patient.png)