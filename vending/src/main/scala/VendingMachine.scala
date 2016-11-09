
import collection.mutable.Stack
import collection.mutable.Queue
import collection.mutable.ArrayBuffer

// NOTE vending machines start with 4 nickles
class VendingMachine {
  val insertedCoins: Stack[Coin] = Stack()

  val coinReturn: Stack[Coin] = Stack()

  val dispensor: Stack[Product] = Stack()

  val displayQueue: Queue[String] = Queue()

  val bank: ArrayBuffer[Coin] = ArrayBuffer(Nickle, Nickle, Nickle, Nickle)

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
    } else if (!canMakeChange()) {
      "EXACT CHANGE ONLY"
    } else {
      "INSERT COIN"
    }
  }

  def select(product: Product): Unit = {
    val sum = sumCoins()

    if (!inventory.contains(product)) {
      displayQueue.enqueue("SOLD OUT")
    } else if (sum >= product.cost && canMakeChange(sum - product.cost)) {

      bank ++= insertedCoins
      insertedCoins.clear()

      val change = makeChange(sum - product.cost)
      coinReturn.pushAll(change)
      for(coin <- change)
        bank -= coin

      displayQueue.enqueue("THANK YOU")

      // TODO remove product from inventory
      dispensor.push(product)
    } else if (sum < product.cost) {
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

  def addToBank(coins: Seq[Coin]): Unit =
    bank ++= coins

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

  private def makeChange(cents: Int): List[Coin] =
    makeChangeOption(cents) match {
      case Some(coins) => coins
      case None        => throw new Exception("Unreachable make change error")
    }

  private def makeChangeOption(cents: Int): Option[List[Coin]] = {
    def rec(needed: Int, coins: List[Coin], acc: List[Coin]): Option[List[Coin]] = {
      if (needed == 0) {
        Some(acc)
      } else if (needed < 0 || coins.isEmpty) {
        None
      } else if (needed - coins.head.value >= 0) {
        rec(needed - coins.head.value, coins.tail, coins.head :: acc)
      } else {
        rec(needed, coins.tail, acc)
      }
    }
    // sort coins by descending order
    val coins = (bank.toList ++ insertedCoins).sortBy(_.value).reverse
    rec(cents, coins, List.empty)
  }

  private def canMakeChange(): Boolean =
    // bad algorithm, but easy to understand
    // TODO base on actual items in inventory
    List(5, 10, 15, 20).forall {
      makeChangeOption(_) != None
    }

  private def canMakeChange(cents: Int): Boolean =
    makeChangeOption(cents) != None
}
