package me.Cyanide.mobVariant;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Ageable;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class VariantEggListener implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey mobKey;
    private final NamespacedKey variantKey;

    public VariantEggListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mobKey = new NamespacedKey(plugin, "variant_egg_mob");
        this.variantKey = new NamespacedKey(plugin, "variant_egg_variant");
    }


    public void giveEgg(Player player, String mobId, String variantName) {
        MobRegistry.MobDefinition mob = MobRegistry.getMob(mobId);

        if (mob == null || variantName == null
                || !mob.variants().contains(variantName.toUpperCase(Locale.ROOT))) {
            return;
        }

        // GET EGG MATERIAL
        Material eggMaterial = getEggMaterial(mob.id());

        if (eggMaterial == null) {
            return;
        }

        ItemStack egg = new ItemStack(eggMaterial);
        ItemMeta meta = egg.getItemMeta();

        if (meta == null) {
            return;
        }

        // FORMAT DISPLAY NAMES
        String formattedVariant = formatVariantName(variantName);
        String formattedMob = formatMobName(mob.id());

        // SET EGG NAME
        meta.setDisplayName(
                "§b" + formattedVariant + " " + formattedMob + " Spawn Egg"
        );

        // SET EGG LORE
        meta.setLore(List.of(
                "§7Custom mob variant",
                "§7Mob: §f" + formattedMob,
                "§7Variant: §f" + formattedVariant,
                "",
                "§eRight-click a block to spawn"
        ));

        // SET ENCHANTMENT GLINT
        meta.setEnchantmentGlintOverride(true);

        meta.getPersistentDataContainer().set(
                mobKey,
                PersistentDataType.STRING,
                mob.id().toLowerCase(Locale.ROOT)
        );

        meta.getPersistentDataContainer().set(
                variantKey,
                PersistentDataType.STRING,
                variantName.toUpperCase(Locale.ROOT)
        );

        egg.setItemMeta(meta);

        // 4. ADD EGG TO PLAYER INVENTORY

        var leftovers = player.getInventory().addItem(egg);

        leftovers.values().forEach(item ->
                player.getWorld().dropItemNaturally(
                        player.getLocation(),
                        item
                )
        );
    }

    @EventHandler
    public void onUseVariantEgg(PlayerInteractEvent event) {

        // CHECK MAIN HAND
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        // CHECK RIGHT-CLICK BLOCK
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // GET HELD ITEM
        ItemStack item = event.getItem();

        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return;
        }

        String mobId = meta.getPersistentDataContainer().get(
                mobKey,
                PersistentDataType.STRING
        );

        String variantName = meta.getPersistentDataContainer().get(
                variantKey,
                PersistentDataType.STRING
        );

        // IGNORE NORMAL EGGS AND UNRELATED ITEMS
        if (mobId == null || variantName == null) {
            return;
        }

        // CANCEL VANILLA EGG BEHAVIOR
        event.setCancelled(true);


        MobRegistry.MobDefinition mob = MobRegistry.getMob(mobId);

        if (mob == null
                || !mob.variants().contains(variantName.toUpperCase(Locale.ROOT))) {
            return;
        }

        // CHECK MATCHING EGG MATERIAL
        Material expectedEgg = getEggMaterial(mob.id());

        if (expectedEgg == null || item.getType() != expectedEgg) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) {
            return;
        }

        // GET ADJACENT BLOCK
        Block targetBlock = clickedBlock.getRelative(event.getBlockFace());
        Block headBlock = targetBlock.getRelative(0, 1, 0);
        Block floorBlock = targetBlock.getRelative(0, -1, 0);

        if (!targetBlock.isPassable()
                || !headBlock.isPassable()
                || !floorBlock.getType().isSolid()) {
            return;
        }

        Location spawnLocation = targetBlock.getLocation().add(
                0.5,
                0,
                0.5
        );

        Player player = event.getPlayer();

        spawnLocation.setYaw(player.getLocation().getYaw());
        spawnLocation.setPitch(0);


        boolean spawned = MobRegistry.spawn(
                player,
                mob.id(),
                variantName,
                spawnLocation
        );

        if (!spawned) {
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE) {
            int amount = item.getAmount();

            if (amount <= 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                item.setAmount(amount - 1);
            }
        }
    }

    @EventHandler
    public void onUseVariantEggOnMob(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }

        String mobId = meta.getPersistentDataContainer().get(
                mobKey, PersistentDataType.STRING
        );

        String variantName = meta.getPersistentDataContainer().get(
                variantKey, PersistentDataType.STRING
        );

        if (mobId == null || variantName == null) {
            return;
        }

        MobRegistry.MobDefinition mob = MobRegistry.getMob(mobId);
        if (mob == null
                || !mob.variants().contains(variantName.toUpperCase(Locale.ROOT))) {
            return;
        }

        Material expectedEgg = getEggMaterial(mob.id());
        if (expectedEgg == null || item.getType() != expectedEgg) {
            return;
        }

        // Only apply baby-spawn behavior when clicking an adult of the same mob.
        if (!(event.getRightClicked() instanceof Ageable target)
                || !target.isAdult()
                || !target.getType().name().equalsIgnoreCase(mob.id())) {
            return;
        }

        event.setCancelled(true);

        Player player = event.getPlayer();

        // Spawn beside the clicked adult.
        Location spawnLocation = target.getLocation().clone().add(1.0, 0, 0);
        spawnLocation.setYaw(player.getLocation().getYaw());
        spawnLocation.setPitch(0);

        boolean spawned = MobRegistry.spawn(
                player,
                mob.id(),
                variantName,
                spawnLocation
        );

        if (!spawned) {
            return;
        }

        // Find the newly spawned mob and make it a baby.
        target.getWorld().getNearbyEntities(
                        spawnLocation,
                        2.0, 2.0, 2.0
                ).stream()
                .filter(entity -> entity instanceof Ageable)
                .filter(entity -> entity.getType() == target.getType())
                .map(entity -> (Ageable) entity)
                .filter(Ageable::isAdult)
                .min(Comparator.comparingDouble(
                        entity -> entity.getLocation().distanceSquared(spawnLocation)
                ))
                .ifPresent(Ageable::setBaby);

        // Consume one egg outside Creative mode.
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (item.getAmount() <= 1) {
                player.getInventory().setItemInMainHand(null);
            } else {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }
    private Material getEggMaterial(String mobId) {
        if (mobId == null) {
            return null;
        }

        return switch (mobId.toLowerCase(Locale.ROOT)) {
            case "wolf" -> Material.WOLF_SPAWN_EGG;
            case "pig" -> Material.PIG_SPAWN_EGG;
            case "cow" -> Material.COW_SPAWN_EGG;
            case "chicken" -> Material.CHICKEN_SPAWN_EGG;
            case "cat" -> Material.CAT_SPAWN_EGG;
            case "rabbit" -> Material.RABBIT_SPAWN_EGG;
            case "horse" -> Material.HORSE_SPAWN_EGG;
            case "villager" -> Material.VILLAGER_SPAWN_EGG;
            case "axolotl" -> Material.AXOLOTL_SPAWN_EGG;
            case "sheep" -> Material.SHEEP_SPAWN_EGG;
            case "parrot" -> Material.PARROT_SPAWN_EGG;
            case "fox" -> Material.FOX_SPAWN_EGG;
            case "frog" -> Material.FROG_SPAWN_EGG;
            case "mooshroom" -> Material.MOOSHROOM_SPAWN_EGG;
            case "llama" -> Material.LLAMA_SPAWN_EGG;
            case "tropical_fish" -> Material.TROPICAL_FISH_SPAWN_EGG;
            case "panda" -> Material.PANDA_SPAWN_EGG;
            case "salmon" -> Material.SALMON_SPAWN_EGG;
            case "shulker" -> Material.SHULKER_SPAWN_EGG;

            // ADD NEW MOB EGG MATERIALS HERE

            default -> null;
        };
    }

    private String formatVariantName(String variantName) {
        return capitalizeWords(
                variantName.toLowerCase(Locale.ROOT).replace('_', ' ')
        );
    }

    private String formatMobName(String mobId) {
        return capitalizeWords(
                mobId.toLowerCase(Locale.ROOT).replace('_', ' ')
        );
    }

    private String capitalizeWords(String text) {
        StringBuilder result = new StringBuilder();

        for (String word : text.split("\\s+")) {
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
}