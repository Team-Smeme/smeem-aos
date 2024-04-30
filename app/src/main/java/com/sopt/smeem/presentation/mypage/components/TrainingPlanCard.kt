package com.sopt.smeem.presentation.mypage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.mypage.TrainingPlanDescription
import com.sopt.smeem.presentation.compose.theme.Typography
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray600
import com.sopt.smeem.presentation.compose.theme.point
import com.sopt.smeem.presentation.compose.theme.white
import com.sopt.smeem.util.HorizontalSpacer

@Composable
fun TrainingPlanCard(
    isSelected: Boolean = false,
    planId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val textColor = if (isSelected) point else gray600
    val textStyle = if (isSelected) Typography.bodySmall else Typography.bodyMedium
    val planDescription = TrainingPlanDescription.entries.firstOrNull { it.id == planId } ?: return
    val borderColor = if (isSelected) point else gray100
    val iconVector =
        if (isSelected) ImageVector.vectorResource(id = R.drawable.ic_selection_active)
        else ImageVector.vectorResource(id = R.drawable.ic_selection_inactive)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = white,
        ),
        border = BorderStroke(1.5.dp, borderColor)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 13.dp)
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = stringResource(R.string.training_plan_selection_icon),
                tint = Color.Unspecified,
                modifier = Modifier.padding(top = 21.dp, bottom = 19.dp)
            )

            HorizontalSpacer(width = 12.dp)

            Text(
                text = planDescription.description,
                style = textStyle,
                color = textColor,
                modifier = Modifier.padding(top = 21.dp, bottom = 20.dp)
            )
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TrainingPlanUnSelectedCardPreview() {
    TrainingPlanCard(planId = 1, onClick = {})
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TrainingPlanSelectedCardPreview() {
    TrainingPlanCard(planId = 1, isSelected = true, onClick = {})
}