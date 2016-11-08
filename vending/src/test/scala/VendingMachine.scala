
import org.scalatest._

// Acceptance Tests
class VendingMachineFeatureSpec extends FeatureSpec {
  info("As a vendor")
  info("I want a vending machine that accepts coins")
  info("So that I can collect money from the customer")

  feature("Accept Coins") {
    scenario("Customer inserts coins") (pending)
    scenario("Vendor collects coins") (pending)
  }

  info("As a vendor")
  info("I want customers to select products")
  info("So that I can give them an incentive to put money in the machine")

  feature("Select Product") {
    scenario("Customer selects a product") (pending)
  }

  info("As a vendor")
  info("I want customers to receive correct change")
  info("So that they will use the vending machine again")

  feature("Make Change") {
    scenario("Customer requires change")
  }

  info("As a customer")
  info("I want to have my money returned")
  info("So that I can change my mind about buying stuff from the vending machine")

  feature("Return Coins") {
    scenario("Customer asks for inserted money back") (pending)
    scenario("Customer asks money back without inserting change") (pending)
  }

  info("As a customer")
  info("I want to be told when the item I have selected is not available")
  info("So that I can select another item")

  feature("Sold Out") {
    scenario("Customer selects a sold out item") (pending)
    scenario("Customer changes selection from sold out item") (pending)
  }

  info("As a customer")
  info("I want to be told when exact change is required")
  info("So that I can determine if I can buy something with the money I have before inserting it")

  feature("Exact Change Only") {
    scenario("Machine requires exact change") (pending)
    scenario("Machine does not require exact change") (pending)
  }
}
