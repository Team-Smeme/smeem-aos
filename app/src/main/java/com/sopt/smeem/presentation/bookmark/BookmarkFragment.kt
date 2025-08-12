package com.sopt.smeem.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.sopt.smeem.presentation.compose.theme.SmeemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SmeemTheme {
                    BookmarkScreen(
                        onNavigateToDetail = { bookmarkId ->
                            // TODO: 상세 화면으로 이동 처리
                        }
                    )
                }
            }
        }
    }
}