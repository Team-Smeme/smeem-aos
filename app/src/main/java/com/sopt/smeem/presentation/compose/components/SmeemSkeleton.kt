package com.sopt.smeem.presentation.compose.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.sopt.smeem.presentation.compose.theme.gray100
import com.sopt.smeem.presentation.compose.theme.gray200

/**
 * 주어진 콘텐츠에 로딩 스켈레톤 효과를 적용하는 컴포저블 함수입니다.
 *
 * @param shimmerColors 스켈레톤 효과에 적용할 색상을 나타내는 리스트입니다.
 * @param content 로딩 스켈레톤 효과를 적용할 콘텐츠를 제공하는 람다 함수입니다. 이 람다 함수는 `Brush`를 매개변수로 받아야 합니다.
 */
@Composable
fun LoadingSkeletonEffect(
    shimmerColors: List<Color> = listOf(
        gray100,
        gray200,
        gray100,
    ),
    content: @Composable (Brush) -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "BookmarkSkeleton")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "BookmarkSkeleton",
    )
    val brush by remember(shimmerColors) {
        derivedStateOf {
            Brush.linearGradient(
                colors = shimmerColors,
                start = Offset.Zero,
                end = Offset(x = translateAnim, y = translateAnim),
            )
        }
    }

    content(brush)
}

/**
 * 스켈레톤 UI 컴포넌트입니다.
 *
 * 로딩 상태를 나타내기 위해 지정된 [shape]와 [modifier]를 사용하여 스켈레톤 효과를 보여줍니다.
 *
 * @param shape 스켈레톤의 모양을 결정하는 Shape
 * @param modifier Modifier를 통해 크기, 배경 등 추가 속성 지정 (기본값: Modifier)
 */
@Composable
fun SmeemSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    shimmerColors: List<Color> = listOf(
        gray100,
        gray200,
        gray100,
    ),
) {
    LoadingSkeletonEffect(shimmerColors = shimmerColors) { brush ->
        Spacer(
            modifier = modifier.background(
                brush = brush,
                shape = shape,
            ),
        )
    }
}
