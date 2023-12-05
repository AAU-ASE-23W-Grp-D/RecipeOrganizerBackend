plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.sonarqube") version "4.4.1.3373"
}

group = "at.aau"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("joda-time:joda-time:2.12.5")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.1")

    implementation("org.springframework.boot:spring-boot-starter-actuator:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.0")
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Jacoco report is always generated after tests run
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    // Tests are required to run before generating the report
    dependsOn(tasks.test)

    reports {
        csv.required = false
        html.required = true
        xml.required = true
    }
}

jacoco {
    toolVersion = "0.8.9"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

sonar {
    properties {
        property("sonar.projectKey", "AAU-ASE-23W-Grp-D_RecipeOrganizerBackend")
        property("sonar.organization", "aau-ase-23w-grp-d")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
