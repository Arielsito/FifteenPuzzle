package com.example.fifteenpuzzle.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fifteenpuzzle.R
import com.example.fifteenpuzzle.presentation.components.Board
import com.example.fifteenpuzzle.presentation.components.WinDialog
import com.example.fifteenpuzzle.ui.theme.FifteenPuzzleTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FifteenPuzzleApp() {
    val list = remember { mutableStateListOf(*(1..15).toList().toTypedArray(), 0) }
    var init by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var timer by remember { mutableLongStateOf(0L) }
    var timerStart by remember { mutableStateOf(false) }

    val formattedTime by remember {
        derivedStateOf {
            "%02d:%02d".format(timer / 60, timer % 60)
        }
    }
    LaunchedEffect(timerStart) {
        if (timerStart) {
            System.currentTimeMillis()
            while (true) {
                delay(1000)
                timer++
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = stringResource(R.string.timer, formattedTime),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Board(
            list = list,
            isInitialized = init,
            columns = 4,
            onSwipe = { from, to ->
                if (list[to] == 0) {
                    val aux = list[from]
                    list[from] = list[to]
                    list[to] = aux
                }

                if (isWin(list)) {
                    timerStart = false
                    scope.launch {
                        delay(500L)
                        showDialog = true
                    }
                }
            }
        )
        Spacer(modifier = Modifier.padding(16.dp))
        Button(
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            onClick = {
                shuffleBoard(list, 4)
                init = 1
                timerStart = true
                timer = 0
            }
        ) {
            Text(
                text = if (init == 0) stringResource(R.string.start) else stringResource(R.string.restart),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        if (showDialog) {
            WinDialog(
                title = stringResource(R.string.win_dialog_title),
                text = stringResource(R.string.win_dialog_desc, formattedTime),
                image = painterResource(R.drawable._win_icon),
                imageDescription = stringResource(R.string._win_icon_description),
                onDismissRequest = { showDialog = false },
                onDismiss = {
                    showDialog = false
                    timer = 0
                    init = 0
                },
                onDismissText = stringResource(R.string.exit),
                onConfirm = {
                    showDialog = false
                    init = 1
                    timer = 0
                    timerStart = true
                    shuffleBoard(list, 4)
                },
                onConfirmText = stringResource(R.string.new_game)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DefaultPreview() {
    FifteenPuzzleTheme() {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            FifteenPuzzleApp()
        }
    }
}

fun isWin(items: MutableList<Int>): Boolean {
    return items.last() == 0 && items.dropLast(1) == items.sorted().drop(1)
}

fun shuffleBoard(items: MutableList<Int>, columns: Int) {
    repeat(100) {
        val zero = items.indexOf(0)
        val adjacentNumbers = mutableListOf<Int>()

        if (zero % columns != 0) adjacentNumbers.add(zero - 1)
        if ((zero + 1) % columns != 0) adjacentNumbers.add(zero + 1)
        if (zero - columns >= 0) adjacentNumbers.add(zero - columns)
        if (zero + columns < items.size) adjacentNumbers.add(zero + columns)

        if (adjacentNumbers.isNotEmpty()) {
            val random = adjacentNumbers.random()
            val aux = items[random]
            items[random] = 0
            items[zero] = aux
        }
    }
}