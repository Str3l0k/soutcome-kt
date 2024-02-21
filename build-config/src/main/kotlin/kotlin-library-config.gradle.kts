plugins {
    `java-library`
    id("kotlin")
    id("kotlin-lang-config")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

task("testUnitTest") {
    dependsOn("test")
}
