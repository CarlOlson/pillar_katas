
import collection.mutable.Stack
import collection.mutable.Queue
import collection.mutable.ArrayBuffer

// NOTE vending machines start with 4 nickles
// NOTE vending machines start with one Cola, Chip, and Candy each
class VendingMachine {
  val EXACT_CHANGE_MESSAGE = "EXACT CHANGE ONLY"
  val INSERT_COIN_MESSAGE  = "INSERT COIN"
  val SOLD_OUT_MESSAGE     = "SOLD OUT"
  val THANK_YOU_MESSAGE    = "THANK YOU"
  val PRICE_FORMAT         = "$%d.%02d"
  val PRICE_CHECK_PREFIX   = "PRICE "

  val coinReturn : Stack[Coin]    = Stack()
  val dispensor  : Stack[Product] = Stack()

  private val insertedCoins: Stack[Coin]   = Stack()
  private val displayQueue : Queue[String] = Queue()
  private val bank      : ArrayBuffer[Coin]    = ArrayBuffer(Nickle, Nickle, Nickle, Nickle)
  private val inventory : ArrayBuffer[Product] = ArrayBuffer(Cola, Chips, Candy)

  def insertCoin(mass: Double, diameter: Double): Unit =
    processCoin(mass, diameter) match {
      case Nickle  =>
        insertedCoins.push(Nickle)
      case Dime    =>
        insertedCoins.push(Dime)
      case Quarter =>
        insertedCoins.push(Quarter)
      case coin =>
        coinReturn.push(coin)
    }

  def display: String =
    if (!displayQueue.isEmpty) {
      displayQueue.dequeue()
    } else if (sumCoins() > 0) {
      formatPrice(sumCoins())
    } else if (!canMakeChange()) {
      EXACT_CHANGE_MESSAGE
    } else {
      INSERT_COIN_MESSAGE
    }

  def select(product: Product): Unit = {
    val sum = sumCoins()

    if (!inventory.contains(product)) {
      displayQueue.enqueue(SOLD_OUT_MESSAGE)
    } else if (sum >= product.cost && canMakeChange(sum - product.cost)) {
      // accept coins
      bank ++= insertedCoins
      insertedCoins.clear()

      // make change
      val change = makeChange(sum - product.cost)
      for (coin <- change) {
        bank -= coin
        coinReturn.push(coin)
      }

      // dispense
      inventory -= product
      dispensor.push(product)

      // thank
      displayQueue.enqueue(THANK_YOU_MESSAGE)
    } else if (sum < product.cost) {
      displayQueue.enqueue(PRICE_CHECK_PREFIX + formatPrice(product.cost))
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
    PRICE_FORMAT.format(dollars, cents % 100)
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
