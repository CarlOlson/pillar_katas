
import collection.mutable.Stack
import collection.mutable.Queue

class VendingMachine {
  val insertedCoins: Stack[Coin] = Stack()

  val coinReturn: Stack[Coin] = Stack()

  val dispensor: Stack[Product] = Stack()

  val displayQueue: Queue[String] = Queue()

  val bank: Stack[Coin] = Stack()

  val inventory: Array[Product] = Array(Cola, Chips, Candy)

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
    if (!inventory.contains(product)) {
      displayQueue.enqueue("SOLD OUT")
    } else if (sum >= product.cost) {
      val change = makeChange(sum - product.cost)
      insertedCoins.clear()

      // TODO only return from coins in machine
      coinReturn.pushAll(change)

      // TODO only add coins given
      bank.pushAll(makeChange(product.cost))

      displayQueue.enqueue("THANK YOU")

      // TODO remove product from inventory
      dispensor.push(product)
    } else {
      displayQueue.enqueue("PRICE " + formatPrice(product.cost))
    }
  }

  def returnCoins(): Unit = {
    coinReturn.pushAll(insertedCoins)
    insertedCoins.clear()
  }

  def emptyBank(): Seq[Coin] = {
    val coins = bank.toList
    bank.clear()
    coins
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

  private def makeChange(cents: Int): List[Coin] = {
    def rec(needed: Int, coins: List[Coin]): List[Coin] = {
      if (needed < 0 || coins.isEmpty) {
        throw new Exception("Unreachable making change exception")
      } else if (needed == 0) {
        List.empty
      } else if (needed - coins.head.value >= 0) {
        coins.head :: rec(needed - coins.head.value, coins)
      } else {
        rec(needed, coins.tail)
      }
    }
    rec(cents, List(Quarter, Dime, Nickle))
  }
}
