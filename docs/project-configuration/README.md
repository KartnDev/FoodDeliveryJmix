# Food Delivery Local Development Setup Guide

Welcome to the setup guide for the Food Delivery application. We'll walk you through the process of 
setting up a local development environment using Docker for easy infrastructure management.

## Steps to Run Food Delivery Locally:
1. Local infrastructure
2. Setup hosts for application urls and hosts
3. Configuring Keycloak
4. Import tokens from Keycloak into the application properties for each standalone systems
5. Run application

## Setting Up Local Infrastructure

Before running the Food Delivery application, you need to deploy Postgres and Keycloak locally using Docker. You have two options for this:

* **Option 1**: Run Docker Compose from the console:
    ```shell
    docker-compose --project-directory FoodDelivery-all up
    ```

* **Option 2**: If you're using IntelliJ IDEA Ultimate, open the `docker-compose.yml` file and click the "run" button on the left side.

  <img alt="docker-compose-deploy.png" src="public/docker-step/docker-compose-deploy.png" width="50%"/>

With either option, you'll have all the necessary infrastructure deployed locally and ready for use.

## Configuring Host Names

To ensure smooth operation of the Food Delivery application, we need to configure hostnames for the three systems running on different ports locally:

1. **Restaurant System** (port 8080)
2. **Order System** (port 8081)
3. **Courier System** (port 8082)

### Option 1: Define Separate Hostnames

Due to limitations within Vaadin when using multiple applications on one hostname, we'll assign separate hostnames to each system on the same local host (127.0.0.1).

Add the following entries to your hosts file:

```text
    127.0.0.1	localhost
    255.255.255.255	broadcasthost
    ::1             localhost
    127.0.0.1       order.io
    127.0.0.1       restaurant.io
    127.0.0.1       courier.io
```

### Option 2: Proxy Hosts (Alternative Solution)

If you prefer not to use port numbers in the hostnames (e.g., "courier.io:8081", "restaurant.io:8080"), you can follow this alternative solution:

1. Allocate local IPs for host names for each system:

    ```text
        127.0.0.1	localhost
        255.255.255.255	broadcasthost
        ::1             localhost
       
        # Example IP allocations (replace with actual IPs)
        156.28.12.56    order.io 
        182.11.244.12   restaurant.io
        143.192.11.12   courier.io
    ```

2. Proxy all systems from the local host with ports into the selected IPs. 
   The specific commands for this vary depending on your operating system.

    ```text
        # Pseudo-proxy setting
        127.0.0.1:8081 -> 182.11.244.12    # order system local IP
        127.0.0.1:8080 -> 156.28.12.56     # restaurant system local IP
        127.0.0.1:8082 -> 143.192.11.12    # courier system local IP
    ```

3. Utilize the mapped hostnames:

    ```text
        order.io      -> 127.0.0.1:8081 (order system)
        restaurant.io -> 127.0.0.1:8080 (restaurant system)
        courier.io    -> 127.0.0.1:8082 (courier system)
    ```

Choose the option that best suits your preferences and environment for seamless 
operation of the Food Delivery application.

## Local Development

Before running the applications, it's important to remember that certain systems rely on specific modules. 
Ensure smooth development by publishing add-ons into the local Maven repository.

## Configuring Keycloak

To set up Keycloak for the Food Delivery application, follow these manual configuration steps:

1. Access Keycloak at http://localhost:8070 using the credentials admin-admin.
2. On the left side of the admin bar, navigate to the list of realms and create a new realm.<br/>
   <img alt="create-realm-image.png" src="public/keycloak-step/create-realm-image.png" width="50%"/><br/>
3. Name the realm "root" as it will serve as our standalone realm for Food Delivery.<br/>
   <img alt="img.png" src="public/keycloak-step/realm-form-creation.png" width="50%"/><br/>
4. Select the newly created realm and navigate to the "Clients" tab.<br/>
   <img alt="client-creation.png" src="public/keycloak-step/client-creation.png" width="50%"/><br/>
5. Click "Create Client".
6. In the general settings, fill in the fields with the name and ID as "fooddelivery".<br/>
   <img alt="client-general.png" src="public/keycloak-step/client-general.png" width="50%"/><br/>
