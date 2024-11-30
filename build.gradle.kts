import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
subprojects {
    apply(plugin = rootProject.libs.plugins.detekt.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.android.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.compose.get().pluginId)
    apply(plugin = rootProject.libs.plugins.kotlin.serialization.get().pluginId)
    apply(plugin = rootProject.libs.plugins.dependency.analysis.get().pluginId)
    configureDetekt()
    dependencies {
        // detekt
        "detektPlugins"(rootProject.libs.detekt.formatting)
        "detektPlugins"(rootProject.libs.detekt.rules.compose)
    }
}

fun Project.configureDetekt() {
    extensions.configure<DetektExtension> {
        buildUponDefaultConfig = true
        parallel = true
        allRules = false
        config.setFrom("$rootDir/config/detekt.yaml")
        baseline = file("$rootDir/config/baseline.xml")
    }

    tasks.withType<Detekt> {
        jvmTarget = "1.8"
        reports {
            html.required.set(true)
        }
    }

    tasks.withType<DetektCreateBaselineTask> {
        jvmTarget = "1.8"
    }
}
