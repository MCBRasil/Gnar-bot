package xyz.gnarbot.gnar.commands.handlers

enum class Scope {
    GUILD {
        override fun checkPermission() {

        }
    },
    TEXT {
        override fun checkPermission() {

        }
    },
    VOICE {
        override fun checkPermission() {

        }
    };

    abstract fun checkPermission()
}