package com.sopt.smeem.presentation.bookmark.navigation

import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.sopt.smeem.presentation.bookmark.BookmarkScreen
import com.sopt.smeem.presentation.bookmark.detail.BookmarkDetailScreen
import com.sopt.smeem.presentation.bookmark.diary.BookmarkDiaryScreen

@Composable
fun BookmarkNavHost(
    navController: NavHostController,
    onNavigateToHome: () -> Unit = {}
) {
    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BookmarkRoute.BookmarkList,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<BookmarkRoute.BookmarkList> {
                BookmarkScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToDetail = { bookmarkId ->
                        navController.navigate(BookmarkRoute.BookmarkDetail(bookmarkId))
                    }
                )
            }

            composable<BookmarkRoute.BookmarkDetail> { backStackEntry ->
                val args = backStackEntry.toRoute<BookmarkRoute.BookmarkDetail>()

                BookmarkDetailScreen(
                    bookmarkId = args.bookmarkId,
                    viewModel = hiltViewModel(),
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToUseExpression = { combinedExpression ->
                        val parts = combinedExpression.split(" ", limit = 2)
                        if (parts.size >= 2) {
                            navController.navigate(BookmarkRoute.BookmarkDiary(parts[0], parts[1]))
                        }
                    }
                )
            }

            composable<BookmarkRoute.BookmarkDiary> { backStackEntry ->
                val args = backStackEntry.toRoute<BookmarkRoute.BookmarkDiary>()
                val context = navController.context

                BookmarkDiaryScreen(
                    expression = args.expression,
                    translatedExpression = args.translatedExpression,
                    viewModel = hiltViewModel(),
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToHome = onNavigateToHome,
                    onShowError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}
