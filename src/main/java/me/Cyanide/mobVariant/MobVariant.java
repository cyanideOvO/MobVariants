
package me.Cyanide.mobVariant;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MobVariant extends JavaPlugin implements CommandExecutor {

    private VariantEggListener variantEggListener;
    private MobMenu mobMenu;

    @Override
    public void onEnable() {
        // Create the plugin's data folder
        if (!getDataFolder().exists() && !getDataFolder().mkdirs()) {
            getLogger().severe("Failed to create MobVariant plugin folder!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Plugin data folder: " + getDataFolder().getAbsolutePath());

        // Load the default config.yml from the JAR
        saveDefaultConfig();

        // Register custom spawn egg listener
        variantEggListener = new VariantEggListener(this);
        getServer().getPluginManager().registerEvents(
                variantEggListener,
                this
        );

        // Initialize and register GUI menu
        mobMenu = new MobMenu(this, variantEggListener);
        getServer().getPluginManager().registerEvents(
                mobMenu,
                this
        );

        // Register command
        if (getCommand("mobvariant") == null) {
            getLogger().severe("Command 'mobvariant' is missing from plugin.yml!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("mobvariant").setExecutor(this);

        getLogger().info("MobVariant enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MobVariant disabled!");
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        if (!player.hasPermission("mobvariant.use")) {
            player.sendMessage("§cYou don't have permission to use this command.");
            return true;
        }

        mobMenu.openMainMenu(player);
        return true;
    }

    public VariantEggListener getVariantEggListener() {
        return variantEggListener;
    }

    public MobMenu getMobMenu() {
        return mobMenu;
    }
}