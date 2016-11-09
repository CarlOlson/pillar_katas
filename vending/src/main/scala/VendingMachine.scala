
import collection.mutable.Stack

class VendingMachine {
  val insertedCoins: Stack[Coin] = Stack()

  val coinReturn: Stack[Coin] = Stack()

  val dispensor: Stack[Product] = Stack()

  val unthanked: Boolean = false

  def insertCoin(mass: Double, diameter: Double): Unit = {
    val coin = processCoin(mass, diameter)
    coin match {
      case Nickle  =>
        insertedCoins.push(coin)
      case Dime    =>
        insertedCoins.push(coin)
      case Quarter =>
        insertedCoins.push(coin)
      case _ =>
        coinReturn.push(coin)
    }
  }

  def display: String = {
    if (sumCoins() > 0) {
      val dollars = math.floor(sumCoins() / 100.0).toInt
      val cents = sumCoins() % 100
      "$%d.%02d".format(dollars, cents)
    } else if (!dispensor.isEmpty) {
      "THANK YOU"
    } else {
      "INSERT COIN"
    }
  }

  def select(product: Product): Unit = {
    val sum = sumCoins()
    if (sum >= product.cost) {
      val change = sum - product.cost
      insertedCoins.clear()

      dispensor.push(product)
    } else {
    }
  }

  private def sumCoins(): Int =
    insertedCoins.map{ (coin) => coin.value }.sum

  private def processCoin(mass: Double, diameter: Double): Coin = {
    val coin = Coins.mass(mass)
    if (coin == Coins.diameter(diameter)) coin
    else UnknownCoin(mass, diameter)
  }
}
