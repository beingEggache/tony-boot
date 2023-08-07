import com.tony.buildscript.Deps

dependencies {
    api(projects.tonyFeign)
    api(Deps.Other.xstream)

    implementation(Deps.Other.commonsCodec)
    implementation(Deps.SpringBoot.springBoot)

    testImplementation(projects.tonyRedis)
}
