package xyz.gnarbot.gnar.tests

import org.junit.Test
import xyz.gnarbot.gnar.utils.YouTube

class KotlinTests {
    @Test
    fun `YouTube utilities`() {
        YouTube.search("necrodancer 5-1", 3).forEach(::println)
    }
}