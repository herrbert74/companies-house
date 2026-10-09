package com.babestudios.core.detekt

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

class BaBeStudiosRuleSetProvider : RuleSetProvider {
    override val ruleSetId: RuleSetId = RuleSetId("babe")

    override fun instance(): RuleSet = RuleSet(
            ruleSetId,
            listOf(
                    { config -> NonExhaustiveWhen(config) },
                    { config -> FunctionNameLength(config) },
            )
    )
}
