# Code structure
A high level overview of the code structure we should have for the project:

```bash
├── config # Gradle configs
│   └── detekt
├── gradle # Gradle wrapper
│   └── wrapper
└── src
    ├── integration # integration tests
    │   ├── kotlin
    │   │   └── com
    │   │       └── ki
    │   │           └── app
    │   │               └── hello
    │   │                   ├── controller # controller intergration tests
    │   │                   ├── repository # repository intergration tests
    │   │                   └── service    # service intergration tests
    │   └── resources
    ├── main
    │   ├── kotlin
    │   │   └── com
    │   │       └── ki
    │   │           └── app
    │   │               ├── config # spring boot application configs
    │   │               ├── hello # hello domain - everything related to hello should live here
    │   │               │   ├── controller # controller for messages and rest request
    │   │               │   │   ├── message # if you have messages
    │   │               │   │   ├── request
    │   │               │   │   └── response    
    │   │               │   ├── model # data classes that are used within the application 
    │   │               │   ├── exampleexternalapi # external API classes structure, we should have a package per API 
    │   │               │   │   └── api # request and response for the API 
    │   │               │   │       ├── request
    │   │               │   │       └── response
    │   │               │   ├── repository # repository classes for the DB 
    │   │               │   ├── service # service classes 
    │   │               └── utils # utils functions and classes
    │   └── resources 
    │       ├── db # flyway migrations
    │       │   └── migration
    │       └── static # Redoc config
    └── test # unit tests
        └── kotlin
            └── com
                └── ki
                    └── app
                        └── hello
                            ├── controller # controller tests
                            └── service # service tests
```
