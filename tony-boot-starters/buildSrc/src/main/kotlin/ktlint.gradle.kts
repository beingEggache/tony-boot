/**
 *
 * @author tangli
 * @since 2020-12-18 10:53
 */
val ktlint by configurations.creating
dependencies {
    ktlint("com.pinterest:ktlint:0.40.0")
    // ktlint(project(":custom-ktlint-ruleset")) // in case of custom ruleset
}
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    group = "verification"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    group = "verification"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args = listOf("-F", "src/**/*.kt")
}

tasks.named("compileKotlin"){
    dependsOn("ktlintCheck")
}
