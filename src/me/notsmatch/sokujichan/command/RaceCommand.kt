package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.model.Race
import me.notsmatch.sokujichan.model.Spots
import me.notsmatch.sokujichan.service.GuildSettingsService
import me.notsmatch.sokujichan.service.SokujiService
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

class RaceCommand(val sokujiService: SokujiService) : Command() {

    init {
        this.name = "race"
        this.help = "順位を入力して得点を適用します"
        this.arguments = "<spot1> <spot2> <spot3>...<spot6>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            val sokuji = sokujiService.getSokuji(guild.idLong, channel.idLong) ?: return reply("即時集計は開始されていません")

            if(sokuji.getRacesLeft() == 0) return reply("レースは終了しました")

            if(args.size >= 6) {
                val dataA = mutableListOf<Int>()
                val dataB = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

                args.forEach {
                    if(!NumberUtils.isNumber(it)){
                        return@apply reply("整数を入力してください")
                    }
                    if(it.toInt() > 12 || it.toInt() < 1){
                        return@apply reply("順位は1 ~ 12で入力してください")
                    }
                    if(dataA.contains(it.toInt())){
                        return@apply reply("順位が重複しています")
                    }
                    dataA.add(it.toInt())
                }

                dataB.removeAll(dataA)

                sokuji.races.add(Race(Spots(dataA.toTypedArray()), Spots(dataB.toTypedArray())))

                sokuji.save()

                sokuji.send()

            }else{
                reply("``_race <spot1> <spot2> <spot3>...<spot6>``")
            }
        }
    }
}