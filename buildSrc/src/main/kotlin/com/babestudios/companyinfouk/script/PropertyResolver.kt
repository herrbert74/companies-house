package com.babestudios.companyinfouk.script

import com.babestudios.companyinfouk.PluginGradleProperty
import org.gradle.api.Project

class PropertyResolver(private val project: Project) {

    fun getBooleanProp(gradleProperty: PluginGradleProperty): Boolean {
        return getBooleanProp(gradleProperty, gradleProperty.defaultValue as Boolean)
    }

    private fun getBooleanProp(gradleProperty: PluginGradleProperty, defaultValue: Boolean): Boolean {
        val value = getProp(gradleProperty, null)
        return if (value != null) {
            when (value) {
                "true" -> true
                "false" -> false
                else -> throw IllegalArgumentException("The ${gradleProperty.name} gradle property value " +
                    "[$value] is not a valid boolean")
            }
        } else {
            defaultValue
        }
    }

    private fun getProp(gradleProperty: PluginGradleProperty, defaultValue: Any?): Any? {
        return when {
            project.hasProperty(gradleProperty.name) -> project.property(gradleProperty.name)
            System.getenv().containsKey(gradleProperty.name) -> System.getenv(gradleProperty.name)
            else -> defaultValue
        }
    }
}
