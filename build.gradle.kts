import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.radiation"
version = "0.0.1-SNAPSHOT"
description = "Radiation exporter service"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//API//
	implementation("org.apache.poi:poi:5.4.1")
	implementation("org.apache.poi:poi-ooxml:5.4.1")
	implementation("org.apache.pdfbox:pdfbox:3.0.5")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	useJUnitPlatform()

	jvmArgs = listOf(
		"-javaagent:${classpath.find { it.name.contains("mockito-core") }?.absolutePath}",
		"-javaagent:${classpath.find { it.name.contains("byte-buddy-agent") }?.absolutePath}"
	)

	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
	}
}


