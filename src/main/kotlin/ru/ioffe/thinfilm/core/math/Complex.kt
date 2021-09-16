package ru.ioffe.thinfilm.core.math

data class Complex(val re: Double, val im: Double) : Number() {

    constructor(re: Double) : this(re, 0.0)

    operator fun plus(other: Complex): Complex = Complex(re + other.re, im + other.im)
    operator fun minus(other: Complex): Complex = Complex(re - other.re, im - other.im)
    operator fun times(other: Complex): Complex =
        Complex(re * other.re - im * other.im, re * other.im + im * other.re)

    operator fun div(other: Complex): Complex =
        Complex(
            (re * other.re + im * other.im) / (other.im * other.im + other.re * other.re),
            (other.re * im - re * other.im) / (other.im * other.im + other.re * other.re)
        )

    override fun toByte(): Byte {
        TODO("Not yet implemented")
    }

    override fun toChar(): Char {
        TODO("Not yet implemented")
    }

    override fun toDouble(): Double {
        TODO("Not yet implemented")
    }

    override fun toFloat(): Float {
        TODO("Not yet implemented")
    }

    override fun toInt(): Int {
        TODO("Not yet implemented")
    }

    override fun toLong(): Long {
        TODO("Not yet implemented")
    }

    override fun toShort(): Short {
        TODO("Not yet implemented")
    }
}
