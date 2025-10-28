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
    val bookmarkViewModel by viewModels<BookmarkViewModel>()

    companion object {
        private const val ARG_URL = "arg_url"

        fun newInstance(url: String? = null): BookmarkFragment {
            return BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URL, url)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val urlFromArgs = arguments?.getString(ARG_URL)

        return ComposeView(requireContext()).apply {
            setContent {

                SmeemTheme {
                    val navController = rememberNavController()
                    this@BookmarkFragment.navController = navController
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val bookmarkState by bookmarkViewModel.collectAsState()

                    // Navigation callback setup and state management
                    LaunchedEffect(navBackStackEntry) {
                        val currentRoute = navBackStackEntry?.destination?.route

                        // Reset navigation flag when returning to bookmark list
                        if (currentRoute == BookmarkRoute.BookmarkList::class.qualifiedName) {
                            bookmarkViewModel.resetNavigatingFlag()
                        }

                        bookmarkViewModel.setOnNavigateToDetailCallback { bookmarkId ->
                            navController.navigate(BookmarkRoute.BookmarkDetail(bookmarkId))
                        }
                    }

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

                    val initialRoute = if (urlFromArgs != null) {
                        BookmarkRoute.BookmarkDetailFromUrl(urlFromArgs)
                    } else {
                        BookmarkRoute.BookmarkList
                    }

                    BookmarkNavHost(
                        navController = navController,
                        bookmarkViewModel = bookmarkViewModel,
                        initialRoute = initialRoute,
                        onNavigateToHome = {
                            navController.popBackStack(BookmarkRoute.BookmarkList, false)
                            (activity as? MainActivity)?.navigateToHome()
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.showBottomNavigation()
    }

}
