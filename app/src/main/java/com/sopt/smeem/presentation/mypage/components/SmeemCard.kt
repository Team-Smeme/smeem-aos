package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.presentation.home.calendar.ui.theme.Typography
import com.sopt.smeem.presentation.home.calendar.ui.theme.black
import com.sopt.smeem.presentation.home.calendar.ui.theme.gray100
import com.sopt.smeem.presentation.home.calendar.ui.theme.point
import com.sopt.smeem.presentation.home.calendar.ui.theme.white

@Composable
fun SmeemCard(
    modifier: Modifier = Modifier,
    text: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = white
        ),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, gray100)
    ) {
        Row(
            modifier = Modifier.padding(start = 20.dp),
        ) {
            Text(
                text = text,
                style = Typography.bodySmall,
                color = black,
                modifier = Modifier.padding(top = 17.dp, bottom = 18.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            content()
        }
    }
}

@Composable
fun EditButton(
    action: () -> Unit
) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = white
        ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Text(
            text = stringResource(R.string.smeem_card_edit),
            style = Typography.labelMedium,
            color = point,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 19.dp)
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SmeemCardPreview() {
    SmeemCard(
        text = "닉네임",
    ) {
        EditButton(action = {})
    }
}