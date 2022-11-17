package com.babestudios.companyinfouk.design

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

val CompaniesHouseLightColorScheme = lightColorScheme(
	primary = Color(LIGHT_PRIMARY),
	onPrimary = Color(LIGHT_ON_PRIMARY),
	primaryContainer = Color(LIGHT_PRIMARY_CONTAINER),
	onPrimaryContainer = Color(LIGHT_ON_PRIMARY_CONTAINER),
	secondary = Color(LIGHT_SECONDARY),
	onSecondary = Color(LIGHT_ON_SECONDARY),
	secondaryContainer = Color(LIGHT_SECONDARY_CONTAINER),
	onSecondaryContainer = Color(LIGHT_ON_SECONDARY_CONTAINER),
	tertiary = Color(LIGHT_TERTIARY),
	onTertiary = Color(LIGHT_ON_TERTIARY),
	tertiaryContainer = Color(LIGHT_TERTIARY_CONTAINER),
	onTertiaryContainer = Color(LIGHT_ON_TERTIARY_CONTAINER),
	error = Color(LIGHT_ERROR),
	errorContainer = Color(LIGHT_ERROR_CONTAINER),
	onError = Color(LIGHT_ON_ERROR),
	onErrorContainer = Color(LIGHT_ON_ERROR_CONTAINER),
	background = Color(LIGHT_BACKGROUND),
	onBackground = Color(LIGHT_ON_BACKGROUND),
	surface = Color(LIGHT_SURFACE),
	onSurface = Color(LIGHT_ON_SURFACE),
	surfaceVariant = Color(LIGHT_SURFACE_VARIANT),
	onSurfaceVariant = Color(LIGHT_ON_SURFACE_VARIANT),
	outline = Color(LIGHT_OUTLINE),
	inverseOnSurface = Color(LIGHT_INVERSE_ON_SURFACE),
	inverseSurface = Color(LIGHT_INVERSE_SURFACE),
)
private val CompaniesHouseDarkColorScheme = darkColorScheme(

	primary = Color(DARK_PRIMARY),
	onPrimary = Color(DARK_ON_PRIMARY),
	primaryContainer = Color(DARK_PRIMARY_CONTAINER),
	onPrimaryContainer = Color(DARK_ON_PRIMARY_CONTAINER),
	secondary = Color(DARK_SECONDARY),
	onSecondary = Color(DARK_ON_SECONDARY),
	secondaryContainer = Color(DARK_SECONDARY_CONTAINER),
	onSecondaryContainer = Color(DARK_ON_SECONDARY_CONTAINER),
	tertiary = Color(DARK_TERTIARY),
	onTertiary = Color(DARK_ON_TERTIARY),
	tertiaryContainer = Color(DARK_TERTIARY_CONTAINER),
	onTertiaryContainer = Color(DARK_ON_TERTIARY_CONTAINER),
	error = Color(DARK_ERROR),
	errorContainer = Color(DARK_ERROR_CONTAINER),
	onError = Color(DARK_ON_ERROR),
	onErrorContainer = Color(DARK_ON_ERROR_CONTAINER),
	background = Color(DARK_BACKGROUND),
	onBackground = Color(DARK_ON_BACKGROUND),
	surface = Color(DARK_SURFACE),
	onSurface = Color(DARK_ON_SURFACE),
	surfaceVariant = Color(DARK_SURFACE_VARIANT),
	onSurfaceVariant = Color(DARK_ON_SURFACE_VARIANT),
	outline = Color(DARK_OUTLINE),
	inverseOnSurface = Color(DARK_INVERSE_ON_SURFACE),
	inverseSurface = Color(DARK_INVERSE_SURFACE)
)

@Composable
fun CompaniesHouseTheme(
	isDarkTheme: Boolean = isSystemInDarkTheme(),
	isDynamicColor: Boolean = false,
	content: @Composable () -> Unit
) {

	val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

	val colorScheme = when {
		dynamicColor && isDarkTheme -> dynamicDarkColorScheme(LocalContext.current)
		dynamicColor && !isDarkTheme -> dynamicLightColorScheme(LocalContext.current)
		isDarkTheme -> CompaniesHouseDarkColorScheme
		else -> CompaniesHouseLightColorScheme
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = CompaniesHouseTypography,
		content = content
	)

}
