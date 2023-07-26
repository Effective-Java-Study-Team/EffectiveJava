package ch2.item1


fun main() {
    val hyundaiCar = CarKotlin(
        price = 2000,
        name = "부릉이",
        brand = "현대",
        leftGas = 200
    )

    val benzCar = CarKotlin(
        price = 5000,
        name = "프리미엄 부릉이",
        brand = "벤츠",
        leftGas = 400
    )

    println(hyundaiCar)
    println(benzCar)
}