module Yahtzee
  module_function

  SINGLES_MAP = {
    ones: 1,
    twos: 2,
    threes: 3,
    fours: 4,
    fives: 5,
    sixes: 6
  }.freeze

  SINGLES_PROC =
    proc { |*args| rule_singles(*args) }

  RULE_MAP = {
    ones:   SINGLES_PROC,
    twos:   SINGLES_PROC,
    threes: SINGLES_PROC,
    fours:  SINGLES_PROC,
    fives:  SINGLES_PROC,
    sixes:  SINGLES_PROC,
    pair:   proc { |*args| rule_pair(*args) },
    two_pair: proc { |*args| rule_two_pair(*args) },
    fullhouse: proc { |*args| rule_full_house(*args) }
  }.freeze

  def score(rolls, rule)
    RULE_MAP[rule].call(rolls, rule)
  end

  def rule_singles(rolls, rule)
    rolls.select do |roll|
      roll == SINGLES_MAP[rule]
    end.reduce :+
  end

  def rule_pair(rolls, _rule)
    pairs = pairs rolls
    zero_or_pair_score(pairs)
  end

  def rule_two_pair(rolls, _rule)
    pairs = pairs rolls

    if pairs.length >= 2
      pairs.take(2).reduce(:+) * 2
    else
      0
    end
  end

  def rule_full_house(rolls, _rule)
    triple = rolls.find { |roll| rolls.count(roll) == 3 }
    pairs  = pairs(rolls.reject { |roll| roll == triple})
    pair   = pairs.first

    if triple && pair
      triple * 3 + pair * 2
    else
      0
    end
  end

  def zero_or_pair_score(pairs)
    pairs.empty? ? 0 : pairs.max * 2
  end

  def pairs(nums)
    nums.reduce([]) do |pairs, num|
      pair_count = nums.count(num) / 2
      if !pairs.include?(num) &&
         pair_count >= 1
        pairs + [num] * pair_count
      else
        pairs
      end
    end
  end
end
