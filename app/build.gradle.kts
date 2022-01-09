plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("info.solidsoft.pitest") version "1.7.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = "com.github.caay2000"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.github.caay2000.ttk.AppKt")
}

tasks.test {
    useJUnitPlatform()
    dependsOn(tasks.ktlintFormat)
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("io.mockk:mockk:1.12.1")
}

pitest {
    pitestVersion.set("1.7.0")
    junit5PluginVersion.set("0.15")
    targetClasses.add("com.github.caay2000.ttk.*")
    outputFormats.add("HTML")
    timestampedReports.set(false)
    excludedTestClasses.add("**.*IntegrationTest")
    avoidCallsTo.add("kotlin.jvm.internal")
    mutators.addAll("DEFAULTS", "STRONGER")
}
