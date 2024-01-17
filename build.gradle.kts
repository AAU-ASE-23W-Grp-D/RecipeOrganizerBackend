plugins {
    id("java")
    id("jacoco")
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.sonarqube") version "4.4.1.3373"
    id("com.google.cloud.tools.jib") version "3.4.0"
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
    val springVersion = "3.2.1"
    val springSecurityVersion = "6.2.1"
    val junitVersion = "5.10.1"
    val jjwtVersion = "0.12.3"
    val junitPlatformVersion = "1.10.1"

    implementation("org.springframework.boot:spring-boot-starter-actuator:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
    implementation("org.springframework.boot:spring-boot-starter-validation:$springVersion")
    implementation("org.springframework.security:spring-security-test:$springSecurityVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
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

tasks.sonar {
    // Jacoco is required to run before generating the report
    dependsOn(tasks.jacocoTestReport)
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

jib {
    from {
        image = "bellsoft/liberica-openjre-alpine-musl:17"
    }
    to {
        image = "recipeorganizer/recipeorganizer-ase-" + System.getenv("CURRENT_DEPLOYMENT")
        auth {
            username = "recipeorganizer"
            password = System.getenv("DOCKER_TOKEN")
        }
    }
    container {
        environment = mapOf("SPRING_PROFILES_ACTIVE" to System.getenv("CURRENT_DEPLOYMENT"))
        jvmFlags = listOf("-Xms256m", "-Xmx256m")
        ports = listOf("8080/tcp")
    }
}
