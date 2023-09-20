package bme.projlab.rikiki.data.repositories

import android.util.Log
import bme.projlab.rikiki.data.entities.Bid
import bme.projlab.rikiki.data.entities.Card
import bme.projlab.rikiki.data.entities.Game
import bme.projlab.rikiki.data.entities.Hand
import bme.projlab.rikiki.domain.base.BaseGameRepository
import bme.projlab.rikiki.domain.responses.GameResponse
import bme.projlab.rikiki.domain.responses.ResourceResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class GameRepository: BaseGameRepository {

    override fun getGame(owner: String?): Flow<ResourceResponse<Game>> = callbackFlow {
        val docRef = owner?.let {
            Firebase
                .firestore
                .collection("games")
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
        val cards = dealCardsHelper(game)
        val hands = arrayListOf<Hand>()
        for((index, player) in game.players.withIndex()){
            val card = cards[index]
            val hand = arrayListOf(card)
            hands.add(Hand(player, hand))
        }
        val gameRef = Firebase.firestore.collection("games")
        game.owner.let {
            //clear bet field
            gameRef
                .document(it)
                .update("bids", ArrayList<Bid>())
            gameRef
                .document(it)
                .update("trump", game.trump)
            gameRef
                .document(it)
                .update("hands", hands)
            gameRef
                .document(it)
                .update("round", FieldValue.increment(1))
        }
    }

    override fun makeBid(game: Game, bid: Int) {
        val gameRef = Firebase.firestore.collection("games")
        val bidData = Firebase.auth.currentUser?.displayName?.let { Bid(it,bid) }
        game.owner.let {
            gameRef
                .document(it)
                .update("bids", FieldValue.arrayUnion(bidData))
        }
    }

    override fun endRound(game: Game) {
        val winner = game.hands.maxBy { it ->
            it.cards.maxBy {  card ->
                card.rank.value
            }
        }
        Log.d("wwww", winner.toString())
    }

    private fun dealCardsHelper(game: Game): ArrayList<Card> {
        val cards = Card.values().toMutableList()
        game.trump = Card.values().random()
        cards.remove(game.trump)
        return cards.shuffled().take(game.players.size) as ArrayList<Card>
    }
}