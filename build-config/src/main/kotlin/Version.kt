import org.gradle.api.Project

const val major = 1
const val minor = 2
const val patch = 1

const val isTest = false
const val isSnapshot = false

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
