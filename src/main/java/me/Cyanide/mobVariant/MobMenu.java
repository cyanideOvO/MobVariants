
package me.Cyanide.mobVariant;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class MobMenu implements Listener {

    private static final int MENU_SIZE = 27;
    private static final int BACK_SLOT = 22;
    private static final int EXIT_SLOT = 26;

    private final VariantEggListener variantEggListener;
    private final JavaPlugin plugin;

    // Players who intentionally selected the Exit button.
    private final Set<UUID> exitingPlayers = new HashSet<>();

    // Prevent duplicate reopen tasks for the same player.
    private final Set<UUID> reopeningPlayers = new HashSet<>();

    public MobMenu(JavaPlugin plugin, VariantEggListener variantEggListener) {
        this.plugin = plugin;
        this.variantEggListener = variantEggListener;
    }

    /**
     * Opens the main mob selection menu.
     */
    public void openMainMenu(Player player) {
        MenuHolder holder = new MenuHolder(MenuType.MOBS, null);

        Inventory inventory = Bukkit.createInventory(
                holder,
                MENU_SIZE,
                ChatColor.DARK_GREEN + "Mob Variant Selector"
        );

        holder.setInventory(inventory);

        int slot = 0;

        for (MobRegistry.MobDefinition mob : MobRegistry.getMobs()) {
            if (slot >= BACK_SLOT) {
                break;
            }

            ItemStack item = new ItemStack(mob.icon());
            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(ChatColor.GREEN + mob.name());
                meta.setLore(List.of(
                        ChatColor.GRAY + "Available variants: "
                                + mob.variants().size(),
                        ChatColor.YELLOW + "Click to view variants"
                ));

                item.setItemMeta(meta);
            }

            inventory.setItem(slot, item);
            slot++;
        }

        setExitButton(inventory);
        player.openInventory(inventory);
    }

    /**
     * Opens the variant selection menu for a specific mob.
     */
    public void openVariantMenu(Player player, String mobId) {
        MobRegistry.MobDefinition mob = MobRegistry.getMob(mobId);

        if (mob == null) {
            return;
        }

        MenuHolder holder = new MenuHolder(MenuType.VARIANTS, mob.id());

        Inventory inventory = Bukkit.createInventory(
                holder,
                MENU_SIZE,
                ChatColor.DARK_GREEN + mob.name() + " Variants"
        );

        holder.setInventory(inventory);

        int slot = 0;

        for (String variant : mob.variants()) {
            if (slot >= BACK_SLOT) {
                break;
            }

            ItemStack item = new ItemStack(
                    getVariantIcon(mob.id(), variant)
            );

            ItemMeta meta = item.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(
                        ChatColor.GREEN + formatName(variant)
                );

                meta.setLore(List.of(
                        ChatColor.GRAY + "Mob: " + mob.name(),
                        ChatColor.GRAY + "Variant: " + formatName(variant),
                        "",
                        ChatColor.YELLOW + "Click to receive spawn egg"
                ));

                item.setItemMeta(meta);
            }

            inventory.setItem(slot, item);
            slot++;
        }

        inventory.setItem(
                BACK_SLOT,
                createItem(
                        Material.ARROW,
                        ChatColor.YELLOW + "Back",
                        ChatColor.GRAY + "Return to mob selection"
                )
        );

        setExitButton(inventory);
        player.openInventory(inventory);
    }

    /**
     * Adds an Exit button to the menu.
     */
    private void setExitButton(Inventory inventory) {
        inventory.setItem(
                EXIT_SLOT,
                createItem(
                        Material.BARRIER,
                        ChatColor.RED + "Exit",
                        ChatColor.GRAY + "Close the menu"
                )
        );
    }

    /**
     * Handles clicks in the plugin's menus.
     */
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory topInventory = event.getView().getTopInventory();

        if (!(topInventory.getHolder(false) instanceof MenuHolder holder)) {
            return;
        }

        // Prevent moving items into or out of the menu.
        event.setCancelled(true);

        // Ignore clicks outside the top menu inventory.
        if (event.getClickedInventory() == null
                || !event.getClickedInventory().equals(topInventory)) {
            return;
        }

        int slot = event.getRawSlot();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType().isAir()) {
            return;
        }

        // Exit button.
        if (slot == EXIT_SLOT) {
            exitingPlayers.add(player.getUniqueId());
            reopeningPlayers.remove(player.getUniqueId());
            player.closeInventory();
            return;
        }

        if (holder.getType() == MenuType.MOBS) {
            handleMobSelection(player, slot);
        } else if (holder.getType() == MenuType.VARIANTS) {
            handleVariantSelection(
                    player,
                    holder.getMobId(),
                    slot
            );
        }
    }

    private void handleMobSelection(Player player, int slot) {
        List<MobRegistry.MobDefinition> mobs = MobRegistry.getMobs();

        if (slot < 0 || slot >= mobs.size()) {
            return;
        }

        MobRegistry.MobDefinition selectedMob = mobs.get(slot);
        openVariantMenu(player, selectedMob.id());
    }

    private void handleVariantSelection(
            Player player,
            String mobId,
            int slot
    ) {
        if (slot == BACK_SLOT) {
            openMainMenu(player);
            return;
        }

        MobRegistry.MobDefinition mob = MobRegistry.getMob(mobId);

        if (mob == null) {
            return;
        }

        List<String> variants = mob.variants();

        if (slot < 0 || slot >= variants.size()) {
            return;
        }

        String selectedVariant = variants.get(slot);

        variantEggListener.giveEgg(
                player,
                mob.id(),
                selectedVariant
        );

        // Intentionally keep the variant menu open.
    }
    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof MenuHolder)) {
            return;
        }

        UUID playerId = event.getPlayer().getUniqueId();
        exitingPlayers.remove(playerId);
        reopeningPlayers.remove(playerId);
    }

    //Add Mobs Variants Here
    private Material getVariantIcon(String mobId, String variant) {
        return switch (mobId.toLowerCase(Locale.ROOT)) {

            case "wolf" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "PALE" -> Material.WHITE_WOOL;
                case "SPOTTED" -> Material.YELLOW_WOOL;
                case "SNOWY" -> Material.LIGHT_BLUE_WOOL;
                case "BLACK" -> Material.BLACK_WOOL;
                case "CHESTNUT" -> Material.BROWN_WOOL;
                case "RUSTY" -> Material.ORANGE_WOOL;
                case "ASHEN" -> Material.GRAY_WOOL;
                case "WOODS" -> Material.GREEN_WOOL;
                case "STRIPED" -> Material.PINK_WOOL;
                default -> Material.WOLF_SPAWN_EGG;
            };

            case "pig" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "TEMPERATE" -> Material.PINK_WOOL;
                case "COLD" -> Material.LIGHT_BLUE_WOOL;
                case "WARM" -> Material.ORANGE_WOOL;
                default -> Material.PIG_SPAWN_EGG;
            };

            case "cow" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "TEMPERATE" -> Material.BROWN_WOOL;
                case "COLD" -> Material.LIGHT_GRAY_WOOL;
                case "WARM" -> Material.ORANGE_WOOL;
                default -> Material.COW_SPAWN_EGG;
            };

            case "chicken" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "TEMPERATE" -> Material.WHITE_WOOL;
                case "COLD" -> Material.LIGHT_BLUE_WOOL;
                case "WARM" -> Material.ORANGE_WOOL;
                default -> Material.CHICKEN_SPAWN_EGG;
            };

            case "cat" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "TABBY" -> Material.ORANGE_WOOL;
                case "BLACK" -> Material.BLACK_WOOL;
                case "RED" -> Material.RED_WOOL;
                case "SIAMESE" -> Material.BROWN_WOOL;
                case "BRITISH_SHORTHAIR" -> Material.GRAY_WOOL;
                case "CALICO" -> Material.ORANGE_WOOL;
                case "PERSIAN" -> Material.WHITE_WOOL;
                case "RAGDOLL" -> Material.LIGHT_GRAY_WOOL;
                case "WHITE" -> Material.WHITE_WOOL;
                case "JELLIE" -> Material.PINK_WOOL;
                case "ALL_BLACK" -> Material.BLACK_WOOL;
                default -> Material.CAT_SPAWN_EGG;
            };

            case "rabbit" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "BROWN" -> Material.BROWN_WOOL;
                case "WHITE" -> Material.WHITE_WOOL;
                case "BLACK" -> Material.BLACK_WOOL;
                case "WHITE_SPLOTCHED" -> Material.WHITE_TERRACOTTA;
                case "GOLD" -> Material.YELLOW_WOOL;
                case "SALT_AND_PEPPER" -> Material.GRAY_WOOL;
                case "THE_KILLER_BUNNY" -> Material.RED_WOOL;
                case "TOAST" -> Material.BLACK_TERRACOTTA;
                default -> Material.RABBIT_SPAWN_EGG;
            };

            case "villager" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "PLAINS" -> Material.LIME_WOOL;
                case "DESERT" -> Material.YELLOW_WOOL;
                case "JUNGLE" -> Material.GREEN_WOOL;
                case "SAVANNA" -> Material.ORANGE_WOOL;
                case "SNOW" -> Material.WHITE_WOOL;
                case "SWAMP" -> Material.BROWN_WOOL;
                case "TAIGA" -> Material.SPRUCE_PLANKS;
                default -> Material.VILLAGER_SPAWN_EGG;
            };

            case "axolotl" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "LUCY" -> Material.PINK_WOOL;
                case "WILD" -> Material.BROWN_WOOL;
                case "GOLD" -> Material.YELLOW_WOOL;
                case "CYAN" -> Material.CYAN_WOOL;
                case "BLUE" -> Material.BLUE_WOOL;
                default -> Material.AXOLOTL_SPAWN_EGG;
            };

            case "sheep" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "WHITE" -> Material.WHITE_WOOL;
                case "ORANGE" -> Material.ORANGE_WOOL;
                case "MAGENTA" -> Material.MAGENTA_WOOL;
                case "LIGHT_BLUE" -> Material.LIGHT_BLUE_WOOL;
                case "YELLOW" -> Material.YELLOW_WOOL;
                case "LIME" -> Material.LIME_WOOL;
                case "PINK" -> Material.PINK_WOOL;
                case "GRAY" -> Material.GRAY_WOOL;
                case "LIGHT_GRAY" -> Material.LIGHT_GRAY_WOOL;
                case "CYAN" -> Material.CYAN_WOOL;
                case "PURPLE" -> Material.PURPLE_WOOL;
                case "BLUE" -> Material.BLUE_WOOL;
                case "BROWN" -> Material.BROWN_WOOL;
                case "GREEN" -> Material.GREEN_WOOL;
                case "RED" -> Material.RED_WOOL;
                case "BLACK" -> Material.BLACK_WOOL;
                case "JEB_" -> Material.PINK_GLAZED_TERRACOTTA;
                default -> Material.SHEEP_SPAWN_EGG;
            };

            case "parrot" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "RED_BLUE" -> Material.RED_WOOL;
                case "BLUE" -> Material.BLUE_WOOL;
                case "GREEN" -> Material.GREEN_WOOL;
                case "YELLOW_BLUE" -> Material.YELLOW_WOOL;
                case "GRAY" -> Material.GRAY_WOOL;
                default -> Material.PARROT_SPAWN_EGG;
            };
            case "fox" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "RED" -> Material.ORANGE_WOOL;
                case "SNOW" -> Material.WHITE_WOOL;
                default -> Material.FOX_SPAWN_EGG;
            };

            case "frog" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "TEMPERATE" -> Material.LIME_WOOL;
                case "WARM" -> Material.ORANGE_WOOL;
                case "COLD" -> Material.GREEN_WOOL;
                default -> Material.FROG_SPAWN_EGG;
            };

            case "mooshroom" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "RED" -> Material.RED_MUSHROOM;
                case "BROWN" -> Material.BROWN_MUSHROOM;
                default -> Material.MOOSHROOM_SPAWN_EGG;
            };

            case "llama" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "CREAMY" -> Material.WHITE_WOOL;
                case "WHITE" -> Material.WHITE_WOOL;
                case "BROWN" -> Material.BROWN_WOOL;
                case "GRAY" -> Material.GRAY_WOOL;
                default -> Material.LLAMA_SPAWN_EGG;
            };

            case "tropical_fish" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "KOB" -> Material.ORANGE_WOOL;
                case "SUNSTREAK" -> Material.YELLOW_WOOL;
                case "SNOOPER" -> Material.RED_WOOL;
                case "DASHER" -> Material.BLUE_WOOL;
                case "BRINELY" -> Material.CYAN_WOOL;
                case "SPOTTY" -> Material.LIME_WOOL;
                case "FLOPPER" -> Material.PINK_WOOL;
                case "STRIPEY" -> Material.MAGENTA_WOOL;
                case "GLITTER" -> Material.PURPLE_WOOL;
                case "BLOCKFISH" -> Material.BROWN_WOOL;
                case "BETTY" -> Material.LIGHT_BLUE_WOOL;
                case "CLAYFISH" -> Material.LIGHT_GRAY_WOOL;
                default -> Material.TROPICAL_FISH_SPAWN_EGG;
            };

            case "panda" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "NORMAL" -> Material.WHITE_WOOL;
                case "LAZY" -> Material.GRAY_WOOL;
                case "WORRIED" -> Material.LIGHT_BLUE_WOOL;
                case "PLAYFUL" -> Material.PINK_WOOL;
                case "AGGRESSIVE" -> Material.RED_WOOL;
                case "WEAK" -> Material.YELLOW_WOOL;
                case "BROWN" -> Material.BROWN_WOOL;
                default -> Material.PANDA_SPAWN_EGG;
            };

            case "salmon" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "SMALL" -> Material.SALMON;
                case "MEDIUM" -> Material.COOKED_SALMON;
                case "LARGE" -> Material.SALMON_SPAWN_EGG;
                default -> Material.SALMON_SPAWN_EGG;
            };

            case "shulker" -> switch (variant.toUpperCase(Locale.ROOT)) {
                case "WHITE" -> Material.WHITE_WOOL;
                case "ORANGE" -> Material.ORANGE_WOOL;
                case "MAGENTA" -> Material.MAGENTA_WOOL;
                case "LIGHT_BLUE" -> Material.LIGHT_BLUE_WOOL;
                case "YELLOW" -> Material.YELLOW_WOOL;
                case "LIME" -> Material.LIME_WOOL;
                case "PINK" -> Material.PINK_WOOL;
                case "GRAY" -> Material.GRAY_WOOL;
                case "LIGHT_GRAY" -> Material.LIGHT_GRAY_WOOL;
                case "CYAN" -> Material.CYAN_WOOL;
                case "PURPLE" -> Material.PURPLE_WOOL;
                case "BLUE" -> Material.BLUE_WOOL;
                case "BROWN" -> Material.BROWN_WOOL;
                case "GREEN" -> Material.GREEN_WOOL;
                case "RED" -> Material.RED_WOOL;
                case "BLACK" -> Material.BLACK_WOOL;
                default -> Material.SHULKER_SPAWN_EGG;
            };

            //Add Mobs Variants Here

            default -> Material.PAPER;
        };
    }

    private ItemStack createItem(
            Material material,
            String displayName,
            String loreLine
    ) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(List.of(loreLine));
            item.setItemMeta(meta);
        }

        return item;
    }

    /**
     * Formats identifiers such as CHESTNUT into Chestnut.
     */
    private String formatName(String name) {
        String[] words = name.toLowerCase(Locale.ROOT).split("_");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (!result.isEmpty()) {
                result.append(' ');
            }

            result.append(Character.toUpperCase(word.charAt(0)));

            if (word.length() > 1) {
                result.append(word.substring(1));
            }
        }

        return result.toString();
    }

    private enum MenuType {
        MOBS,
        VARIANTS
    }

    /**
     * Identifies the menu type and selected mob.
     */
    private static class MenuHolder implements InventoryHolder {

        private final MenuType type;
        private final String mobId;
        private Inventory inventory;

        private MenuHolder(MenuType type, String mobId) {
            this.type = type;
            this.mobId = mobId;
        }

        private void setInventory(Inventory inventory) {
            this.inventory = inventory;
        }

        private MenuType getType() {
            return type;
        }

        private String getMobId() {
            return mobId;
        }

        @Override
        public Inventory getInventory() {
            return inventory;
        }
    }
}