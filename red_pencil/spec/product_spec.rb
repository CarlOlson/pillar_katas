# coding: utf-8

require "product"

describe Product do
  before do
    @product = Product.new 100
    @product.day = 30
  end

  describe "#price=" do
    it "should update the current price" do
      @product.price = 110
      expect(@product.price).to eq 110
    end

    it "should trigger a red pencil promotion" do
      @product.price = 90
      expect(@product.red_pencil?).to eq be_true
    end
  end
  
  describe "red pencil promotion" do
    # A red pencil promotion lasts 30 days as the maximum length.
    it "should last 30 days at most" do
      @product.price = 90
      expect(@product.red_pencil?).to eq be_true

      @product.day += 30
      expect(@product.red_pencil?).to eq be_false
    end

    # A red pencil promotion starts due to a price reduction. The price
    # has to be reduced by at least 5% but at most bei 30% and the
    # previous price had to be stable for at least 30 days.
    it "should start due to a price reduction" do
      @product.price = 90
      expect(@product.red_pencil?).to eq be_true
    end
    it "should start after a >=5% reduction" do
      @product.price = 96
      expect(@product.red_pencil?).to eq be_false

      @product.price = 95
      expect(@product.red_pencil?).to eq be_true
    end
    it "should not start after a >30% reduction" do
      @product.price = 69
      expect(@product.red_pencil?).to eq be_false
    end
    it "should only start after the previous price was stable for >=30 days" do
      @product.price = 110
      @product.day += 29
      @product.price = 100
      expect(@product.red_pencil?).to eq be_false

      @product.day += 30
      @product.price = 90
      expect(@product.red_pencil?).to be_true
    end

    # If the price is further reduced during the red pencil promotion the
    # promotion will not be prolonged by that reduction.
    it "should not be extended upon additional reductions" do
      @product.price = 90
      @product.day += 15
      expect(@product.red_pencil?).to be_true
      
      @product.price = 80
      @product.day += 15
      expect(@product.red_pencil?).to be_false
    end

    # If the price is increased during the red pencil promotion the
    # promotion will be ended immediately.
    it "should end if the price is increased" do
      @product.price = 90
      expect(@product.red_pencil?).to be_true
      
      @product.price += 10
      expect(@product.red_pencil?).to be_false
    end

    # If the price if reduced during the red pencil promotion so that the
    # overall reduction is more than 30% with regard to the original
    # price, the promotion is ended immediately.
    it "should end after additional reductions equal >30% of the original price" do
      @product.price = 90
      expect(@product.red_pencil?).to be_true

      @product.day += 1
      @product.price = 69
      expect(@product.red_pencil?).to be_false
    end

    # After a red pencil promotion is ended additional red pencil
    # promotions may follow - as long as the start condition is valid: the
    # price was stable for 30 days and these 30 days don’t intersect with
    # a previous red pencil promotion.
    it "should not happen again until 30 days after the last promotion" do
      @product.price = 90
      expect(@product.red_pencil?).to be_true

      @product.day += 1
      @product.price += 10
      expect(@product.red_pencil?).to be_false

      @product.day += 29
      @product.price = 90
      expect(@product.red_pencil?).to be_false

      @product.day += 30
      @product.price = 90
      expect(@product.red_pencil?).to be_true
    end
  end
end
