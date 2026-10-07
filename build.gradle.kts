plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    mavenCentral()
}

javafx {
    version = "21.0.5"
    modules("javafx.controls")
}

application {
    mainClass.set("org.example.CustomerManagerApp")
}