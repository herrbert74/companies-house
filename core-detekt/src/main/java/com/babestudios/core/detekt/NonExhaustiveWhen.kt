package com.babestudios.core.detekt

import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.Rule
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtWhenExpression

class NonExhaustiveWhen(config: Config = Config.empty) : Rule(config, DESCRIPTION) {

	override fun visitNamedFunction(function: KtNamedFunction) {
		super.visitNamedFunction(function)

		val whenExpressions =
			function.children.filterIsInstance<KtBlockExpression>()
				.flatMap { blockExpression -> blockExpression.children.asIterable() }
				.filterIsInstance<KtWhenExpression>()
		if (whenExpressions.isNotEmpty()) {
			report(
				Finding(
					Entity.from(function),
					MESSAGE
				)
			)
		}
	}
}

internal const val DESCRIPTION = "When should be used as expression"
internal const val MESSAGE = "When not used as expression"
