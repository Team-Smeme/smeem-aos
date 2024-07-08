package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.black
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray400
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white

@Composable
fun SmeemTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: String = "",
    minLines: Int = 1,
    backgroundColor: Color = white,
    cursorColor: Color = point,
    hasBorder: Boolean = true,
    textStyle: TextStyle = Typography.headlineSmall.copy(
        color = point
    ),
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = minLines == 1,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardOptions.keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (hasBorder) 1.dp else 0.dp,
                color = gray100,
                shape = RoundedCornerShape(6.dp)
            )
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(vertical = 19.dp, horizontal = 20.dp),
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor),
        decorationBox = { innerTextField ->
            if (value.text.isEmpty()) {
                Text(
                    text = placeholder,
                    style = Typography.bodySmall.copy(
                        lineHeight = 22.sp
                    ),
                    color = gray400
                )
            }
            innerTextField()
        }
    )
}

@Preview
@Composable
fun NicknameTextFieldPreview() {
    SmeemTextField(
        value = TextFieldValue("텍스트"),
        onValueChange = {}
    )
}

@Preview
@Composable
fun DeleteAccountTextFieldPreview() {
    SmeemTextField(
        value = TextFieldValue("텍스트"),
        onValueChange = {},
        minLines = 2,
        placeholder = "텍스트 힌트",
        backgroundColor = gray100,
        cursorColor = black,
        hasBorder = false,
        textStyle = Typography.bodySmall.copy(
            color = black,
            lineHeight = 22.sp
        )
    )
}