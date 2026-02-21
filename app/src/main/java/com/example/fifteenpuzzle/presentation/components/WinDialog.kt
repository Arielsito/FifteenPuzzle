package com.example.fifteenpuzzle.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fifteenpuzzle.R
import com.example.fifteenpuzzle.presentation.shuffleBoard
import com.example.fifteenpuzzle.ui.theme.FifteenPuzzleTheme

@Composable
fun WinDialog(
    title: String,
    text: String,
    image: Painter,
    imageDescription: String,
    onDismissRequest: () -> Unit,
    onDismiss: () -> Unit,
    onDismissText: String,
    onConfirm: () -> Unit,
    onConfirmText: String
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Image(
                painter = image,
                modifier = Modifier.fillMaxWidth().height(80.dp),
                contentDescription = imageDescription,
                contentScale = ContentScale.Fit,
            )
        },
        title = { Text(text = title) },
        text = { Text(text = text, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
        confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    onClick = onDismiss
                ) {
                    Text(text = onDismissText)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                    onClick = onConfirm
                ) {
                    Text(text = onConfirmText)
                }
            }
        }
    )

}

//@Preview (showBackground = true)
//@Composable
//fun PreviewDialog() {
//    FifteenPuzzleTheme() {
//        WinDialog(
//            title = "Congratulations!",
//            text = "You won the game!",
//            image = painterResource(R.drawable._win_icon),
//            imageDescription = stringResource(R.string._win_icon_description),
//            onDismissRequest = {},
//            onDismiss = {},
//            onDismissText = "Exit",
//            onConfirm = {},
//            onConfirmText = "Play Again"
//        )
//    }
//}