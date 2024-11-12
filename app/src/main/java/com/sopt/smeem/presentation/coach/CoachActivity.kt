package com.sopt.smeem.presentation.coach

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sopt.smeem.presentation.IntentConstants.DIARY_CONTENT
import com.sopt.smeem.presentation.IntentConstants.DIARY_ID
import com.sopt.smeem.presentation.IntentConstants.SNACKBAR_TEXT
import com.sopt.smeem.presentation.base.DefaultSnackBar
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoachActivity : ComponentActivity() {
    private val viewModel by viewModels<CoachViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val diaryContent = intent.getStringExtra(DIARY_CONTENT) ?: ""
        viewModel.setDiaryId(intent.getLongExtra(DIARY_ID, -1))
        viewModel.getDiaryDetail()

        setContent {
            SmeemTheme {

            }
        }

        showDiaryCompleted()
    }

    private fun showDiaryCompleted() {
        val msg = intent.getStringExtra(SNACKBAR_TEXT)
        if (msg != null) {
            DefaultSnackBar.make(findViewById(android.R.id.content), msg).show()
            intent.removeExtra(SNACKBAR_TEXT)
        }
    }
}
