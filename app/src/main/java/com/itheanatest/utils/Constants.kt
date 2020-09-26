package com.itheanatest.utils

class Constants {
    interface ACTION {
        companion object {
            const val MAIN_ACTION = "com.marothiatechs.foregroundservice.action.main"
            const val INIT_ACTION = "com.marothiatechs.foregroundservice.action.init"
            const val PREV_ACTION = "com.marothiatechs.foregroundservice.action.prev"
            const val PLAY_ACTION = "com.marothiatechs.foregroundservice.action.play"
            const val NEXT_ACTION = "com.marothiatechs.foregroundservice.action.next"
            const val STARTFOREGROUND_ACTION =
                "com.marothiatechs.foregroundservice.action.startforeground"
            const val STOPFOREGROUND_ACTION =
                "com.marothiatechs.foregroundservice.action.stopforeground"
        }
    }

    interface NOTIFICATION_ID {
        companion object {
            const val FOREGROUND_SERVICE = 101
        }
    }
}