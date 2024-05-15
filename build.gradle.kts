plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.akka.io/maven")
    }
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/com.typesafe.akka/akka-actor
    implementation("com.typesafe.akka:akka-actor_3:2.9.0-M2")

    implementation(platform("com.typesafe.akka:akka-bom_2.13:2.9.3"))

    implementation("com.typesafe.akka:akka-actor-typed_2.13")
    testImplementation("com.typesafe.akka:akka-actor-testkit-typed_2.13")
}

tasks.test {
    useJUnitPlatform()
}