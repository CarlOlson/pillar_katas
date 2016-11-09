
class Product
  attr_reader :price
  attr_reader :day

  ONE_MONTH = 30
  MIN_CHANGE = 0.05
  MAX_CHANGE = 0.30
  
  def initialize price
    @price = price
    @day   = 0
    @last_price_change = 0
    
    @red_pencil = false
    @last_red_pencil_start = 0
    @before_promotion_price = price
  end

  def day= day
    if day < @day
      raise StandardError, "Cannot reverse time"
    end
    
    if day - @last_red_pencil_start >= ONE_MONTH
      self.red_pencil= false
    end

    @day = day
  end
  
  def price= new_price
    # activate red pencil promotion
    if percent_of(new_price, @price) >= MIN_CHANGE and ready_for_new_promotion?
      self.red_pencil= true
    end
    
    # prevent suspicious price changes
    if percent_of(new_price, @before_promotion_price) >= MAX_CHANGE
      self.red_pencil= false
    end

    # end promotion on price increase
    if new_price > @price
      self.red_pencil= false
    end
    
    if @price != new_price
      @last_price_change = @day
    end
    
    @price = new_price
  end

  def red_pencil?
    @red_pencil
  end

  private
  def red_pencil= value
    if not @red_pencil and value
      @last_red_pencil_start = @day
      # NOTE should be called before price change
      @before_promotion_price = @price
    end
    @red_pencil = value
  end

  def ready_for_new_promotion?
    @day - @last_price_change >= ONE_MONTH and
      @day - @last_red_pencil_start >= ONE_MONTH
  end

  def percent_of value, base
    (base - value).to_f / base
  end
end
