package com.babestudios.core.detekt

import io.gitlab.arturbosch.detekt.test.lint
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class NonExhaustiveWhenTest {
    @Test
    @DisplayName("non compliant when statement should warn")
    internal fun nonCompliantCodeShouldWarn() {
        val findings = NonExhaustiveWhen()
                .lint(WHEN_STATEMENT.trimIndent())

        findings.size shouldBe 1
        findings[0].message shouldBe MESSAGE
    }
}

const val WHEN_STATEMENT = """
        sealed class S {
          object A: S()
          object B: S()
        }
        
        class WhenTester {
          fun checkS(state: S) {
            when (state) {
              S.A -> println("a")
            }
          }
        }
        """

const val COMPLIANT_WHEN_DOT = """
        sealed class S {
          object A: S()
          object B: S()
        }
        
        class WhenTester {
          fun checkS(state: S) {
            when (state) {
              S.A -> println("a")
              S.B -> println("b")
            }.exhaustive
          }
        }
        """

const val COMPLIANT_WHEN_PROPERTY = """
        sealed class S {
          object A: S()
          object B: S()
        }
        
        class WhenTester {
          fun checkS(state: S) {
            val r = when (state) {
              S.A -> println("a")
              S.B -> println("b")
            }
          }
        }
        
        """

const val COMPLIANT_WHEN_RETURN = """
        sealed class S {
          object A: S()
          object B: S()
        }
        
        class WhenTester {
          fun checkS(state: S) {
            return when (state) {
              S.A -> println("a")
              S.B -> println("b")
            }
          }
        }
  
"""
