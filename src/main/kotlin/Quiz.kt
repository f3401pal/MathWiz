package com.f3401pal.mathwiz

import io.ktor.application.log
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.lang.Math
import com.f3401pal.mathwiz.EquationQuizConfig

private val logger = LoggerFactory.getLogger("Quiz");

sealed class BaseQuiz {

    val integerOnly = true

    val presentation: String by lazy { generatePresentation() }

    protected abstract fun generatePresentation(): String
}

enum class Operator {
    Plus, Minus
}

data class EquationQuiz(
    private val numbers: FloatArray,
    private val operator: Operator
) : BaseQuiz() {

    private val numberFormat = if(integerOnly) DecimalFormat("#") else DecimalFormat("#.#")
    
    protected override fun generatePresentation(): String {
        assert(numbers.size == 2) {
            logger.error("invalid EquationQuiz: $numbers, $operator")
        }
        return StringBuilder().apply { 
            append(numberFormat.format(numbers[0]))
            when(operator) {
                Operator.Plus -> append(" + ")
                Operator.Minus -> append(" - ")
            }
            append(numberFormat.format(numbers[1]))
        }.append(" = ").toString()
    }

}

object QuizGenerator {

    fun generateEquationQuiz(
        config: EquationQuizConfig
    ) = (1..config.size).map {
        assert(config.max >= 0) {
            logger.error("invalid input, max=${config.max}")
        }
        assert(config.max > config.min) {
            logger.error("invalid input, max must be greater than min")
        }
        val op = config.operators.map { Operator.valueOf(it) }.pickRandomly()
        val delta = (config.max - config.min).randomInt()
        val num: FloatArray = when (op) {
            Operator.Plus -> {
                val sum = config.min + delta
                val first = config.min + delta.randomFloat()
                val second = sum - first + config.min.randomFloat()
                floatArrayOf(first, second)
            }
            Operator.Minus -> {
                val first = (config.min + delta).toFloat()
                val second = config.min.randomFloat() + delta.randomFloat()
                floatArrayOf(first, second)
            }
            else -> throw RuntimeException("no supported operation, $op")
        }
        EquationQuiz(
            num,
            op
        )
    }

}

private fun <T> List<T>.pickRandomly(): T = this[(size * Math.random()).toInt()]

private fun Int.randomFloat(): Float = (Math.random() * this).toFloat()

private fun Int.randomInt(): Int = (Math.random() * (this + 1)).toInt()
        
