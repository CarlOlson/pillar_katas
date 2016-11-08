
class Product
  attr_reader :price
  attr_reader :day
  
  def initialize price
    @price = price
    @day   = 0
    @red_pencil = false
  end

  def day= value
    raise StandardError if value < day
    @day = value
  end
  
  def price= value
    @price = value
    @red_pencil = true
  end

  def red_pencil?
    @red_pencil
  end
end
