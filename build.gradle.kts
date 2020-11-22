import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.10.RELEASE" apply false
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10" apply false
    kotlin("plugin.jpa") version "1.4.10"
    kotlin("plugin.allopen") version "1.4.10"
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

group = "com.rutesun"
version = "0.0.1-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "1.8"
        }
    }
    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "kotlin-jpa")
    apply(plugin = "kotlin-spring")


    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        runtimeOnly("mysql:mysql-connector-java")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test:1.4.10")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.10")

    }

}

project(":app") {
    dependencies {
        implementation(project(":core:service"))
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.flywaydb:flyway-core")
    }
}

project(":core") {
    subprojects {
        val bootJar: BootJar by tasks
        val jar: Jar by tasks

        bootJar.enabled = false
        jar.enabled = true

        dependencies {
            implementation("org.springframework.boot:spring-boot-starter")
            implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        }
    }
    project(":core:service") {
        dependencies {
            implementation(project(":core:domain"))
            implementation("org.apache.commons:commons-lang3:3.11")
        }
    }

    project(":core:domain") {
        dependencies {

            testRuntimeOnly("com.h2database:h2")
        }
    }
}
