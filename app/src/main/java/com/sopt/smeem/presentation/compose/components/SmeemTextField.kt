package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.point

@Composable
fun SmeemTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (TextFieldValue) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardOptions.keyboardType,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = gray100, shape = RoundedCornerShape(6.dp))
            .padding(vertical = 19.dp, horizontal = 20.dp),
        textStyle = Typography.headlineSmall.copy(
            color = point
        ),
        cursorBrush = SolidColor(point)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SmeemTextFieldPreview() {
    SmeemTextField(
        value = TextFieldValue("텍스트"),
        onValueChange = {}
    )
}