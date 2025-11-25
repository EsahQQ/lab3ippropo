plugins {
    application
    jacoco
    checkstyle
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine") // ВОТ ЧТО МЫ ВЕРНУЛИ
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass.set("ru.university.lab3.App")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.10"
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
}
tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"
            excludes = listOf(
                "ru.university.lab3.App",
                "ru.university.lab3.service.ConfigLoader"
            )
            limit {
                counter = "INSTRUCTION"
                value = "COVEREDRATIO"
                minimum = "0.75".toBigDecimal()
            }
        }
    }
}
tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

checkstyle {
    toolVersion = "10.12.3"
    configFile = file("checkstyle.xml")
}