import com.tony.buildscript.addTestDependencies

dependencies {
    api(projects.tonyJwt)
    api(projects.tonyWeb)
    addTestDependencies()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
