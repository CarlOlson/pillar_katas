
class Product
  attr_reader :price
  attr_reader :day
  
  def initialize price
    @price = price
    @day   = 0
    @red_pencil = false
    @last_price_change = 0
  end

  def day= value
    if value < day
      raise StandardError
    end
    
    @day = value
    
    if @day - @last_price_change >= 30
      @red_pencil = false
    end
  end
  
  def price= value
    if ((@price - value).to_f / @price >= 0.05 and
        @day - @last_price_change >= 30)
      @red_pencil = true
    end

    if (@price - value).to_f / @price >= 0.30
      # prevent suspicious price changes
      @red_pencil = false
    end
    
    @price = value
    @last_price_change = @day
  end

  def red_pencil?
    @red_pencil
  end
end
