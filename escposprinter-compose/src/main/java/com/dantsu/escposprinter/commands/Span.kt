package com.dantsu.escposprinter.commands

sealed interface Span {

    /**
     * Get the length of [Span] relative to it's [fullSpanLength]
     * note, however that the result would be an [Int]
     * therefore the result will be rounded to the nearest zero value
     *
     * @param fullSpanLength
     * @param cut : whether or not the length is allowed to go beyond
     * fullSpanLength
     */
    fun length(fullSpanLength: Int, cut: Boolean = false): Int

    companion object {

        val FullSpanned = RatioSpan(multiplyBy = 1, divideBy = 1)
        val ZeroSpanned = RatioSpan(multiplyBy = 0, divideBy = 1)

        /**
         * Given list of type [T] with [spanAccessor]
         * returns the given span length for each item [T]
         *
         * @param list : the given list of type [T]
         * @param spanAccessor : property of [T] with it's span
         * @param fullSpanLength : length of the span
         *
         * @return pair of type [T] and it's span length
         * relative to [fullSpanLength]
         */
        fun <T> distributeLength(
            list: List<T>,
            spanAccessor: (T) -> Span,
            fullSpanLength: Int
        ): List<Pair<T, Int>> {

            // get the list of all FixSpan
            val fixed = list.filter {
                val span = spanAccessor.invoke(it)
                span is FixSpan
            }

            // find sum total of the length of the FixSpan
            val totalFixSpan = fixed.sumOf {
                val span = spanAccessor.invoke(it)
                span.length(fullSpanLength, cut = false)
            }.coerceAtMost(fullSpanLength)
            // coerceAtMost, would limit the length to fullSpanLength

            val result = mutableListOf<Pair<T, Int>>()

            var totalSpanLength = totalFixSpan
            var totalRatioSpan = ZeroSpanned

            // the length that would be divided into each RatioSpan
            val expectedRatioSpanLength = fullSpanLength - totalFixSpan

            // get the spanLength of each span item and pair it with
            // the item itself to be returned
            for (item in list) {
                when (val span = spanAccessor.invoke(item)) {
                    is FixSpan -> {
                        val spanLength = span.length
                        result.add(Pair(item, spanLength))
                        continue
                    }
                    is RatioSpan -> {
                        val spanLength = span.length(expectedRatioSpanLength)
                        result.add(Pair(item, spanLength))
                        totalSpanLength += spanLength
                        totalRatioSpan += span
                    }
                }
            }

            // if there is rounding remainder
            // distribute the rounding into the last item
            if (totalRatioSpan == FullSpanned && totalSpanLength < fullSpanLength) {

                // the resulting difference because of the rounding
                val roundingDiff = fullSpanLength - totalSpanLength

                val (item, spanLength) = result[list.lastIndex]

                // add the rounding diff to last spanned item
                val newSpanLength = spanLength + roundingDiff

                result[list.lastIndex] = Pair(item, newSpanLength)
            }

            return result
        }

    }
}

data class FixSpan(val length: Int) : Span {

    init {
        require(length > 0)
    }

    override fun length(fullSpanLength: Int, cut: Boolean): Int {
        if (cut && length > fullSpanLength) {
            return fullSpanLength
        }

        return length
    }

    operator fun plus(span: FixSpan): Span {
        return span.plus(this)
    }

}

class RatioSpan internal constructor(
    private val multiplyBy: Int,
    private val divideBy: Int
) : Span {

    operator fun plus(span: RatioSpan): RatioSpan {
        return RatioSpan(
            (multiplyBy * span.divideBy) + (span.multiplyBy * divideBy),
            divideBy * span.divideBy
        )
    }

    operator fun minus(span: RatioSpan): RatioSpan {
        return RatioSpan(
            (multiplyBy * span.divideBy) - (span.multiplyBy * divideBy),
            divideBy * span.divideBy
        )
    }

    operator fun div(number: Int): RatioSpan {
        if (number == 0) throw ArithmeticException("Division by 0")

        return RatioSpan(
            multiplyBy,
            divideBy * number,
        )
    }

    operator fun times(number: Int) = RatioSpan(
        multiplyBy * number,
        divideBy
    )

    override fun length(fullSpanLength: Int, cut: Boolean): Int {
        val spanLength = fullSpanLength * multiplyBy / divideBy

        if (cut && spanLength > fullSpanLength) {
            return fullSpanLength
        }

        if (spanLength < 0) {
            throw ArithmeticException("Negative span length")
        }

        return spanLength
    }

    override fun equals(other: Any?): Boolean {
        if (other !is RatioSpan) return false
        return multiplyBy * other.divideBy == other.multiplyBy * divideBy
    }

    override fun hashCode(): Int {
        var hash = 7
        if (multiplyBy == 0) {
            return 31 * hash
        }

        val div = multiplyBy / divideBy
        hash = 31 * hash + div.hashCode()
        val div2 = divideBy / multiplyBy
        hash = 31 * hash + div2.hashCode()
        return hash
    }

}

operator fun Int.times(span: RatioSpan): RatioSpan {
    return span * this
}