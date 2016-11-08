
sealed trait Coin {
  val value: Int

  val coinMass = Map(
    2.500 -> Penny,
    5.000 -> Nickle,
    2.268 -> Dime,
    5.670 -> Quarter
  );

  val coinDiameter = Map(
    19.05 -> Penny,
    21.21 -> Nickle,
    17.91 -> Dime,
    24.26 -> Quarter
  );
}
case object Penny extends Coin {
  val value = 1
}
case object Nickle extends Coin {
  val value = 5
}
case object Dime extends Coin {
  val value = 10
}
case object Quarter extends Coin {
  val value = 25
}