7. Configure capabilities by applying all rules.<br/>
   <img alt="capability-client-step.png" src="public/keycloak-step/capability-client-step.png" width="50%"/><br/>
8. Configure Login settings:
    - Root URL: Always http://localhost:8081
    - Home URL: http://localhost:8081, where the order system is placed.
    - Valid redirect URIs: List all localhost addresses from ports 8080 to 8082 and the respective system hostnames.
    - Leave Valid post logout redirect URIs unchanged.
    - Permit all Web origins by setting the value as "+".<br/>

   <img alt="login-settings.png" src="public/keycloak-step/login-settings.png" width="50%"/><br/>
   <img alt="login-setting-upd.png" src="public/keycloak-step/login-setting-upd.png" width="50%"/>
9. Add a **realm role** named "system-full-access".<br/>
   <img alt="realm-role-sys-full-access.png" src="public/keycloak-step/realm-role-sys-full-access.png" width="50%"/><br/>
10. Add a realm **client-scope**.<br/>
    <img alt="client-scope-setting.png" src="public/keycloak-step/client-scope-setting.png" width="50%"/><br/>
11. Add a scope **mapper** for the created **client-scope**.<br/>
    <img alt="add-new-configured-mapper.png" src="public/keycloak-step/add-new-configured-mapper.png" width="50%"/><br/>
12. Select "create configured mapper" and choose "user realm role".<br/>
    <img alt="realm-role-mapper-list.png" src="public/keycloak-step/realm-role-mapper-list.png" width="50%"/><br/>
13. Fill in the realm role mapper fields, naming it "realm-role", and set the claim name as "realm role".
    This will apply an additional "claim-key" with the same name containing all realm roles inside the JWT token.<br/>
    <img alt="realm-role-mapper-field.png" src="public/keycloak-step/realm-role-mapper-field.png" width="50%"/><br/>
14. Go back to the created client "fooddelivery", navigate to the "Client Scopes" tab, and click "Add client scope".<br/>
    <img alt="client-scope-assignition.png" src="public/keycloak-step/client-scope-assignition.png" width="50%"/><br/>
15. Add our client-scope as default. You should see our client scope in the list.<br/>
    <img alt="add-client-scope.png" src="public/keycloak-step/add-client-scope.png" width="50%"/><br/>
16. Finally, create a user and fill in the details as desired.<br/>
    <img alt="add-user.png" src="public/keycloak-step/add-user.png" width="50%"/><br/>
17. Change the password for this user.<br/>
    <img alt="change-user.png" src="public/keycloak-step/change-user.png" width="50%"/><br/>
18. Navigate to the "Role mapping" tab and assign "system-full-access" to the user.<br/>
    <img alt="role-assign-on-user.png" src="public/keycloak-step/role-assign-on-user.png" width="50%"/><br/>
    <img alt="assigning-role.png" src="public/keycloak-step/assigning-role.png" width="50%"/>
    <br/>
Your configuration is nearly complete.
    Just remember the password for the created user.
    Now, Keycloak is set up for use with the Food Delivery application.


## Importing Client Secret and Testing

To complete the Keycloak configuration, you need to import the client secret for the "fooddelivery" client into the application properties. Follow these steps:

<img alt="credential-import.png" src="public/keycloak-import-step/credential-import.png" width="50%"/>

1. Copy the client secret.
2. Paste it into each application properties file using the following key:

    ```properties
    spring.security.oauth2.client.registration.keycloak.client-secret=YOUR_SECRET_PASTE_HERE
    ```

### Running and Testing the Application

1. Run any of the systems (e.g., Restaurant, Order, Courier) to ensure everything is functioning correctly.
2. For example, if you're testing the Order System, navigate to http://order.io:8081 in your web browser.<br/>
   <img alt="login-form.png" src="public/login-step/login-form.png" width="50%"/><br/>
3. You should see the Keycloak login form. Log in using your credentials.<br/>
   <img alt="login-result.png" src="public/login-step/login-result.png" width="50%"/><br/>
4. If everything is configured correctly, you will be logged in and redirected to the main page of the application.
5. Congratulations! You can now run all applications and explore the **Food Delivery Demo Application**.

With the client secret imported and the application successfully tested, 
you're ready to use Keycloak with the Food Delivery application. Enjoy exploring the demo!

