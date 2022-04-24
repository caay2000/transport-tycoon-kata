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

application {
    mainClass.set("com.github.caay2000.ttk.AppKt")
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":app"))
}

tasks.withType<Wrapper> {
    gradleVersion = "7.4.2"
}

dependencies {
    testImplementation(kotlin("test"))
}

apply(plugin = "info.solidsoft.pitest.aggregator")
pitest {
    pitestVersion.set("1.7.5")
    junit5PluginVersion.set("0.15")
    targetClasses.add("com.github.caay2000.ttk.*")
    outputFormats.addAll("XML")
    timestampedReports.set(false)
    exportLineCoverage.set(true)
    //    excludedTestClasses.add("**.*IntegrationTest")
    avoidCallsTo.add("kotlin.jvm.internal")
    mutators.addAll("DEFAULTS")
    detectInlinedCode.set(true)
    threads.set(4)
    failWhenNoMutations.set(false)
}
