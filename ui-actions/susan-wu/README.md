# Ivan Pronin's script for listing gift cards on Raise.com
To compile source code and create eclipse project use gradle command:
```gradle build eclipse```
To create a runnable jar file run:
```gradle shadowJar```
This file will be created under <project_location>/build/libs directory

## Running the script
The following files are needed to run the script:
- runnable jar file
- input.csv (comma-separated) - contains listing data. Each row represents one script iteration
- app.properties - contains user login data to both sites

**Running:**
- Place all three files in the same directory, e.g. D:\Script\
- Open command line and **cd** to that directory 
   - ```cd D:```
   - ```cd Script```
- Run the jar: ```java -jar <jar_name>```, e.g. ```java -jar susan-wu.jar```
The script should start running, you'll see its output in the console:
``` 
D:\Users\ipronin\Downloads>java -jar susan-wu.jar

=================Starting iteration #1===================

Printing CSV row data for this iteration: STORE_NAME=Gap1, ACCOUNT_NUMBER=6003860000000000, PIN=733, COST_PRICE=25, PRIC
E=24
Starting ChromeDriver 2.27.440174 (e97a722caafc2d3a8b807ee115bfb307f7d2cfd9) on port 35743
Only local connections are allowed.
Jan 31, 2017 4:32:03 PM org.openqa.selenium.remote.ProtocolHandshake createSession
INFO: Attempting bi-dialect session, assuming Postel's Law holds true on the remote end
Jan 31, 2017 4:32:05 PM org.openqa.selenium.remote.ProtocolHandshake createSession
INFO: Detected dialect: OSS
Current page is: https://www.topcashback.com/login 
```
