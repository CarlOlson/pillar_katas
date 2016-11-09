
class VendingMachine {
  val insertedCoins: collection.mutable.Stack[Coin] =
    collection.mutable.Stack()

  def insertCoin(mass: Double, diameter: Double): Unit = {
    val coin = processCoin(mass, diameter)
    insertedCoins.push(coin)
  }

  def coinReturn(): Seq[Coin] = ???
  
  def display: String = {
    if (sumCoins() > 0) {
      val dollars = math.floor(sumCoins() / 100.0).toInt
      val cents = sumCoins() % 100
      "$%d.%02d".format(dollars, cents)
    } else {
      "INSERT COIN"
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