package com.babestudios.core.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class BaBeStudiosRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "babe"

    override fun instance(config: Config): RuleSet = RuleSet(
            ruleSetId,
            listOf(
                    NonExhaustiveWhen(config),
                    FunctionNameLength(config)
            )
    )
}
