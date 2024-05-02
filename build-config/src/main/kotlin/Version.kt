import org.gradle.api.Project

val major = 1
val minor = 0
val patch = 2

val isTest = false
val isSnapshot = false

@Suppress("UnusedReceiverParameter")
fun Project.generateVersion(): String = buildString {
    append("$major.$minor.$patch")

    if (isTest) {
        append("-test")
    }

    if (isSnapshot) {
        append("-SNAPSHOT")
    }
}
