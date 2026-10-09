package me.Cyanide.mobVariant;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Parrot;
import org.bukkit.DyeColor;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.TropicalFish;
import java.util.List;
import java.util.Locale;

public final class MobRegistry {

    private MobRegistry() {}

    // MOB DEFINITION
    public record MobDefinition(
            String id,
            String name,
            Material icon,
            List<String> variants
    ) {}

    private static final List<MobDefinition> MOBS = List.of(
            new MobDefinition(
                    "wolf",
                    "Wolf",
                    Material.WOLF_SPAWN_EGG,
                    List.of(
                            "PALE", "SPOTTED", "SNOWY",
                            "BLACK", "CHESTNUT", "RUSTY",
                            "ASHEN", "WOODS", "STRIPED"
                    )
            ),
            new MobDefinition(
                    "pig",
                    "Pig",
                    Material.PIG_SPAWN_EGG,
                    List.of("TEMPERATE", "COLD", "WARM")
            ),
            new MobDefinition(
                    "cow",
                    "Cow",
                    Material.COW_SPAWN_EGG,
                    List.of("TEMPERATE", "COLD", "WARM")
            ),
            new MobDefinition(
                    "chicken",
                    "Chicken",
                    Material.CHICKEN_SPAWN_EGG,
                    List.of("TEMPERATE", "COLD", "WARM")
            ),
            new MobDefinition(
                    "cat",
                    "Cat",
                    Material.CAT_SPAWN_EGG,
                    List.of(
                            "TABBY", "BLACK", "RED",
                            "SIAMESE", "BRITISH_SHORTHAIR",
                            "CALICO", "PERSIAN", "RAGDOLL",
                            "WHITE", "JELLIE", "ALL_BLACK"
                    )
            ),

            new MobDefinition(
                    "rabbit",
                    "Rabbit",
                    Material.RABBIT_SPAWN_EGG,
                    List.of(
                            "BROWN",
                            "WHITE",
                            "BLACK",
                            "WHITE_SPLOTCHED",
                            "GOLD",
                            "SALT_AND_PEPPER",
                            "THE_KILLER_BUNNY",
                            "TOAST"
                    )
            ),

            new MobDefinition(
                    "horse",
                    "Horse",
                    Material.HORSE_SPAWN_EGG,
                    List.of(
                            "WHITE",
                            "CREAMY",
                            "CHESTNUT",
                            "BROWN",
                            "BLACK",
                            "GRAY",
                            "DARK_BROWN"
                    )
            ),

            new MobDefinition(
                    "villager",
                    "Villager",
                    Material.VILLAGER_SPAWN_EGG,
                    List.of(
                            "PLAINS",
                            "DESERT",
                            "JUNGLE",
                            "SAVANNA",
                            "SNOW",
                            "SWAMP",
                            "TAIGA"
                    )
            ),

            new MobDefinition(
                    "axolotl",
                    "Axolotl",
                    Material.AXOLOTL_SPAWN_EGG,
                    List.of("LUCY", "WILD", "GOLD", "CYAN", "BLUE")
            ),

            new MobDefinition(
                    "sheep",
                    "Sheep",
                    Material.SHEEP_SPAWN_EGG,
                    List.of(
                            "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE",
                            "YELLOW", "LIME", "PINK", "GRAY",
                            "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE",
                            "BROWN", "GREEN", "RED", "BLACK",
                            "JEB_"
                    )
            ),

            new MobDefinition(
                    "parrot",
                    "Parrot",
                    Material.PARROT_SPAWN_EGG,
                    List.of(
                            "RED_BLUE",
                            "BLUE",
                            "GREEN",
                            "YELLOW_BLUE",
                            "GRAY"
                    )
            ),
            new MobDefinition(
                    "fox", "Fox", Material.FOX_SPAWN_EGG,
                    List.of("RED", "SNOW")
            ),
            new MobDefinition(
                    "frog", "Frog", Material.FROG_SPAWN_EGG,
                    List.of("TEMPERATE", "WARM", "COLD")
            ),
            new MobDefinition(
                    "mooshroom", "Mooshroom", Material.MOOSHROOM_SPAWN_EGG,
                    List.of("RED", "BROWN")
            ),
            new MobDefinition(
                    "llama", "Llama", Material.LLAMA_SPAWN_EGG,
                    List.of("CREAMY", "WHITE", "BROWN", "GRAY")
            ),
            new MobDefinition(
                    "tropical_fish", "Tropical Fish", Material.TROPICAL_FISH_SPAWN_EGG,
                    List.of(
                            "KOB", "SUNSTREAK", "SNOOPER", "DASHER",
                            "BRINELY", "SPOTTY", "FLOPPER", "STRIPEY",
                            "GLITTER", "BLOCKFISH", "BETTY", "CLAYFISH"
                    )
            ),
            new MobDefinition(
                    "panda", "Panda", Material.PANDA_SPAWN_EGG,
                    List.of(
                            "NORMAL", "LAZY", "WORRIED", "PLAYFUL",
                            "AGGRESSIVE", "WEAK", "BROWN"
                    )
            ),
            new MobDefinition(
                    "salmon", "Salmon", Material.SALMON_SPAWN_EGG,
                    List.of("SMALL", "MEDIUM", "LARGE")
            ),
            new MobDefinition(
                    "shulker", "Shulker", Material.SHULKER_SPAWN_EGG,
                    List.of(
                            "WHITE", "ORANGE", "MAGENTA", "LIGHT_BLUE",
                            "YELLOW", "LIME", "PINK", "GRAY",
                            "LIGHT_GRAY", "CYAN", "PURPLE", "BLUE",
                            "BROWN", "GREEN", "RED", "BLACK"
                    )
            )

            // ADD MORE MOB DEFINITIONS ABOVE

    );

