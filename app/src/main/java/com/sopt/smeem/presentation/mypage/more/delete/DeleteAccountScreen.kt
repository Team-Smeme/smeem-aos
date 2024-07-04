package com.sopt.smeem.presentation.mypage.more.delete

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.presentation.compose.components.SmeemButton
import com.sopt.smeem.presentation.compose.components.SmeemTextField
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.mypage.components.SelectCard
import com.sopt.smeem.util.VerticalSpacer

@Composable
fun DeleteAccountScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val deleteReasonList =
        listOf(
            "이용이 어렵고 서비스가 불안정해요.",
            "일기 작성을 도와주는 기능이 부족해요",
            "일기 쓰기가 귀찮아요",
            "다른 앱이 더 사용하기 편해요.",
            "기타 의견"
        )
    var selectedItem by rememberSaveable { mutableStateOf("") }
    var textFieldState by remember { mutableStateOf(TextFieldValue(text = "")) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxSize()) {
        VerticalSpacer(height = 14.dp)

        Text(
            text = "정말 떠나시는 건가요?",
            style = Typography.headlineMedium,
            color = black,
            modifier = Modifier.padding(start = 26.dp)
        )

        VerticalSpacer(height = 6.dp)

        Text(
            text = "계정을 삭제하는 이유를 선택해주세요.\n" +
                    "서비스 개선에 참고할게요.",
            style = Typography.bodySmall.copy(
                lineHeight = 22.sp
            ),
            color = black,
            modifier = Modifier.padding(start = 26.dp)
        )

        VerticalSpacer(height = 12.dp)

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(deleteReasonList.size) { index ->
                val item = deleteReasonList[index]

                SelectCard(
                    isSelected = selectedItem == item,
                    selectContent = item,
                    onClick = { selectedItem = item },
                )
            }
        }

        VerticalSpacer(height = 27.dp)

        Text(
            text = "계정 삭제 사유를 자유롭게 적어주세요.",
            style = Typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            ),
            color = black,
            modifier = Modifier.padding(start = 26.dp)
        )

        VerticalSpacer(height = 22.dp)

        SmeemTextField(
            value = textFieldState,
            onValueChange = { newValue ->
                textFieldState = newValue
            },
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        textFieldState = textFieldState.copy(
                            selection = TextRange(
                                textFieldState.text.length
                            )
                        )
                    }
                },
            minLines = 2,
            backgroundColor = gray100,
            cursorColor = black,
            hasBorder = false,
            textStyle = Typography.bodySmall.copy(
                color = black,
                lineHeight = 22.sp
            )
        )

        VerticalSpacer(height = 12.dp)

        SmeemButton(
            text = "탈퇴하기",
            onClick = {},
            modifier = Modifier.padding(horizontal = 18.dp),
            isButtonEnabled = true
        )

        VerticalSpacer(height = 16.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DeleteAccountScreenPreview() {
    DeleteAccountScreen(navController = rememberNavController(), modifier = Modifier)
}