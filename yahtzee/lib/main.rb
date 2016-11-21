module Yahtzee
  module_function

  SINGLES_MAP = {
    ones:   1,
    twos:   2,
    threes: 3,
    fours:  4,
    fives:  5,
    sixes:  6
  }.freeze

  RULE_MAP = {
    ones:      :rule_singles,
    twos:      :rule_singles,
    threes:    :rule_singles,
    fours:     :rule_singles,
    fives:     :rule_singles,
    sixes:     :rule_singles,
    pair:      :rule_pair,
    two_pair:  :rule_two_pair,
    fullhouse: :rule_full_house
  }.freeze

  def score(rolls, rule)
    method(RULE_MAP[rule])
      .call(rolls, rule)
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

    if pairs.length >= 4
      pairs.take(4).reduce(:+)
    else
      0
    end
  end

  def rule_full_house(rolls, _rule)
    has_triple = rolls.any? { |roll| rolls.count(roll) == 3 }
    has_pair   = rolls.any? { |roll| rolls.count(roll) == 2 }

    if has_triple && has_pair
      rolls.reduce(:+)
    else
      0
    end
  end

  def zero_or_pair_score(pairs)
    pairs.empty? ? 0 : pairs.max * 2
  end

  def pairs(nums)
    nums
      .group_by(&:itself)
      .values
      .map { |group| group.drop(group.size % 2) }
      .flatten
  end
end
