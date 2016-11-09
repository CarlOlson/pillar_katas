
sealed trait Product {
  val cost: Int
}

case object Cola extends Product {
  val cost = 100
}

case object Chips extends Product {
  val cost = 50
}

case object Candy extends Product {
  val cost = 65
}
