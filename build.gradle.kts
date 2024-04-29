plugins {
	java
	id("org.springframework.boot") version "3.2.5"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.swop.api.assignment"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("io.netty:netty-resolver-dns-native-macos:4.1.84.Final") {
		artifact {
			classifier = "osx-aarch_64"
		}
	}
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok:1.18.22")
	compileOnly("org.projectlombok:lombok:1.18.22")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.1")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
	implementation("com.google.guava:guava:33.0.0-jre")
	implementation("com.influxdb:influxdb-client-java:7.0.0")
	implementation("io.micrometer:micrometer-registry-influx:1.12.2")
	implementation("io.micrometer:micrometer-core:1.12.2")

	//api docs
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.3.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

springBoot {
	buildInfo()
}

