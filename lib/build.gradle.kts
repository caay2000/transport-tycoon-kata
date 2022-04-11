plugins {
    application
    kotlin("jvm") version "1.6.10"
    id("info.solidsoft.pitest") version "1.7.4"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = "com.github.caay2000"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation("org.jgrapht:jgrapht-core:1.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("io.arrow-kt:arrow-core:1.0.1")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    testImplementation("org.assertj:assertj-core:3.21.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

apply(plugin = "info.solidsoft.pitest.aggregator")
pitest {
    pitestVersion.set("1.7.4")
    junit5PluginVersion.set("0.15")
    targetClasses.add("com.github.caay2000.ttk.*")
    outputFormats.addAll("HTML", "XML")
    timestampedReports.set(false)
    exportLineCoverage.set(true)
//    excludedTestClasses.add("**.*IntegrationTest")
    avoidCallsTo.add("kotlin.jvm.internal")
    mutators.addAll("DEFAULTS")
}
