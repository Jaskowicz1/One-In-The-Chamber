package me.jaskowicz.oneinthechamber.Listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Settings.GameSettings;
import me.jaskowicz.oneinthechamber.Tasks.StartingGameTask;
import me.jaskowicz.oneinthechamber.Utils.Game;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class OnPlayerBlockInteract implements Listener {

    private Plugin plugin = OneInTheChamber.getPlugin(OneInTheChamber.class);

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        if(user.getGameIn() != null) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST_MINECART) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.TRAPPED_CHEST) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BARREL) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DISPENSER) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DROPPER) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CAULDRON) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.FURNACE) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LOOM) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SMOKER) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CAMPFIRE) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.JUKEBOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENCHANTING_TABLE) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ANVIL) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BLACK_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BLUE_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BROWN_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CYAN_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GRAY_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GREEN_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIGHT_BLUE_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIGHT_GRAY_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIME_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.MAGENTA_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ORANGE_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.PINK_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.PURPLE_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.WHITE_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.YELLOW_SHULKER_BOX) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ITEM_FRAME) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BLAST_FURNACE) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ARMOR_STAND) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.STONECUTTER) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GRINDSTONE) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BELL) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.FLOWER_POT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BLACK_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BLUE_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BROWN_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CYAN_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GRAY_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GREEN_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIGHT_BLUE_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIGHT_GRAY_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LIME_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.MAGENTA_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ORANGE_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.PINK_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.PURPLE_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RED_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.WHITE_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.YELLOW_BED) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BEACON) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.BIRCH_BOAT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ACACIA_BOAT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.DARK_OAK_BOAT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.JUNGLE_BOAT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.OAK_BOAT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            } else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.SPRUCE_BOAT) {
                if(user.getGameIn() != null) {
                    event.setCancelled(true);
                }
            }
        }
    }
}