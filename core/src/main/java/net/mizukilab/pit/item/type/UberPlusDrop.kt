package net.mizukilab.pit.item.type

import cn.charlotte.pit.ThePit
import net.minecraft.server.v1_8_R3.EnumParticle
import net.mizukilab.pit.item.AbstractPitItem
import net.mizukilab.pit.util.ParticleBuilder
import net.mizukilab.pit.util.PlayerUtil
import net.mizukilab.pit.util.RandomList
import net.mizukilab.pit.util.chat.CC
import net.mizukilab.pit.util.item.ItemBuilder
import net.mizukilab.pit.util.item.ItemUtil
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

/**
 * @author Araykal
 * @since 2025/5/4
 */
class UberPlusDrop : AbstractPitItem(), Listener {

    private val randomList = RandomList(
        "excalibur" to 10,
        "hydrofoil" to 10,
    )
    override fun getInternalName(): String {
        return "uber_plus_drop"
    }

    override fun getItemDisplayName(): String {
        return "&c不朽登峰造极掉落物"
    }

    override fun getItemDisplayMaterial(): Material {
        return Material.ENDER_CHEST
    }

    override fun toItemStack(): ItemStack {
        return ItemBuilder(Material.ENDER_CHEST).enchantment(Enchantment.THORNS).flags(ItemFlag.HIDE_ENCHANTS)
            .internalName(internalName).name(itemDisplayName).dontStack().deathDrop(false).build()
    }
    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val item = event.item ?: return

        if (internalName == ItemUtil.getInternalName(item)) {

            event.isCancelled = true
            event.setUseInteractedBlock(Event.Result.DENY)
            event.setUseItemInHand(Event.Result.DENY)
        }
    }
    override fun loadFromItemStack(item: ItemStack?) {
    }
}