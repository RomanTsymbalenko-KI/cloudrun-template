import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.gitlab.arturbosch.detekt") version ("1.23.5")
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("org.flywaydb.flyway") version "10.1.0"

    id("com.adarshr.test-logger") version "3.2.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"

    idea
}

group = "com.ki"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springCloudGcpVersion"] = "5.1.0"
extra["springCloudVersion"] = "2023.0.0"

sourceSets {
    // note: the name of the sourceSet impacts the name of the configurations
    create("integration") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

configurations["integrationRuntimeOnly"].extendsFrom(configurations.runtimeOnly.get())

val integrationImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("com.google.cloud:spring-cloud-gcp-starter")
    implementation("com.google.cloud:spring-cloud-gcp-starter-logging")
    implementation("com.google.cloud:spring-cloud-gcp-starter-secretmanager")

    // metrics
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.brave:brave-instrumentation-messaging")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.2.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    with("org.springframework.security:spring-security-test") {
        testImplementation(this)
        integrationImplementation(this)
    }

    with("io.kotest:kotest-runner-junit5:5.6.2") {
        testImplementation(this)
        integrationImplementation(this)
    }

    with("io.kotest:kotest-assertions-core:5.6.2") {
        testImplementation(this)
        integrationImplementation(this)
    }

    with("io.kotest.extensions:kotest-extensions-spring:1.1.3") {
        testImplementation(this)
        integrationImplementation(this)
    }

    with("io.mockk:mockk:1.13.5") {
        testImplementation(this)
        integrationImplementation(this)
    }

    integrationImplementation("org.springframework.boot:spring-boot-starter-test")
    integrationImplementation("com.ninja-squad:springmockk:4.0.2")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.5")
}

dependencyManagement {
    imports {
        mavenBom("com.google.cloud:spring-cloud-gcp-dependencies:${property("springCloudGcpVersion")}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks =
        (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1).also {
            println("Setting maxParallelForks to $it")
        }
}

testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
}

val integrationTest =
    task<Test>("integrationTest") {
        environment("spring.profiles.active", "integration")
        useJUnitPlatform()

        description = "Runs integration tests."
        group = "verification"

        testClassesDirs = sourceSets["integration"].output.classesDirs
        classpath = sourceSets["integration"].runtimeClasspath
        shouldRunAfter("test")
    }

tasks.check { dependsOn(integrationTest) }

detekt {
    toolVersion = "1.23.5"
    config.setFrom(file("config/detekt/detekt.yml"))
    source.setFrom(files("src/main/kotlin"))
    autoCorrect = true
}

idea {
    module {
        testSources.from(sourceSets["integration"].kotlin.srcDirs)
    }
}

koverReport {
    filters {
        excludes {
            packages("com.ki.app.config")
            packages("com.ki.app.utils")
            packages("*.model")
            classes("com.ki.app.SpringbootApplicationKt")
        }
    }

    verify {
        rule("Basic Line Coverage") {
            isEnabled = true
            bound {
                minValue = 60 // Minimum coverage percentage
                metric = kotlinx.kover.gradle.plugin.dsl.MetricType.LINE
                aggregation = kotlinx.kover.gradle.plugin.dsl.AggregationType.COVERED_PERCENTAGE
            }
        }
        rule("Basic Branch Coverage") {
            isEnabled = true
            bound {
                minValue = 90 // Minimum coverage percentage
                metric = kotlinx.kover.gradle.plugin.dsl.MetricType.BRANCH
                aggregation = kotlinx.kover.gradle.plugin.dsl.AggregationType.COVERED_PERCENTAGE
            }
        }
    }
}
