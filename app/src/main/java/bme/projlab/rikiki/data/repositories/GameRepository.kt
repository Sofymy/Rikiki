package bme.projlab.rikiki.data.repositories

import bme.projlab.rikiki.data.entities.Bid
import bme.projlab.rikiki.data.entities.cards.Card
import bme.projlab.rikiki.data.entities.Game
import bme.projlab.rikiki.data.entities.Hand
import bme.projlab.rikiki.data.entities.Leader
import bme.projlab.rikiki.data.entities.Points
import bme.projlab.rikiki.domain.base.BaseGameRepository
import bme.projlab.rikiki.domain.responses.ResourceResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GameRepository: BaseGameRepository {

    private val gameRef = Firebase.firestore.collection("games")
    override fun getGame(owner: String?): Flow<ResourceResponse<Game>> = callbackFlow {
        val docRef = owner?.let {
            gameRef
                .document(it)
        }
        val listener = docRef?.addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val gameObject = snapshot.toObject(Game::class.java)
                trySend(ResourceResponse.Success(data = gameObject)).isSuccess
            }
        }
        awaitClose {
            listener?.remove()
            close()
        }
    }

    override fun dealCards(game: Game) {
        //deal
        game.trump = getTrump()
        val cards = dealCardsHelper(game)
        val hands = arrayListOf<Hand>()
        for((index, player) in game.players.withIndex()){
            val card = cards[index]
            val hand = arrayListOf(card)
            hands.add(Hand(player, hand))
        }
        game.hands = hands
        game.leader = getLeader(game)
        game.hands = countPoints(game)

        game.owner.let {
            //clear bet field
            gameRef
                .document(it)
                .update("bids", ArrayList<Bid>())
            gameRef
                .document(it)
                .update("leader", game.leader)
            gameRef
                .document(it)
                .update("trump", game.trump)
            gameRef
                .document(it)
                .update("hands", game.hands)
            gameRef
                .document(it)
                .update("round", FieldValue.increment(1))
        }
    }

    private fun getLeader(game: Game): Leader? {
        val round = game.round
        val player = game.players[round % game.players.size]
        val card = game.hands.find { hand ->
            hand.player == player
        }?.cards?.get(0)
        return card?.let { Leader(player, it) }
    }

    override fun makeBid(game: Game, bid: Int) {
        val bidData = Firebase.auth.currentUser?.displayName?.let { Bid(it,bid) }
        if (bidData != null) {
            game.bids.add(bidData)
        }
        game.owner.let {
            gameRef
                .document(it)
                .update("bids", FieldValue.arrayUnion(bidData))
        }
        if(game.bids.size > 1){
            val bids = countBids(game)
            endRound(bids, game)
            game.owner.let {
                gameRef
                    .document(it)
                    .update("bids", bids)
            }
        }
    }

    private fun countBids(game: Game): ArrayList<Bid> {
        val winner = game.hands.maxBy {
            it.points
        }
        for(player in game.bids){
            if(winner.player == player.player){
                player.took++
            }
        }
        return game.bids
    }

    private fun countPoints(game: Game): ArrayList<Hand> {
        game.hands.forEach { hand ->
            when (hand.cards[0].suit) {
                game.trump.suit -> {
                    hand.points = 13 + hand.cards[0].rank.value
                }
                game.leader?.card?.suit -> {
                    hand.points = hand.cards[0].rank.value
                }
                else -> hand.points = 0
            }
        }
        return game.hands
    }

    private fun dealCardsHelper(game: Game): ArrayList<Card> {
        val cards = Card.values().toMutableList()
        cards.remove(game.trump)
        return cards.shuffled().take(game.players.size) as ArrayList<Card>
    }

    private fun getTrump(): Card {
        val cards = Card.values().toMutableList()
        return cards.random()
    }

    private fun endRound(bids: ArrayList<Bid>, game: Game){
        val points = arrayListOf<Points>()
        for (bid in bids){
            val diff = kotlin.math.abs(bid.bid - bid.took)
            if(diff == 0){
                points.add(Points(bid.player,10+bid.bid*2))
            }
            else
                points.add(Points(bid.player, -diff*2))
        }
        game.owner.let {
            gameRef
                .document(it)
                .update("points", points)
        }
    }
}