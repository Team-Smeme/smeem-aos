package com.sopt.smeem.presentation.coach

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.presentation.IntentConstants.DIARY_CONTENT
import com.sopt.smeem.presentation.IntentConstants.DIARY_ID
import com.sopt.smeem.presentation.IntentConstants.RETRIEVED_BADGE_DTO
import com.sopt.smeem.presentation.IntentConstants.SNACKBAR_TEXT
import com.sopt.smeem.presentation.base.DefaultSnackBar
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

@AndroidEntryPoint
class CoachActivity : ComponentActivity() {
    private val viewModel by viewModels<CoachViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val diaryContent = intent.getStringExtra(DIARY_CONTENT) ?: ""
        val diaryId = intent.getLongExtra(DIARY_ID, -1)
        viewModel.initialize(diaryId, diaryContent)

        setContent {
            SmeemTheme {
                CoachRoute(
                    viewModel = viewModel,
                    onClosesClick = {
                        Intent(this, HomeActivity::class.java).apply {
                            putExtra(
                                RETRIEVED_BADGE_DTO,
                                intent.getSerializableExtra(RETRIEVED_BADGE_DTO) as Serializable
                            )
                            flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }.run(::startActivity)
                    }
                )
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
