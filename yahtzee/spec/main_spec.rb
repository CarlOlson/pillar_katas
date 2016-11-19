
require 'main'

describe Yahtzee do

  describe '#score' do

    def roll_n
    end

    it 'returns a score' do
      score = described_class.score([1, 1, 2, 4, 4], :fours)
      expect(score).to eq 8
    end

    it 'scores singles' do
      score = described_class.score([1, 1, 2, 4, 4], :ones)
      expect(score).to eq 2
    end

    it 'pair rule scores sum of highest pair' do
      score = described_class.score([3, 3, 4, 4, 5], :pair)
      expect(score).to eq 8
    end

    it 'pair rule rejects rolls without pairs' do
      score = described_class.score([3, 1, 4, 2, 5], :pair)
      expect(score).to eq 0
    end

    it 'two pair rule gives sum of all 4' do
      score = described_class.score([1, 1, 2, 3, 3], :two_pair)
      expect(score).to eq 8
    end

    it 'two pair rule rejects rolls with one pair' do
      score = described_class.score([1, 1, 2, 3, 4], :two_pair)
      expect(score).to eq 0
    end

    it 'two pair rule gives sum of 4-of-a-kind' do
      score = described_class.score([1, 1, 1, 1, 4], :two_pair)
      expect(score).to eq 4
    end

    it 'full house rule gives sum of 3-of-a-kind with pair' do
      score = described_class.score([1, 1, 1, 4, 4], :fullhouse)
      expect(score).to eq 11
    end

  end

end
