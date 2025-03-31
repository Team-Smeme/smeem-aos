package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.pointInactive
import com.sopt.smeem.presentation.compose.theme.white
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SmeemButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    isButtonEnabled: Boolean = true,
    throttleTime: Long = 300L
) {
    var isThrottling by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            if (!isThrottling) {
                isThrottling = true
                onClick()

                coroutineScope.launch {
                    delay(throttleTime)
                    isThrottling = false
                }
            }
        },
        modifier = modifier.fillMaxWidth(),
        enabled = isButtonEnabled,
        shape = RoundedCornerShape(6.dp),
        contentPadding = PaddingValues(vertical = 19.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = point,
            contentColor = white,
            disabledContainerColor = pointInactive,
            disabledContentColor = white
        )
    ) {
        Text(
            text = text,
            style = Typography.titleLarge,
            color = white
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SmeemActiveButtonPreview() {
    SmeemButton(text = "버튼", onClick = {})
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SmeemNonActiveButtonPreview() {
    SmeemButton(text = "버튼", onClick = {}, isButtonEnabled = false)
}