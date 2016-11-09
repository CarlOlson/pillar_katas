
import org.scalatest._

// Acceptance Tests
class VendingMachineFeatureSpec extends FeatureSpec with GivenWhenThen {
  info("As a vendor")
  info("I want a vending machine that accepts coins")
  info("So that I can collect money from the customer")

  feature("Accept Coins") {
    scenario("Customer doesn't insert a coin") {
      Given("a vending machine")
      val vm = new VendingMachine()

      Then("it displays insert coin message")
      assert(vm.display === "INSERT COIN")
    }
    scenario("Customer inserts vaild coin") {
      Given("a vending machine")
      val vm = new VendingMachine()

      When("a dime is inserted")
      vm.insertCoin(Dime.mass, Dime.diameter)

      Then("it displays $0.10")
      assert(vm.display === "$0.10")
    }
    scenario("Customer inserts invalid coin") {
      Given("a vending machine")
      val vm = new VendingMachine()

      When("a penny is inserted")
      vm.insertCoin(Penny.mass, Penny.diameter)

      Then("it is in the coin return")
      assert(vm.coinReturn.length === 1)
    }
    scenario("Vendor collects coins") {
      Given("a vending machine with coins")
      val vm = new VendingMachine()
      for( x <- (1 to 4) ) {
        vm.insertCoin(Quarter.mass, Quarter.diameter)
      }
      // TODO purchase something

      When("a vender collects coins")
      (pending)

      Then("the coins are removed")
    }
  }

  info("As a vendor")
  info("I want customers to select products")
  info("So that I can give them an incentive to put money in the machine")

  feature("Select Product") {
    scenario("Customer purchases a product") {
      Given("a customer inserts enough coins")
      val vm = new VendingMachine()
      for( x <- (1 to 4) ) {
        vm.insertCoin(Quarter.mass, Quarter.diameter)
      }
      
      When("an item is select")
      vm.select(Cola)

      Then("it is dispensed and the customer is thanked")
      assert(vm.dispensor.contains(Cola))
      assert(vm.display === "THANK YOU")

      Then("the display reverts to its insert coin message")
      assert(vm.display === "INSERT COIN")
    }
  }

  info("As a vendor")
  info("I want customers to receive correct change")
  info("So that they will use the vending machine again")

  feature("Make Change") {
    scenario("Customer requires change") (pending)
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
  info("So that I can determine if I can buy something " +
    "with the money I have before inserting it")

  feature("Exact Change Only") {
    scenario("Machine requires exact change") (pending)
    scenario("Machine does not require exact change") (pending)
  }
}
