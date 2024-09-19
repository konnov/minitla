plugins {
    id("java")
    application
}

group = "com.github.konnov.minitla"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "com.github.konnov.minitla.Main"
}

tasks.test {
    useJUnitPlatform()
}