    // GET ALL MOBS
    public static List<MobDefinition> getMobs() {
        return MOBS;
    }

    // GET MOB BY ID
    public static MobDefinition getMob(String id) {
        if (id == null) {
            return null;
        }

        return MOBS.stream()
                .filter(mob -> mob.id().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public static boolean spawn(
            Player player,
            String mobId,
            String variantName,
            Location spawnLocation
    ) {
        if (player == null
                || mobId == null
                || variantName == null
                || spawnLocation == null
                || spawnLocation.getWorld() == null) {
            return false;
        }

        MobDefinition mob = getMob(mobId);
        String normalizedVariant = variantName.toUpperCase(Locale.ROOT);

        if (mob == null || !mob.variants().contains(normalizedVariant)) {
            return false;
        }

        return switch (mob.id()) {
            case "wolf" -> spawnWolf(normalizedVariant, spawnLocation);
            case "pig" -> spawnPig(normalizedVariant, spawnLocation);
            case "cow" -> spawnCow(normalizedVariant, spawnLocation);
            case "chicken" -> spawnChicken(normalizedVariant, spawnLocation);
            case "cat" -> spawnCat(variantName, spawnLocation);
            case "rabbit" -> spawnRabbit(variantName, spawnLocation);
            case "horse" -> spawnHorse(variantName, spawnLocation);
            case "villager" -> spawnVillager(variantName, spawnLocation);
            case "axolotl" -> spawnAxolotl(variantName, spawnLocation);
            case "sheep" -> spawnSheep(variantName, spawnLocation);
            case "parrot" -> spawnParrot(variantName, spawnLocation);
            case "fox" -> spawnFox(variantName, spawnLocation);
            case "frog" -> spawnFrog(variantName, spawnLocation);
            case "mooshroom" -> spawnMooshroom(variantName, spawnLocation);
            case "llama" -> spawnLlama(variantName, spawnLocation);
            case "tropical_fish" -> spawnTropicalFish(variantName, spawnLocation);
            case "panda" -> spawnPanda(variantName, spawnLocation);
            case "salmon" -> spawnSalmon(variantName, spawnLocation);
            case "shulker" -> spawnShulker(variantName, spawnLocation);

            // ADD NEW MOB SPAWN CASES HERE

            default -> false;
        };
    }


    // WOLF
    private static boolean spawnWolf(
            String variantName,
            Location spawnLocation
    ) {
        Wolf.Variant variant = switch (variantName) {
            case "PALE" -> Wolf.Variant.PALE;
            case "SPOTTED" -> Wolf.Variant.SPOTTED;
            case "SNOWY" -> Wolf.Variant.SNOWY;
            case "BLACK" -> Wolf.Variant.BLACK;
            case "CHESTNUT" -> Wolf.Variant.CHESTNUT;
            case "RUSTY" -> Wolf.Variant.RUSTY;
            case "ASHEN" -> Wolf.Variant.ASHEN;
            case "WOODS" -> Wolf.Variant.WOODS;
            case "STRIPED" -> Wolf.Variant.STRIPED;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Wolf wolf = spawnLocation.getWorld().spawn(
                spawnLocation,
                Wolf.class
        );

        wolf.setVariant(variant);
        return true;
    }

    // PIG
    private static boolean spawnPig(
            String variantName,
            Location spawnLocation
    ) {
        Pig.Variant variant = switch (variantName) {
            case "TEMPERATE" -> Pig.Variant.TEMPERATE;
            case "COLD" -> Pig.Variant.COLD;
            case "WARM" -> Pig.Variant.WARM;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Pig pig = spawnLocation.getWorld().spawn(
                spawnLocation,
                Pig.class
        );

        pig.setVariant(variant);
        return true;
    }

    // COW
    private static boolean spawnCow(
            String variantName,
            Location spawnLocation
    ) {
        Cow.Variant variant = switch (variantName) {
            case "TEMPERATE" -> Cow.Variant.TEMPERATE;
            case "COLD" -> Cow.Variant.COLD;
            case "WARM" -> Cow.Variant.WARM;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Cow cow = spawnLocation.getWorld().spawn(
                spawnLocation,
                Cow.class
        );

        cow.setVariant(variant);
        return true;
    }

    // CHICKEN
    private static boolean spawnChicken(
            String variantName,
            Location spawnLocation
    ) {
        Chicken.Variant variant = switch (variantName) {
            case "TEMPERATE" -> Chicken.Variant.TEMPERATE;
            case "COLD" -> Chicken.Variant.COLD;
            case "WARM" -> Chicken.Variant.WARM;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Chicken chicken = spawnLocation.getWorld().spawn(
                spawnLocation,
                Chicken.class
        );

        chicken.setVariant(variant);
        return true;
    }

    // CAT
    private static boolean spawnCat(
            String variantName,
            Location spawnLocation
    ) {
        Cat.Type variant = switch (variantName.toUpperCase(Locale.ROOT)) {
            case "TABBY" -> Cat.Type.TABBY;
            case "BLACK" -> Cat.Type.BLACK;
            case "RED" -> Cat.Type.RED;
            case "SIAMESE" -> Cat.Type.SIAMESE;
            case "BRITISH_SHORTHAIR" -> Cat.Type.BRITISH_SHORTHAIR;
            case "CALICO" -> Cat.Type.CALICO;
            case "PERSIAN" -> Cat.Type.PERSIAN;
            case "RAGDOLL" -> Cat.Type.RAGDOLL;
            case "WHITE" -> Cat.Type.WHITE;
            case "JELLIE" -> Cat.Type.JELLIE;
            case "ALL_BLACK" -> Cat.Type.ALL_BLACK;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Cat cat = spawnLocation.getWorld().spawn(
                spawnLocation,
                Cat.class
        );

        cat.setCatType(variant);
        return true;
    }

    private static boolean spawnRabbit(
            String variantName,
            Location spawnLocation
    ) {
        Rabbit.Type variant = switch (variantName.toUpperCase(Locale.ROOT)) {
            case "BROWN" -> Rabbit.Type.BROWN;
            case "WHITE" -> Rabbit.Type.WHITE;
            case "BLACK" -> Rabbit.Type.BLACK;
            case "WHITE_SPLOTCHED", "BLACK_AND_WHITE" -> Rabbit.Type.BLACK_AND_WHITE;
            case "GOLD" -> Rabbit.Type.GOLD;
            case "SALT_AND_PEPPER" -> Rabbit.Type.SALT_AND_PEPPER;
            case "THE_KILLER_BUNNY" -> Rabbit.Type.THE_KILLER_BUNNY;
            case "TOAST" -> Rabbit.Type.BROWN;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Rabbit rabbit = spawnLocation.getWorld().spawn(
                spawnLocation,
                Rabbit.class
        );

        rabbit.setRabbitType(variant);

        if (variantName.equalsIgnoreCase("TOAST")) {
            rabbit.setCustomName("Toast");
        }

        return true;
    }

    // HORSE
    private static boolean spawnHorse(
            String variantName,
            Location spawnLocation
    ) {
        Horse.Color color = switch (variantName.toUpperCase(Locale.ROOT)) {
            case "WHITE" -> Horse.Color.WHITE;
            case "CREAMY" -> Horse.Color.CREAMY;
            case "CHESTNUT" -> Horse.Color.CHESTNUT;
            case "BROWN" -> Horse.Color.BROWN;
            case "BLACK" -> Horse.Color.BLACK;
            case "GRAY" -> Horse.Color.GRAY;
            case "DARK_BROWN" -> Horse.Color.DARK_BROWN;
            default -> null;
        };

        if (color == null) {
            return false;
        }

        Horse horse = spawnLocation.getWorld().spawn(
                spawnLocation,
                Horse.class
        );

        horse.setColor(color);
        horse.setStyle(Horse.Style.NONE);
        return true;
    }

    // VILLAGER
    private static boolean spawnVillager(
            String variantName,
            Location spawnLocation
    ) {
        Villager.Type variant = switch (variantName.toUpperCase(Locale.ROOT)) {
            case "PLAINS" -> Villager.Type.PLAINS;
            case "DESERT" -> Villager.Type.DESERT;
            case "JUNGLE" -> Villager.Type.JUNGLE;
            case "SAVANNA" -> Villager.Type.SAVANNA;
            case "SNOW" -> Villager.Type.SNOW;
            case "SWAMP" -> Villager.Type.SWAMP;
            case "TAIGA" -> Villager.Type.TAIGA;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        Villager villager = spawnLocation.getWorld().spawn(
                spawnLocation,
                Villager.class
        );

        villager.setVillagerType(variant);
        return true;
    }

    private static boolean spawnAxolotl(String variantName, Location location) {
        Axolotl.Variant variant;

        try {
            variant = Axolotl.Variant.valueOf(variantName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        location.getWorld().spawn(location, Axolotl.class, axolotl -> {
            axolotl.setVariant(variant);
        });

        return true;
    }

    private static boolean spawnSheep(String variantName, Location location) {
        boolean jeb = variantName.equalsIgnoreCase("JEB_");

        DyeColor color;
        if (jeb) {
            color = DyeColor.WHITE;
        } else {
            try {
                color = DyeColor.valueOf(variantName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        location.getWorld().spawn(location, Sheep.class, sheep -> {
            sheep.setColor(color);

            if (jeb) {
                sheep.setCustomName("jeb_");
                sheep.setCustomNameVisible(false);
            }
        });

        return true;
    }

    private static boolean spawnParrot(String variantName, Location location) {
        Parrot.Variant variant = switch (variantName.toUpperCase(Locale.ROOT)) {
            case "RED_BLUE" -> Parrot.Variant.RED;
            case "BLUE" -> Parrot.Variant.BLUE;
            case "GREEN" -> Parrot.Variant.GREEN;
            case "YELLOW_BLUE" -> Parrot.Variant.CYAN;
            case "GRAY" -> Parrot.Variant.GRAY;
            default -> null;
        };

        if (variant == null) {
            return false;
        }

        location.getWorld().spawn(location, Parrot.class, parrot ->
                parrot.setVariant(variant)
        );

        return true;
    }
    private static boolean spawnFox(String name, Location loc) {
        Fox.Type type = switch (name.toUpperCase(Locale.ROOT)) {
            case "RED" -> Fox.Type.RED;
            case "SNOW" -> Fox.Type.SNOW;
            default -> null;
        };
        if (type == null) return false;

        loc.getWorld().spawn(loc, Fox.class, fox -> fox.setFoxType(type));
        return true;
    }

    private static boolean spawnFrog(String name, Location loc) {
        Frog.Variant variant;
        try {
            variant = Frog.Variant.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, Frog.class, frog -> frog.setVariant(variant));
        return true;
    }

    private static boolean spawnMooshroom(String name, Location loc) {
        MushroomCow.Variant variant;
        try {
            variant = MushroomCow.Variant.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, MushroomCow.class,
                cow -> cow.setVariant(variant));
        return true;
    }

    private static boolean spawnLlama(String name, Location loc) {
        Llama.Color color;
        try {
            color = Llama.Color.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, Llama.class, llama -> llama.setColor(color));
        return true;
    }

    private static boolean spawnTropicalFish(String name, Location loc) {
        TropicalFish.Pattern pattern;
        try {
            pattern = TropicalFish.Pattern.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, TropicalFish.class, fish -> {
            fish.setPattern(pattern);
            fish.setBodyColor(DyeColor.BLUE);
            fish.setPatternColor(DyeColor.YELLOW);
        });
        return true;
    }

    private static boolean spawnPanda(String name, Location loc) {
        Panda.Gene gene;
        try {
            gene = Panda.Gene.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, Panda.class, panda -> {
            panda.setMainGene(gene);
            panda.setHiddenGene(gene);
        });
        return true;
    }

    private static boolean spawnSalmon(String name, Location loc) {
        Salmon.Variant variant;
        try {
            variant = Salmon.Variant.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, Salmon.class,
                salmon -> salmon.setVariant(variant));
        return true;
    }

    private static boolean spawnShulker(String name, Location loc) {
        DyeColor color;
        try {
            color = DyeColor.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return false;
        }

        loc.getWorld().spawn(loc, Shulker.class, shulker -> shulker.setColor(color));
        return true;
    }

    // ADD NEW MOB SPAWN METHODS HERE
}