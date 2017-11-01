# SGGW-webservice

### Setup app
Requirements:
1. Java 8
2. Maven 3

For developers:
1. create local configuration file src/main/resources/local.properties
local configuration (local.properties) override the default configuration (application.properties), local.properties is untracked by git
2. configure connection to your database, example db config for MySQL and SQL Server 
you can find in src/main/resources/application.properties
3. Add default user and role to your database. 
    1. add role ADMIN in table "Rola"
        * ```INSERT INTO `YOUR_SCHEMA_NAME`.`Rola` (`RolaID`, `Kod`, `Nazwa`) VALUES ('1', 'ADMIN', 'Administrator');```
    2. add user admin
        * ```INSERT INTO `YOUR_SCHEMA_NAME`.`Uzytkownik` (`UzytkownikID`, `Email`, `Imie`, `Nazwisko`, `Login`, `Haslo`) VALUES ('1', 'admin@mail.com', 'Admin', 'Admin', 'admin', 'admin');```
    3. add role to user 
        * ```INSERT INTO `YOUR_SCHEMA_NAME`.`RolaUzytkownika` (`UzytkownikID`, `RolaID`) VALUES ('1', '1');```
4. build and run app, try to open swagger ui http://localhost:8090/swagger-ui.html

#####PS. If you add some new config, don't forget add default prop value to application.properties, will be nice if you add description, what it is for or what this does

For app administrator
1. create environment configuration file, example config you can find in src/main/resources/application.properties
2. build app(run mvn install in in catalog where is pom.xml file)
3. run app 
    * mvn spring-boot:run -Dspring.config.location=C:\environment configuration file location\environment.properties
    *  or
    * java -jar target\SGGWSupportWS.jar --spring.config.location=C:\environment configuration file location\environment.properties

> auto init data will be added in future
