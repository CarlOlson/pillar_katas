
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
      vm.select(Cola)

      When("a vender collects coins")
      val coins = vm.emptyBank()

      Then("the coins are removed")
      assert(coins.map(_.value).sum === 100)
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

      When("the product is taken")
      val received = vm.dispensor.pop()

      Then("it matches selection")
      assert(received === Cola)
    }

    scenario("Customer can't purchase a product") {
      Given("a customer doesn't insert enough coins")
      val vm = new VendingMachine()
      vm.insertCoin(Quarter.mass, Quarter.diameter)

      When("an item is selected")
      vm.select(Cola)

      Then("it displays the price")
      assert(vm.display === "PRICE $1.00")

      Then("the display reverts to amount inserted")
      assert(vm.display === "$0.25")
    }
  }

  info("As a vendor")
  info("I want customers to receive correct change")
  info("So that they will use the vending machine again")

  feature("Make Change") {
    scenario("Customer requires change") {
      Given("a customer inserts too many coins")
      val vm = new VendingMachine()
      for( x <- (1 to 4) ) {
        vm.insertCoin(Quarter.mass, Quarter.diameter)
      }
      vm.insertCoin(Dime.mass, Dime.diameter)
      vm.insertCoin(Nickle.mass, Nickle.diameter)
      vm.insertCoin(Penny.mass, Penny.diameter)

      When("an item is purchased")
      vm.select(Candy) // 65 cents

      Then("correct change is receieved")
      assert(vm.coinReturn.length >= 3)
      assert(vm.coinReturn.map(_.value).sum === 51)
    }
  }

  info("As a customer")
  info("I want to have my money returned")
  info("So that I can change my mind about buying stuff from the vending machine")

  feature("Return Coins") {
    scenario("Customer asks for inserted money back") {
      Given("a customer inserts money")
      val vm = new VendingMachine()
      vm.insertCoin(Quarter.mass, Quarter.diameter)
      vm.insertCoin(Dime.mass, Dime.diameter)
      vm.insertCoin(Nickle.mass, Nickle.diameter)

      When("asks for money back")
      vm.returnCoins()

      Then("correct change is given")
      assert(vm.coinReturn.length === 3)
      assert(vm.coinReturn.map(_.value).sum === 40)
    }
    scenario("Customer asks money back without inserting change") {
      Given("a vending machine")
      val vm = new VendingMachine()

      When("ask for coin return")
      vm.returnCoins()

      Then("no coins are returned")
      assert(vm.coinReturn.isEmpty)
    }
  }

  info("As a customer")
  info("I want to be told when the item I have selected is not available")
  info("So that I can select another item")

  feature("Sold Out") {
    scenario("Customer selects a sold out item") {
      Given("a customer inserts enough money")
      val vm = new VendingMachine()
      for (i <- 1 to 40) vm.insertCoin(Quarter.mass, Quarter.diameter)

      When("a sold out item is selected")
      vm.select(RareCandy)

      Then("it displays sold out")
      assert(vm.display === "SOLD OUT")

      Then("it displays money inserted")
      assert(vm.display === "$10.00")
    }
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
