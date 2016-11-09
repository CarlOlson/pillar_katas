
sealed trait Coin {
  val value: Int
  val mass: Double
  val diameter: Double
}

case object Penny extends Coin {
  val value = 1
  val mass = 2.500
  val diameter = 19.05
}

case object Nickle extends Coin {
  val value = 5
  val mass = 5.000
  val diameter = 21.21
}

case object Dime extends Coin {
  val value = 10
  val mass = 2.268
  val diameter = 17.91
}

case object Quarter extends Coin {
  val value = 25
  val mass = 5.670
  val diameter = 24.26
}

case class UnknownCoin(val mass: Double, val diameter: Double) extends Coin {
  val value = 0
}

object Coins {
  // grams
  val mass = Map(
    2.500 -> Penny,
    5.000 -> Nickle,
    2.268 -> Dime,
    5.670 -> Quarter
  );

  // millimeters
  val diameter = Map(
    19.05 -> Penny,
    21.21 -> Nickle,
    17.91 -> Dime,
    24.26 -> Quarter
  );
}
