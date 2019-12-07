package com.babestudios.core.detekt

import io.gitlab.arturbosch.detekt.api.*
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtWhenExpression

class NonExhaustiveWhen(config: Config = Config.empty): Rule(config) {
    override val issue = Issue(
            javaClass.simpleName,
            Severity.Defect,
            DESCRIPTION,
            Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val whenExpressions =
                function.children.filterIsInstance<KtBlockExpression>()
                        .flatMap { blockExpression -> blockExpression.children.asIterable() }
                        .filterIsInstance<KtWhenExpression>()
        if (whenExpressions.isNotEmpty()) {
            report(
                    CodeSmell(
                            issue, Entity.from(function), MESSAGE
                    )
            )
        }
    }
}

internal const val DESCRIPTION = "When should be used as expression"
internal const val MESSAGE = "When not used as expression"
