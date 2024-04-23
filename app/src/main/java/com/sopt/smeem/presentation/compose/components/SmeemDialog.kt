package com.sopt.smeem.presentation.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.theme.Typography
import com.sopt.smeem.presentation.theme.black
import com.sopt.smeem.presentation.theme.gray600
import com.sopt.smeem.presentation.theme.point
import com.sopt.smeem.presentation.theme.white

@Composable
fun SmeemDialog(
    setShowDialog: (Boolean) -> Unit,
    title: String,
    content: String,
    onConfirmButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        title = {
            Text(
                text = title,
                style = Typography.titleSmall,
                color = black
            )
        },
        text = {
            Text(
                text = content,
                style = Typography.labelMedium,
                color = gray600
            )
        },
        shape = RoundedCornerShape(10.dp),
        containerColor = white,
        confirmButton = {
            TextButton(
                onClick = onConfirmButtonClick
            ) {
                Text(
                    text = stringResource(R.string.smeem_dialog_yes),
                    style = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = point
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { setShowDialog(false) }) {
                Text(
                    text = stringResource(R.string.smeem_dialog_no),
                    style = Typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = point
                )
            }
        },
        modifier = modifier.fillMaxWidth()
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SmeemDialogPreview() {
    SmeemDialog(
        setShowDialog = {},
        title = "Title",
        content = "Content Content Content Content",
        onConfirmButtonClick = {},
    )
}