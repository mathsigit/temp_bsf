# Installation Guide

## Environment

You should check the environment of the production, and following the version as below:

* JAVA OPENJDK 1.8.0_322
* Maven 3.5.4
* Oracle Database 19c
* Apache Hive 3.1
* Redhat 7.9

## DB Schema and Data

* Created database and datatable according to the db schema file : `Data Schema.pdf`
* Insert data about application configuration into datatable: `DI_CONFIG` and `DI_TABLES`

## Configuration

Below are the path of configuration file you should modify.
1. `${SOURCE_CODE_ROOT_PATH}/data-inspection/src/main/resources/application.yaml` 
2. `${SOURCE_CODE_ROOT_PATH}/data-inspection/src/main/resources/persistence-db-config.yaml`
3. `${SOURCE_CODE_ROOT_PATH}/mail-alert/src/main/resources/application.yaml`
4. `${SOURCE_CODE_ROOT_PATH}/mail-alert/src/main/resources/persistence-db-config.yaml`

### Modify the configuration.
* **data-inspection/src/main/resources/application.yaml**
```yaml
 jasypt:
   encryptor:
     password: fubon
```
 
Note: Changing the password from `fubon` to the value which you want to encrypt in the `persistence-db-config.yaml`.

* **/data-inspection/src/main/resources/persistence-db-config.yaml**
```yaml
spring:
  datasource:
    driver-class-name: oracle.jdbc.driver.OracleDriver
    jdbc-url: jdbc:oracle:thin:@10.3.0.73:1521:devzxsm
    username: ENC(MUuq6j1C0gJqVZE85gbOJR2fXd2jbnBn)
    password: ENC(KvVLNNJRp0sc26tfUXPhhKOms3H278XO)


  hive-datasource:
    driver-class-name: org.apache.hive.jdbc.HiveDriver
    jdbc-url: jdbc:hive2://10.4.0.25:10000/default;principal=hive/_HOST@KDC.COM;
```

Note: 
1. Changing the `jdbc-url`, `username`, `password` from the default value to which you wanted.
2. `username` and `password` must follow the format: `ENC(YOUR_ENCRYPED_VALUE)`. 
3. Encrypting via this website : https://www.devglan.com/online-tools/jasypt-online-encryption-decryption
and the `Secret Key` is the property `password`  in the `application.yaml`.

* **mail-alert/src/main/resources/application.yaml**
```yaml
 jasypt:
   encryptor:
     password: fubon

 mail:
   sender:
     email: "noreply@fubon.com"
     passwd: ENC(WEFHaycaZBurnujSJ+nUGAHHXkgwvhKF)
   smtp:
     host: "smtp.gmail.com"
     port: "465"
     auth: "true"
     socketfactory:
       port: "465"
       class: "javax.net.ssl.SSLSocketFactory"
```

Note:
1. Changing the `password`, `email`, `password`, `smtp.host`, `smtp.port`, `socketfactory.port` from the default value to which you wanted.
2. `password` must follow the format: `ENC(YOUR_ENCRYPED_VALUE)`.
3. Encrypting via this website : https://www.devglan.com/online-tools/jasypt-online-encryption-decryption
   and the `Secret Key` is the property `password`.

* **mail-alert/src/main/resources/persistence-db-config.yaml**
```yaml
spring:
  datasource:
    driver-class-name: oracle.jdbc.driver.OracleDriver
    jdbc-url: jdbc:oracle:thin:@10.3.0.73:1521:devzxsm
    username: ENC(MUuq6j1C0gJqVZE85gbOJR2fXd2jbnBn)
    password: ENC(KvVLNNJRp0sc26tfUXPhhKOms3H278XO)
```

Note:
1. Changing the `jdbc-url`, `username`, `password` from the default value to which you wanted.
2. `username` and `password` must follow the format: `ENC(YOUR_ENCRYPED_VALUE)`.
3. Encrypting via this website : https://www.devglan.com/online-tools/jasypt-online-encryption-decryption
   and the `Secret Key` is the property `password`  in the `application.yaml`.

## Preparing for accessing Hive which integrated kerberos.

* Step 1: Prepare a keytab that can be authenticated by kerberos and has access to hive.
* Step 2: Prepare a `hive-jaas.conf` file and the content text is as below:
```properties
com.sun.security.jgss.krb5.initiate {
    com.sun.security.auth.module.Krb5LoginModule required
    doNotPrompt=true
    principal="YOUR_PRINCIPAL@YOUR_REALM"
    useKeyTab=true
    keyTab="PATH/OF/YOUR_KEYTAB.keytab"
    storeKey=true;
};

com.sun.security.jgss.krb5.accept {
    com.sun.security.auth.module.Krb5LoginModule required
    doNotPrompt=true
    principal="YOUR_PRINCIPAL@YOUR_REALM"
    useKeyTab=true
    keyTab="PATH/OF/YOUR_KEYTAB.keytab"
    storeKey=true;
};
```

Note:
  1. Changing the `principal`, `keyTab` from the default value to which you wanted.
  2. The `keyTab` is the keytab file which prepared by `Step 1`.

* Step 3: Find out the path of `krb5.conf` file. The krb5.conf file tells Kerberos clients where the Kerberos server is 
on the network. On Linux and UNIX machines, krb5.conf typically resides on `/etc`.

## Building java application from source code

On the root path of source code.

```shell
mvn clean install -o -gs settings.xml -DskipTests
```


## Executing java application

On the root path of source code.

* Data Inspection
```shell
cd data-inspection/target

java -jar \
-Dtable.name=ALL \
-Djava.security.auth.login.config=absolute path of jaas.conf/hive-jaas.conf \
-Djava.security.krb5.conf=absolute path of krb5.conf/krb5.conf \
-Djavax.security.auth.useSubjectCredsOnly=False \
data-inspection-0.0.1-SNAPSHOT.jar
```

* Mail Alert
```shell
cd mail-alert/target

java -jar \
-Dtable.name=ALL \
mail-alert-0.0.1-SNAPSHOT.jar
```