package com.babestudios.core.detekt

import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import org.jetbrains.kotlin.psi.KtNamedFunction

private const val MAX_LENGTH = 10

class FunctionNameLength(config: Config = Config.empty) : Rule(
	config,
	"Function names should not be longer than the allowed maximum."
) {

	override fun visitNamedFunction(function: KtNamedFunction) {
		function.name?.let {
			if (it.length > MAX_LENGTH) {
				report(
					Finding(
						Entity.from(function),
						"Function name ${function.name} is longer than allowed $MAX_LENGTH"
					)
				)
			}
		}
	}
}
