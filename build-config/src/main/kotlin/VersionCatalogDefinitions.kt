import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

internal val VersionCatalog.composeCompiler: String
    get() = findVersionOrThrow("compose-compiler")

internal val VersionCatalog.composeFoundation: Provider<MinimalExternalModuleDependency>
    get() = findLibraryOrThrow("androidX-compose-foundation")