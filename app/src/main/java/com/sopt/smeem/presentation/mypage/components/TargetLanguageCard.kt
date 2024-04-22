package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.util.noRippleClickable

@Composable
fun TargetLanguageCard(
    targetLanguage: String = stringResource(R.string.language_english),
    onEditClick: () -> Unit,
) {
    SmeemContents(title = stringResource(R.string.my_page_languages)) {
        SmeemCard(
            text = targetLanguage,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_checked),
                contentDescription = stringResource(R.string.edit_target_language),
                tint = point,
                modifier = Modifier
                    .padding(top = 7.dp, bottom = 7.dp, end = 14.dp)
                    .noRippleClickable { onEditClick() }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TargetLanguageCardPreview() {
    TargetLanguageCard(
        onEditClick = {}
    )
}