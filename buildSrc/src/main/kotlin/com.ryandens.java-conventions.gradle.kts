plugins {
  java
  id("com.diffplug.spotless")
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
  useJUnitPlatform()
}