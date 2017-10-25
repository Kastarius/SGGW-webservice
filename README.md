# SGGW-webservice

# Setup app
src/main/resources/application.properties
example db config for MySQL
db.username - db user
db.password - db password
db.url - url to your db / example for MySQL jdbc:mysql://HOST:PORT/SCHEMA_NAME

example db config for MsSQL
in progress ....


add role ADMIN in table "Rola"
INSERT INTO `YOUR_SCHEMA_NAME`.`Rola` (`RolaID`, `Kod`, `Nazwa`) VALUES ('1', 'ADMIN', 'Administrator');
add user admin
INSERT INTO `YOUR_SCHEMA_NAME`.`Uzytkownik` (`UzytkownikID`, `Email`, `Imie`, `Nazwisko`, `Login`, `Haslo`) VALUES ('1', 'admin@mail.com', 'Admin', 'Admin', 'admin', 'admin');
add role to user 
INSERT INTO `YOUR_SCHEMA_NAME`.`RolaUzytkownika` (`UzytkownikID`, `RolaID`) VALUES ('1', '1');


auto init data will be added in future
# build app
mvn install
# run app
mvn spring-boot:run