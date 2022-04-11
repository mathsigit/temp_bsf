# Environment

* JAVA JDK 1.8.0_322
* Spring Boot 2.6.3
* Maven 3.5.4
* Oracle Database 19c
* Apache Hive 3.1.2

# How to build

* Preparing for kerberos
    - jaas.conf file
  ```properties
  com.sun.security.jgss.krb5.initiate {
  com.sun.security.auth.module.Krb5LoginModule required
  doNotPrompt=true
  principal="principal_name@principal_domain"
  useKeyTab=true
  keyTab="absolute path of keytab/XXXX.keytab"
  storeKey=true;
  };
   
  com.sun.security.jgss.krb5.accept {
  com.sun.security.auth.module.Krb5LoginModule required
  doNotPrompt=true
  principal="principal_name@principal_domain"
  useKeyTab=true
  keyTab="absolute path of keytab/XXXX.keytab"
  storeKey=true;
  };
  ```
    - krb5.conf file
        + Find out the krb5.conf file path which verify identity from KDC server

## Online build(with internet)

```shell
mvn clean install -DskipTests 
```

## Offline build(without internet)

```shell
mvn clean install -o -gs settings.xml -DskipTests
```

# How to execute java application

## For Production(Executing with kerberos env)

```shell
#data-inspection
cd data-inspection/target

java -jar \
-Dtable.name=ALL \
-Djava.security.auth.login.config=absolute path of jaas.conf/hive-jaas.conf \
-Djava.security.krb5.conf=absolute path of krb5.conf/krb5.conf \
-Djavax.security.auth.useSubjectCredsOnly=False \
data-inspection-0.0.1-SNAPSHOT.jar

#mail-alert
cd mail-alert/target

java -jar \
-Dtable.name=ALL \
mail-alert-0.0.1-SNAPSHOT.jar
```

## For Developing(Executing with kerberos env)

```shell
cd target

#data-inspection
java -jar \
-Dtable.name=ALL \
-Djava.security.auth.login.config=/Users/heyongan/Documents/git/github/mathsigit/DataInspection/data-inspection/dev/hive-jaas.conf \
-Djava.security.krb5.conf=/Users/heyongan/Documents/git/github/mathsigit/DataInspection/data-inspection/dev/krb5.conf \
-Djavax.security.auth.useSubjectCredsOnly=False \
-Dspring.profiles.active=dev \
data-inspection-0.0.1-SNAPSHOT.jar

#mail-alert
java -jar \
-Dtable.name=ALL \
-Dspring.profiles.active=dev \
mail-alert-0.0.1-SNAPSHOT.jar
``` 