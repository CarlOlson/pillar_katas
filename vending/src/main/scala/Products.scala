
sealed trait Product {
  val cost: Int
}

case object Cola {
  val cost = 100
}

case object Chips {
  val cost = 50
}

case object Candy {
  val cost = 65
}
