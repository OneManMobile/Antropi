import com.soywiz.korge.gradle.*

plugins {
	alias(libs.plugins.korge)
}
repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

korge {
	id = "com.sample.demo"

// To enable all targets at once

	//targetAll()

// To enable targets based on properties/environment variables
	//targetDefault()

// To selectively enable targets
	
	targetJvm()
	//targetJs()
	//targetDesktop()
	//targetIos()
	//targetAndroidIndirect() // targetAndroidDirect()

	//serializationJson()
	//targetAndroidDirect()

    addDependency("jvmMainImplementation", "com.soywiz.korlibs.klock:klock-jvm:1.6.1")
}


dependencies {
    add("commonMainApi", project(":deps"))
    //add("commonMainApi", project(":korge-dragonbones"))
}

