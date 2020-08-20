package me.jaskowicz.oneinthechamber.Listeners;

import me.jaskowicz.oneinthechamber.OneInTheChamber;
import me.jaskowicz.oneinthechamber.Utils.User;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class OnInventoryClick implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        User user = OneInTheChamber.USERS.get(player.getUniqueId());

        ItemStack clicked = event.getCurrentItem();
        Inventory inv = event.getInventory();
        InventoryView invview = event.getView();
        String invname = event.getView().getTitle();

        if(clicked == null) {
            return;
        }

        if(clicked.getItemMeta() == null) {
            return;
        }

        if(user.getGameIn() != null) {
            if(invname.contains("Your Statistics")) {
                event.setCancelled(true);
            } else if (invname.contains("Arrow Trails")) {
                event.setCancelled(true);
                if (clicked.getItemMeta().getDisplayName().contains("Fire Trail")) {
                    if (player.hasPermission("oneinthechamber.firetrail")) {
                        if (!user.hasTrailOn() && !user.hasFireTrailOn() || user.hasTrailOn() && user.hasFireTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasFireTrailOn()) {
                                user.setHasFireTrailOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Fire trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setHasFireTrailOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Fire trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Water Trail")) {
                    if (player.hasPermission("oneinthechamber.watertrail")) {
                        if (!user.hasTrailOn() && !user.hasWaterTrailOn() || user.hasTrailOn() && user.hasWaterTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasWaterTrailOn()) {
                                user.setHasWaterTrailOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Water trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setHasWaterTrailOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Water trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Lava Trail")) {
                    if (player.hasPermission("oneinthechamber.lavatrail")) {
                        if (!user.hasTrailOn() && !user.hasLavaTrailOn() || user.hasTrailOn() && user.hasLavaTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasLavaTrailOn()) {
                                user.setHasLavaTrailOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Lava trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setHasLavaTrailOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Lava trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Note Trail")) {
                    if (player.hasPermission("oneinthechamber.notetrail")) {
                        if (!user.hasTrailOn() && !user.hasNoteTrailOn() || user.hasTrailOn() && user.hasNoteTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasNoteTrailOn()) {
                                user.setHasNoteTrailOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Note trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setHasNoteTrailOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Note trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Heart Trail")) {
                    if (player.hasPermission("oneinthechamber.hearttrail")) {
                        if (!user.hasTrailOn() && !user.hasHeartTrailOn() || user.hasTrailOn() && user.hasHeartTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasHeartTrailOn()) {
                                user.setHasHeartTrailOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Heart trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setHasHeartTrailOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Heart trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Snow Trail")) {
                    if (player.hasPermission("oneinthechamber.snowtrail")) {
                        if (!user.hasTrailOn() && !user.hasSnowTrailOn() || user.hasTrailOn() && user.hasSnowTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasSnowTrailOn()) {
                                user.setHasSnowTrailOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Snow trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setHasSnowTrailOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Snow trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Blood Trail")) {
                    if (player.hasPermission("oneinthechamber.bloodtrail")) {
                        if (!user.hasTrailOn() && !user.hasBloodTrailOn() || user.hasTrailOn() && user.hasBloodTrailOn()) {
                            player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 5.0F, 1F);
                            if (!user.hasBloodTrailOn()) {
                                user.setBloodTrailCosmeticOn(true);
                                user.setHasTrailOn(true);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Blood trail: " + ChatColor.GREEN + "Enabled");
                            } else {
                                user.setBloodTrailCosmeticOn(false);
                                user.setHasTrailOn(false);
                                player.sendMessage(OneInTheChamber.prefix + ChatColor.GRAY + "Blood trail: " + ChatColor.RED + "Disabled");
                            }
                        } else {
                            player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You already have a trail on!");
                        }
                    } else {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 5.0F, 0.4F);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "You lack the permission to use this particle.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Clear arrow trails")) {
                    event.setCancelled(true);
                    if (user.hasTrailOn()) {
                        user.setHasAngryTrailOn(false);
                        user.setHasEmeraldTrailOn(false);
                        user.setBloodTrailCosmeticOn(false);
                        user.setHasEnchantTrailOn(false);
                        user.setHasFireTrailOn(false);
                        user.setHasHeartTrailOn(false);
                        user.setHasLavaTrailOn(false);
                        user.setHasNoteTrailOn(false);
                        user.setHasSnowTrailOn(false);
                        user.setHasWaterTrailOn(false);
                        user.setHasPurpleTrailOn(false);
                        user.setHasTrailOn(false);
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "Your current arrow trail has been cleared.");
                        player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 5.0F, 0.4F);
                    } else if (!user.hasTrailOn()) {
                        player.sendMessage(OneInTheChamber.prefix + ChatColor.RED + "There are no arrow trails to be cleared.");
                    }
                } else if (clicked.getItemMeta().getDisplayName().contains("Exit")) {
                    event.setCancelled(true);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 5.0F, 0.6F);
                } else {
                    event.setCancelled(true);
                }
            } else {
                if(!user.getGameIn().hasStarted() && !user.getGameIn().isFinished()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
