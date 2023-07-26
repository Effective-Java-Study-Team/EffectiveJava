package ch2.item1

class CarKotlin(
    val price : Int,
    val name : String,
    val brand : String,
    val leftGas : Int
) {

    override fun toString(): String {
        return "price = ${price}, name = ${name}, brand = ${brand}, leftGas = ${leftGas}"
    }
}