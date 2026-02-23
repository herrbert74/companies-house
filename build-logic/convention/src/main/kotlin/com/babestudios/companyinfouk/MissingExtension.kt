package com.babestudios.companyinfouk

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Hack because it is missing inside the gradle dependencies
fun Project.androidLibrary(block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) {
	configure<KotlinMultiplatformExtension> {
		androidLibrary(block)
	}
}

fun KotlinMultiplatformExtension.androidLibrary(block: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) {
	(this as ExtensionAware).extensions.configure("androidLibrary", block)
}
