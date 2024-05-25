package com.sopt.smeem.presentation.mypage.mysummary

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.domain.dto.GetBadgeListDto
import com.sopt.smeem.presentation.compose.components.LoadingScreen
import com.sopt.smeem.presentation.compose.theme.background
import com.sopt.smeem.presentation.mypage.components.MyBadgesBottomSheet
import com.sopt.smeem.presentation.mypage.components.MyBadgesContent
import com.sopt.smeem.presentation.mypage.components.MyPlanCard
import com.sopt.smeem.presentation.mypage.components.MySmeemCard
import com.sopt.smeem.presentation.mypage.components.NoMyPlanCard
import com.sopt.smeem.presentation.mypage.navigation.SettingNavGraph
import com.sopt.smeem.util.CrashlyticsManager
import com.sopt.smeem.util.VerticalSpacer
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySummaryScreen(
    navController: NavController,
    viewModel: MySummaryViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.collectAsState()
    val isDataChanged by viewModel.isDataChanged.collectAsStateWithLifecycle()

    LaunchedEffect(isDataChanged) {
        if (isDataChanged) {
            viewModel.fetchMySummaryData()
            viewModel.setDataChanged(false)
        }
    }

    /***** bottom sheet configuration *****/
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var selectedBadge by rememberSaveable { mutableStateOf<GetBadgeListDto?>(null) }

    val context = LocalContext.current

    if (selectedBadge != null) {
        MyBadgesBottomSheet(
            badge = selectedBadge!!,
            sheetState = sheetState,
            onDismiss = {
                selectedBadge = null
                coroutineScope.launch { sheetState.hide() }
            }
        )
    }

    /**** UI ****/
    when (val uiState = state.uiState) {
        is MySummaryUiState.Idle -> {
            // 데이터 로딩이 시간이 0.5초가 넘지 않을 때 보여줌
            Box(modifier = modifier.fillMaxSize())
        }

        is MySummaryUiState.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is MySummaryUiState.Success -> {
            val (smeemData, planData, badgesData) = uiState

            Surface(
                color = background
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    VerticalSpacer(height = 18.dp)

                    MySmeemCard(mySmeem = smeemData)

                    VerticalSpacer(height = 44.dp)

                    if (planData != null) {
                        MyPlanCard(myPlan = planData)
                    } else {
                        NoMyPlanCard(onSetPlanClick = {
                            navController.navigate(
                                SettingNavGraph.EditTrainingPlan.route
                            )
                        })
                    }

                    VerticalSpacer(height = 36.dp)

                    MyBadgesContent(
                        badges = badgesData,
                        onClickCard = { clickedBadge -> selectedBadge = clickedBadge },
                        modifier = Modifier.heightIn(max = 1000.dp)
                    )

                    VerticalSpacer(height = 100.dp)
                }
            }
        }

        is MySummaryUiState.Error -> {
            Toast.makeText(context, uiState.error.message, Toast.LENGTH_SHORT).show()
            CrashlyticsManager.logMessage(uiState.error.message ?: "MySummaryScreen Error")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MySummaryScreenPreview() {
    MySummaryScreen(
        navController = rememberNavController(),
        viewModel = hiltViewModel(),
        modifier = Modifier
    )
}