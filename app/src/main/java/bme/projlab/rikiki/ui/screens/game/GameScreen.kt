package bme.projlab.rikiki.ui.screens.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bme.projlab.rikiki.R
import bme.projlab.rikiki.domain.viewmodels.GameViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun GameScreen(
    gameViewModel: GameViewModel = hiltViewModel(),
    owner: String?
){
    val game = gameViewModel.game.value
    val gameResponse = gameViewModel.gameFlow.collectAsState()
    gameViewModel.getGame(owner)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var allBetsPlaced by remember {
        mutableStateOf(false)
    }
    var sliderPosition by remember {
        mutableFloatStateOf(0f)
    }
    var buttonBid by remember {
        mutableStateOf(true)
    }
    var endRound by remember {
        mutableStateOf(false)
    }
    var cardFace by remember {
        mutableStateOf(CardFace.Back)
    }
    var user = Firebase.auth.currentUser?.displayName

    LaunchedEffect(gameResponse.value){
        when(gameResponse.value){
            else -> {}
        }
    }

    Column {
        Text(text = game.trump.toString())
        LazyColumn {
            items(game.hands) { hand ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Gray)
                        .border(2.dp, Color.White)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if(hand.player != user){
                        Text(hand.player)
                        Text(hand.cards.toString())
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
        Button(onClick = {
            gameViewModel.dealCards(game)
            buttonBid = true
            endRound = false
        }) {
            Text("add")
        }
        Column {
            Slider(
                value = sliderPosition,
                steps = game.round-1,
                valueRange = 0f..game.round.toFloat(),
                onValueChange = { sliderPosition = it }
            )
            Text(text = sliderPosition.toInt().toString())
            Button(onClick = {
                gameViewModel.makeBid(game, sliderPosition.toInt())
                buttonBid = false
            }, enabled = buttonBid) {
                Text("Bid")
            }

            FlipCard(
                cardFace = cardFace,
                onClick = {  },
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .aspectRatio(1f),
                front = {
                    Image(painterResource(R.drawable.card6),"content description")
                },
                back = {
                    Image(painterResource(R.drawable.back),"content description")
                },
            )
        }
        SnackbarHost(hostState = snackbarHostState)
    }

}

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    };

    abstract val next: CardFace
}

enum class RotationAxis {
    AxisX,
    AxisY,
}


//https://fvilarino.medium.com/creating-a-rotating-card-in-jetpack-compose-ba94c7dd76fb
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    axis: RotationAxis = RotationAxis.AxisY,
    back: @Composable () -> Unit = {},
    front: @Composable () -> Unit = {},
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 100,
            easing = FastOutSlowInEasing,
        ), label = ""
    )
    Card(
        onClick = { onClick(cardFace) },
        modifier = modifier
            .graphicsLayer {
                if (axis == RotationAxis.AxisX) {
                    rotationX = rotation.value
                } else {
                    rotationY = rotation.value
                }
                cameraDistance = 12f * density
            },
    ) {
        if (rotation.value <= 90f) {
            Box(
                Modifier.fillMaxSize()
            ) {
                front()
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (axis == RotationAxis.AxisX) {
                            rotationX = 180f
                        } else {
                            rotationY = 180f
                        }
                    },
            ) {
                back()
            }
        }
    }
}