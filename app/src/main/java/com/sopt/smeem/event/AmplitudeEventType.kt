package com.sopt.smeem.event

enum class AmplitudeEventType(val eventName: String) {
    FIRST_VIEW("first_view"),

    // on boarding
    ON_BOARDING_GOAL_VIEW("onboarding_goal_view"),
    ON_BOARDING_ALARM_VIEW("onboarding_alarm_view"),
    ON_BOARDING_LATER_CLICK("onboarding_later_click"),
    ON_BOARDING_PLAN_VIEW("onboarding_plan_view"),
    SIGN_UP_SUCCESS("signup_success"),

    // home
    HOME_VIEW("home_view"),
    FULL_CALENDAR_APPEAR("full_calendar_appear"),

    // diary
    FOR_WRITING_CLICK("for_writing_click"),
    KOR_WRITING_CLICK("kor_writing_click"),
    FIRST_STEP_COMPLETE("first_step_complete"),
    SECOND_STEP_COMPLETE("sec_step_complete"),
    DIARY_COMPLETE("diary_complete"),
    HINT_CLICK("hint_click"),

    // diary detail
    MY_DIARY_CLICK("mydiary_click"),
    MY_DIARY_EDIT("mydiary_edit"),
    MY_DIARY_EDIT_COMPLETE_CLICK("mydiary_edit_complete_click"),
    MY_DIARY_TOGGLE_CLICK("toggle_click"),

    // my page
    MY_PAGE_VIEW("mypage_view"),
    ACHIEVEMENT_VIEW("achievement_view"),
    BADGE_BOTTOM_SHEET_VIEW("badge_bottom_sheet_view"),

    // badge
    BADGE_MORE_CLICK("badge_more_click"),
    WELCOME_QUIT_CLICK("welcome_quit_click"),
    WELCOME_MORE_CLICK("welcome_more_click"),

    // delete account
    DELETE_ID_TRY("delete_id_try"),
    DELETE_ID_DONE("delete_id_done"),

    // banner
    BANNER_CLICK("banner_click"),
    BANNER_X("banner_x"),

    // coach
    COACHING_TRY_CLICK("coaching_try_click"),
    COACHING_EXIT_CLICK("coaching_exit_click"),
    COACHING_LOAD_VIEW("coaching_load_view"),
    COACHING_RESULT_VIEW("coaching_result_view"),
    COACHING_FEEDBACK_VIEW("coaching_feedback_view"),
}
