package com.sopt.smeem.presentation.mypage.mysummary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.data.datasource.BadgeList
import com.sopt.smeem.domain.model.mypage.MyBadges
import com.sopt.smeem.domain.model.mypage.MyPlan
import com.sopt.smeem.domain.model.mypage.MySmeem
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.mypage.components.MyBadgesBottomSheet
import com.sopt.smeem.presentation.mypage.components.MyBadgesContent
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.smeem.presentation.compose.components.LoadingScreen
import com.sopt.smeem.presentation.mypage.components.MyPlanCard
import com.sopt.smeem.presentation.mypage.components.MySmeemCard
import com.sopt.smeem.util.VerticalSpacer
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySummaryScreen(
    viewModel: MySummaryViewModel,
    modifier: Modifier
) {
    val state by viewModel.collectAsState()

    when (val uiState = state.uiState) {
        is MySummaryUiState.Loading -> {
            LoadingScreen()
        }

        is MySummaryUiState.Success -> {
//            val (smeemData, planData, + 배지) = uiState
            val (smeemData, planData) = uiState

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                VerticalSpacer(height = 18.dp)

                MySmeemCard(mySmeem = smeemData)

                VerticalSpacer(height = 44.dp)

                MyPlanCard(myPlan = planData)

                VerticalSpacer(height = 36.dp)

                MyBadgesContent(
                    badges = BadgeList.sprint2,
                    onClickCard = { clickedBadge -> selectedBadge = clickedBadge },
                    modifier = Modifier.heightIn(max = 1000.dp)
                )
    
                VerticalSpacer(height = 120.dp)
            }
        }

        is MySummaryUiState.Error -> {
            // ErrorScreen
        }
       
    }


}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MySummaryScreenPreview() {
    MySummaryScreen(viewModel = hiltViewModel(), modifier = Modifier)
}