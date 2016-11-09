
import collection.mutable.Stack
import collection.mutable.Queue

class VendingMachine {
  val insertedCoins: Stack[Coin] = Stack()

  val coinReturn: Stack[Coin] = Stack()

  val dispensor: Stack[Product] = Stack()

  val displayQueue: Queue[String] = Queue()

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
    if (!displayQueue.isEmpty) {
      displayQueue.dequeue()
    } else if (sumCoins() > 0) {
      formatPrice(sumCoins())
    } else {
      "INSERT COIN"
    }
  }

  def select(product: Product): Unit = {
    val sum = sumCoins()
    if (sum >= product.cost) {
      val change = sum - product.cost
      insertedCoins.clear()

      displayQueue.enqueue("THANK YOU")
      dispensor.push(product)
    } else {
      displayQueue.enqueue("PRICE " + formatPrice(product.cost))
    }
  }

  private def sumCoins(): Int =
    insertedCoins.map{ (coin) => coin.value }.sum

  private def processCoin(mass: Double, diameter: Double): Coin = {
    val coin = Coins.mass(mass)
    if (coin == Coins.diameter(diameter)) coin
    else UnknownCoin(mass, diameter)
  }

  private def formatPrice(cents: Int): String = {
    val dollars = math.floor(cents / 100.0).toInt
    "$%d.%02d".format(dollars, cents % 100)
  }
}
