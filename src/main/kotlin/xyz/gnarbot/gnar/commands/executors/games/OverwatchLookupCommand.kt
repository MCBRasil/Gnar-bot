package xyz.gnarbot.gnar.commands.executors.games

import com.mashape.unirest.http.Unirest
import org.json.JSONObject
import xyz.gnarbot.gnar.commands.handlers.Command
import xyz.gnarbot.gnar.commands.handlers.CommandExecutor
import xyz.gnarbot.gnar.utils.Note
import java.awt.Color

@Command(aliases = arrayOf("overwatch", "ow"),
        usage = "(BattleTag#0000) [region]",
        description = "Look up Overwatch information about a player.")
class OverwatchLookupCommand : CommandExecutor() {
    private val regions = arrayOf("us", "eu", "kr")

    public override fun execute(note: Note, args: List<String>) {
        if (args.isEmpty()) {
            note.respond().error("Insufficient arguments. `${meta.usage}`.").queue()
            return
        }

        if (!args[0].matches("""[a-zA-Z1-9]+[#-]\d+""".toRegex())) {
            note.respond().error("You did not enter a valid BattleTag `[BattleTag#0000]`.").queue()
            return
        }

        val tag = args[0].replace('#', '-')

        var region: String? = null
        if (args.size > 1) {
            for (r in regions) {
                if (args[1].equals(r, ignoreCase = true)) {
                    region = r
                    break
                }
            }
            if (region == null) {
                note.respond().error("Invalid region provided. `[us, eu, kr]`").queue()
                return
            }
        }

        val response = Unirest.get("https://owapi.net/api/v3/u/{tag}/stats")
                .routeParam("tag", tag)
                .asJson()
                .body
                .`object`

        var jso: JSONObject? = null

        // Region arg provided.
        if (region != null) {
            if (!response.has(region)) {
                note.respond().error("Unable to find Overwatch player `" + tag + "` in region `" + region.toUpperCase() + "`.").queue()
                return
            }

            jso = response.optJSONObject(region)
        } else {
            for (r in regions) {
                if (!response.has(r)) {
                    continue
                }

                jso = response.optJSONObject(r)
                region = r
                break
            }

            if (jso == null || region == null) {
                note.respond().error("Unable to find Overwatch player `$tag`.").queue()
                return
            }
        }// Region arg not provided. Search for first non-null region.

        note.respond().embed("Overwatch Stats: $tag") {
            description {
                appendln("Battle Tag: **__[$tag](https://playoverwatch.com/en-gb/career/pc/$region/$tag)__**")
                appendln("Region: **__[${region!!.toUpperCase()}](http://masteroverwatch.com/leaderboards/pc/$region/mode/ranked/category/skillrating)__**")
            }

            val overall = jso?.optJSONObject("stats")

            if (overall == null) {
                note.respond().error("Unable to find statistics for Overwatch player`$tag`.").queue()
                return
            }

            var avatar: String? = null
            var eliminations = 0
            var medals = 0
            var dmg_done = 0
            var cards = 0

            overall.optJSONObject("quickplay")?.let {
                it.optJSONObject("overall_stats")?.let {
                    field("General") {
                        append("Level: **[${it.optInt("prestige") * 100 + it.optInt("level")}]()**")
                        avatar = it.optString("avatar")
                    }
                }
                field("Quick Play", true) {
                    it.optJSONObject("average_stats")?.let {
                        appendln("Avg. Elims: **[${it.optDouble("eliminations_avg")}]()**")
                        appendln("Avg. Deaths: **[${it.optDouble("deaths_avg")}]()**")
                    }

                    it.optJSONObject("overall_stats")?.let {
                        appendln("Wins: **[${it.optInt("wins")}]()**")
                    }

                    it.optJSONObject("game_stats")?.let {
                        appendln("K/D Ratio: **[${it.optDouble("kpd")}]()**")
                        appendln("Played for: **[${it.optInt("time_played")} hours]()**")

                        eliminations += it.optDouble("eliminations").toInt()
                        medals += it.optDouble("medals").toInt()
                        dmg_done += it.optDouble("damage_done").toInt()
                        cards += it.optDouble("cards").toInt()
                    }
                }
            }

            var sideColor: Color? = null
            var rankName: String? = null

            overall.optJSONObject("competitive")?.let {
                field("Competitive", true) {

                    val cp_avg = it.optJSONObject("average_stats")

                    if (cp_avg != null) {
                        appendln("Avg. Elims: **[" + cp_avg.optDouble("eliminations_avg") + "]()**")
                        appendln("Avg. Deaths: **[" + cp_avg.optDouble("deaths_avg") + "]()**")
                    }

                    it.optJSONObject("game_stats").let {
                        appendln("Wins/Draws/Loses: **["
                                + it.optInt("games_won") + "]()** | **["
                                + it.optInt("games_tied") + "]()** | **["
                                + it.optInt("games_lost") + "]()**")
                        appendln("K/D Ratio: **[" + it.optDouble("kpd") + "]()**")
                        appendln("Played for: **[" + it.optInt("time_played") + " hours]()**")

                        eliminations += it.optDouble("eliminations").toInt()
                        medals += it.optDouble("medals").toInt()
                        dmg_done += it.optDouble("damage_done").toInt()
                        cards += it.optDouble("cards").toInt()
                    }

                    it.optJSONObject("overall_stats")?.let {
                        val rank = it.optInt("comprank")

                        if (rank < 1500) {
                            sideColor = Color(150, 90, 56)
                            rankName = "Bronze"
                        } else if (rank in 1500..1999) {
                            sideColor = Color(168, 168, 168)
                            rankName = "Silver"
                        } else if (rank in 2000..2499) {
                            sideColor = Color(201, 137, 16)
                            rankName = "Gold"
                        } else if (rank in 2500..2999) {
                            sideColor = Color(229, 228, 226)
                            rankName = "Platinum"
                        } else if (rank in 3000..3499) {
                            sideColor = Color(63, 125, 255)
                            rankName = "Diamond"
                        } else if (rank in 3500..3999) {
                            sideColor = Color(255, 184, 12)
                            rankName = "Master"
                        } else if (rank >= 4000) {
                            sideColor = Color(238, 180, 255)
                            rankName = "Grand Master"
                        }

                        appendln("Comp. Rank: **[:beginner: $rank]() ($rankName)**")
                    }
                }
            }

            field("Overall", false) {
                appendln("Eliminations: **[$eliminations]()**")
                appendln("Medals: **[$medals]()**")
                appendln("Total Damage: **[$dmg_done]()**")
                appendln("Cards: **[$cards]()**")
            }

            color = sideColor
            thumbnail = avatar
        }.rest().queue()
    }
}
