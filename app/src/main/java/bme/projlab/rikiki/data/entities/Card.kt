package bme.projlab.rikiki.data.entities

enum class Card(val rank: Rank, val suit: Suit) {
    TWO_OF_SPADES(Rank.TWO, Suit.SPADES),
    THREE_OF_SPADES(Rank.THREE, Suit.SPADES),
    FOUR_OF_SPADES(Rank.FOUR, Suit.SPADES),
    FIVE_OF_SPADES(Rank.FIVE, Suit.SPADES),
    SIX_OF_SPADES(Rank.SIX, Suit.SPADES),
    SEVEN_OF_SPADES(Rank.SEVEN, Suit.SPADES),
    EIGHT_OF_SPADES(Rank.EIGHT, Suit.SPADES),
    NINE_OF_SPADES(Rank.NINE, Suit.SPADES),
    TEN_OF_SPADES(Rank.TEN, Suit.SPADES),
    JACK_OF_SPADES(Rank.JACK, Suit.SPADES),
    QUEEN_OF_SPADES(Rank.QUEEN, Suit.SPADES),
    KING_OF_SPADES(Rank.KING, Suit.SPADES),
    ACE_OF_SPADES(Rank.ACE, Suit.SPADES);

    private val rankComparator = Comparator<Rank> { r1, r2 ->
        r1.value - r2.value
    }
    val cardComparator = Comparator<Card> { c1, c2 ->
        rankComparator.compare(c1.rank, c2.rank)
    }
}
