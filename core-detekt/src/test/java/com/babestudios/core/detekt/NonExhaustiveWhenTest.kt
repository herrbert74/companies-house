package com.babestudios.core.detekt

import io.gitlab.arturbosch.detekt.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class NonExhaustiveWhenTest {
    @Test
    @DisplayName("non compliant when statement should warn")
    internal fun nonCompliantCodeShouldWarn() {
        val findings = NonExhaustiveWhen()
                .lint(WHEN_STATEMENT.trimIndent())

        assertThat(findings).hasSize(1)
        assertThat(findings[0].message).isEqualTo(MESSAGE)
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
