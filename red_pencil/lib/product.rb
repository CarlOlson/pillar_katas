
class Product
  attr_reader :price
  attr_reader :day
  
  def initialize price
    @price = price
    @day   = 0
    @last_price_change = 0
    
    @red_pencil = false
    @red_pencil_start = -30
    @before_promotion_price = price
  end

  def day= value
    if value < day
      raise StandardError
    end
    
    @day = value
    
    if @day - @red_pencil_start >= 30
      self.red_pencil= false
    end
  end
  
  def price= value
    if ((@price - value).to_f / @price >= 0.05 and
        @day - @last_price_change >= 30)
      self.red_pencil= true
    end

    # prevent suspicious price changes
    if (@before_promotion_price - value).to_f / @price >= 0.30
      self.red_pencil= false
    end

    # end promotion on price increase
    if value > @price
      self.red_pencil= false
    end
    
    if @price != value
      @last_price_change = @day
    end
    
    @price = value
  end

  def red_pencil?
    @red_pencil
  end

  private
  def red_pencil= value
    if not @red_pencil and value
      @red_pencil_start = @day
      # NOTE should be called before price change
      @before_promotion_price = @price
    end
    @red_pencil = value
  end
end
