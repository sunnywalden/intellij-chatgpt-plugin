plugins {
  id("org.jetbrains.intellij.platform") version "2.2.0"
}

group = "com.sunnywalden"
version = "1.0"

repositories {
  mavenCentral()

  intellijPlatform {
    localPlatformArtifacts()
  }

  maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

dependencies {
  intellijPlatform {
    bundledPlugin("org.jetbrains.plugins.go")
    local("""C:\Program Files\JetBrains\GoLand 2024.3""")

  }

  testImplementation("com.jetbrains.intellij.go:go-test-framework:GOLAND-212-SNAPSHOT") {
          exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
          exclude("org.jetbrains.kotlin", "kotlin-reflect")
          exclude("com.jetbrains.rd", "rd-core")
          exclude("com.jetbrains.rd", "rd-swing")
          exclude("com.jetbrains.rd", "rd-framework")
      }
}