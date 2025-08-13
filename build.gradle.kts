plugins {
	java
	id("org.springframework.boot") version "3.5.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.portafolio"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
		vendor = JvmVendorSpec.ADOPTIUM
		implementation = JvmImplementation.VENDOR_SPECIFIC
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
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("se.michaelthelin.spotify:spotify-web-api-java:9.3.0")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.asynchttpclient:async-http-client:3.0.2")
	implementation("com.squareup.okhttp:okhttp:2.7.5")
	implementation("org.jsoup:jsoup:1.21.1")
	implementation("com.jayway.jsonpath:json-path:2.9.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
