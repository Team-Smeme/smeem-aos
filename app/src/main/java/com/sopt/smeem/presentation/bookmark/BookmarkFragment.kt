package com.sopt.smeem.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sopt.smeem.presentation.bookmark.navigation.BookmarkNavHost
import com.sopt.smeem.presentation.bookmark.navigation.BookmarkRoute
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import com.sopt.smeem.presentation.home.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    private var navController: NavController? = null
    private var pendingUrl: String? = null
    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        
        return ComposeView(requireContext()).apply {
            setContent {
                
                SmeemTheme {
                    val navController = rememberNavController()
                    this@BookmarkFragment.navController = navController
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val bookmarkState by bookmarkViewModel.collectAsState()

                    // 튜토리얼 상태에 따른 바텀 네비게이션 제어
                    LaunchedEffect(bookmarkState.shouldShowTutorial) {
                        val mainActivity = activity as? MainActivity
                        if (bookmarkState.shouldShowTutorial) {
                            mainActivity?.hideBottomNavigation()
                        } else {
                            mainActivity?.showBottomNavigation()
                        }
                    }

                    // 기존 네비게이션 상태 제어 (튜토리얼이 아닐 때만)
                    LaunchedEffect(navBackStackEntry, bookmarkState.shouldShowTutorial) {
                        if (!bookmarkState.shouldShowTutorial) {
                            val mainActivity = activity as? MainActivity
                            val route = navBackStackEntry?.destination?.route

                            when {
                                route == BookmarkRoute.BookmarkList::class.qualifiedName -> {
                                    mainActivity?.showBottomNavigation()
                                }
                                route?.startsWith("${BookmarkRoute.BookmarkDetail::class.qualifiedName}") == true -> {
                                    mainActivity?.hideBottomNavigation()
                                }
                                route?.startsWith("${BookmarkRoute.BookmarkDetailFromUrl::class.qualifiedName}") == true -> {
                                    mainActivity?.hideBottomNavigation()
                                }
                                route?.startsWith("${BookmarkRoute.BookmarkDiary::class.qualifiedName}") == true -> {
                                    mainActivity?.hideBottomNavigation()
                                }
                                else -> {
                                    mainActivity?.showBottomNavigation()
                                }
                            }
                        }
                    }
                    
                    val initialRoute = BookmarkRoute.BookmarkList
                    
                    BookmarkNavHost(
                        navController = navController,
                        bookmarkViewModel = bookmarkViewModel,
                        initialRoute = initialRoute,
                        onNavigateToHome = {
                            navController.popBackStack(BookmarkRoute.BookmarkList, false)
                            (activity as? MainActivity)?.navigateToHome()
                        }
                    )
                    
                    // NavController 준비 후 대기 중인 URL이 있다면 네비게이션
                    LaunchedEffect(navController, pendingUrl) {
                        pendingUrl?.let { url ->
                            navController.navigate(BookmarkRoute.BookmarkDetailFromUrl(url))
                            pendingUrl = null
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavigation()
    }
    
    fun navigateToUrlDetail(url: String) {
        if (navController != null) {
            navController?.navigate(BookmarkRoute.BookmarkDetailFromUrl(url))
        } else {
            pendingUrl = url
        }
    }
    
}
