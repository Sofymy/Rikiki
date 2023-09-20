package bme.projlab.rikiki.data.repositories

import android.util.Log
import bme.projlab.rikiki.data.entities.Card
import bme.projlab.rikiki.data.entities.Game
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
    override fun getCard() {
        val card = Card.values().random()
        val card2 = Card.values().random()
        val card3 = Card.values().random()
        val card4 = Card.values().random()
        val cards = arrayListOf(card, card2)
        val cards2 = arrayListOf(card3, card4)
        val hands1 = Firebase.auth.currentUser?.displayName?.let { mapOf(it to cards) }
        val hands2 = mapOf("aa" to cards2)
        val a = arrayListOf(hands1, hands2)
        val gameRef = Firebase.firestore.collection("games")
        Firebase.auth.currentUser?.displayName?.let {
            gameRef
                .document(it)
                .update("hands", a)
        }
    }

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

    override fun addCards(game: Game) {
        //deal
        val cards = dealCards(game)
        val hands = arrayListOf<Map<String, ArrayList<Card>>>()
        for((index, player) in game.players.withIndex()){
            val card = cards[index]
            val hand = arrayListOf(card)
            hands.add(mapOf(player to hand))
        }
        val gameRef = Firebase.firestore.collection("games")
        game.owner.let {
            //clear bet field
            gameRef
                .document(it)
                .update("bets", arrayListOf<Map<String, Int>>())
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

    override suspend fun placeBet(game: Game, bet: Int): GameResponse<Game> {
        return try {
            val gameRef = Firebase.firestore.collection("games")
            val betData: Map<String?, Int> = mapOf(Firebase.auth.currentUser?.displayName to bet)
            game.owner.let {
                gameRef
                    .document(it)
                    .update("bets", FieldValue.arrayUnion(betData))
                    .await()
            }
            GameResponse.Betting(game)
        }catch (e: Exception){
            GameResponse.Error(e.message.toString())
        }
    }

    override fun endRound(game: Game) {
        var trumpSuit = game.trump.suit

        val cards = ArrayList<Card>()
        game.hands.forEach { player ->
            cards + player.values
        }
        val winner = game.hands.maxBy {
            it.values.flatten().toMutableList().maxBy {
                it.rank.value
            }
        }
        Log.d("wwww", winner.toString())
    }

    private fun dealCards(game: Game): ArrayList<Card> {
        val cards = Card.values().toMutableList()
        game.trump = Card.values().random()
        cards.remove(game.trump)
        return cards.shuffled().take(game.players.size) as ArrayList<Card>
    }
}