package com.sopt.smeem.presentation.bookmark.diary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.smeem.presentation.bookmark.diary.contract.BookmarkDiaryIntent
import com.sopt.smeem.presentation.bookmark.diary.contract.BookmarkDiarySideEffect
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray300
import com.sopt.smeem.presentation.compose.theme.gray900
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.toTextDp
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun BookmarkDiaryScreen(
    expression: String,
    translatedExpression: String,
    viewModel: BookmarkDiaryViewModel,
    onNavigateBack: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onShowError: (String) -> Unit = {}
) {
    val state by viewModel.collectAsState()
    
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is BookmarkDiarySideEffect.NavigateBack -> onNavigateBack()
            is BookmarkDiarySideEffect.NavigateToHomeWithSuccess -> onNavigateToHome()
            is BookmarkDiarySideEffect.ShowError -> onShowError(sideEffect.message)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(white)
    ) {
        NavigationBar(
            onCancelClick = { viewModel.onIntent(BookmarkDiaryIntent.OnBackClick) },
            onCompleteClick = { viewModel.onIntent(BookmarkDiaryIntent.OnCompleteClick(expression, translatedExpression)) },
            isCompleteEnabled = state.isValidDiary,
            isLoading = state.isLoading
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$expression $translatedExpression",
                style = Typography.bodyLarge.copy(
                    color = gray900,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.dp.toTextDp()
                )
            )

            Spacer(modifier = Modifier.height(6.dp))


            SmeemTextField(
                value = state.diaryText,
                onValueChange = { viewModel.onIntent(BookmarkDiaryIntent.OnDiaryTextChange(it)) },
                placeholder = "이 표현을 활용해 일기를 써보세요!",
                minLines = 15,
                backgroundColor = white,
                cursorColor = black,
                hasBorder = false,
                textStyle = Typography.bodyMedium.copy(
                    color = black,
                    fontSize = 16.dp.toTextDp(),
                    fontWeight = FontWeight.Normal
                ),
                placeholderStyle = Typography.bodyLarge.copy(
                    fontSize = 16.dp.toTextDp(),
                    fontWeight = FontWeight.Normal
                ),
                placeholderColor = gray300,
                contentPadding = PaddingValues(0.dp),
                cornerRadius = 0.dp
            )
        }
    }
}

@Composable
private fun NavigationBar(
    onCancelClick: () -> Unit,
    onCompleteClick: () -> Unit,
    isCompleteEnabled: Boolean,
    isLoading: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(white),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "취소",
            style = Typography.bodyLarge.copy(
                color = gray900,
                fontWeight = FontWeight.Medium,
                fontSize = 16.dp.toTextDp()
            ),
            modifier = Modifier
                .clickable { onCancelClick() }
                .padding(horizontal = 18.dp, vertical = 17.dp)
        )

        Text(
            text = "English",
            style = Typography.headlineSmall.copy(
                color = gray900,
                fontWeight = FontWeight.Bold,
                fontSize = 18.dp.toTextDp()
            )
        )

        Text(
            text = if (isLoading) "저장중..." else "완료",
            style = Typography.bodyLarge.copy(
                color = if (isCompleteEnabled && !isLoading) point else gray300,
                fontWeight = FontWeight.Bold,
                fontSize = 16.dp.toTextDp()
            ),
            modifier = Modifier
                .clickable(enabled = isCompleteEnabled && !isLoading) {
                    if (isCompleteEnabled && !isLoading) onCompleteClick()
                }
                .padding(horizontal = 18.dp, vertical = 17.dp)

        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookmarkDiaryScreenPreview() {
    BookmarkDiaryScreen(
        expression = "Pissed off",
        translatedExpression = "엄청 화나다",
        viewModel = hiltViewModel<BookmarkDiaryViewModel>(),
    )
}
