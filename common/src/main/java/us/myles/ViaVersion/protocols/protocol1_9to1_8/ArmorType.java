package us.myles.ViaVersion.protocols.protocol1_9to1_8;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@RequiredArgsConstructor
@Getter
public enum ArmorType {

    LEATHER_HELMET(1, 298, "minecraft:leather_helmet"),
    LEATHER_CHESTPLATE(3, 299, "minecraft:leather_chestplate"),
    LEATHER_LEGGINGS(2, 300, "minecraft:leather_leggings"),
    LEATHER_BOOTS(1, 301, "minecraft:leather_boots"),
    CHAINMAIL_HELMET(2, 302, "minecraft:chainmail_helmet"),
    CHAINMAIL_CHESTPLATE(5, 303, "minecraft:chainmail_chestplate"),
    CHAINMAIL_LEGGINGS(4, 304, "minecraft:chainmail_leggings"),
    CHAINMAIL_BOOTS(1, 305, "minecraft:chainmail_boots"),
    IRON_HELMET(2, 306, "minecraft:iron_helmet"),
    IRON_CHESTPLATE(6, 307, "minecraft:iron_chestplate"),
    IRON_LEGGINGS(5, 308, "minecraft:iron_leggings"),
    IRON_BOOTS(2, 309, "minecraft:iron_boots"),
    DIAMOND_HELMET(3, 310, "minecraft:diamond_helmet"),
    DIAMOND_CHESTPLATE(8, 311, "minecraft:diamond_chestplate"),
    DIAMOND_LEGGINGS(6, 312, "minecraft:diamond_leggings"),
    DIAMOND_BOOTS(3, 313, "minecraft:diamond_boots"),
    GOLD_HELMET(2, 314, "minecraft:gold_helmet"),
    GOLD_CHESTPLATE(5, 315, "minecraft:gold_chestplate"),
    GOLD_LEGGINGS(3, 316, "minecraft:gold_leggings"),
    GOLD_BOOTS(1, 317, "minecraft:gold_boots"),
    NONE(0, 0, "none");

    private static HashMap<Integer, ArmorType> armor;

    static {
        armor = new HashMap<>();
        for (ArmorType a : ArmorType.values()) {
            armor.put(a.getId(), a);
        }
    }

    private final int armorPoints;
    private final int id;
    private final String type;

    /**
     * Find an armour type by the item id
     * @param id ID of the item
     *
     * @return Return the ArmourType, ArmourType.NONE if not found
     */
    public static ArmorType findById(int id) {
        for (ArmorType a : ArmorType.values())
            if (a.getId() == id)
                return a;
        return ArmorType.NONE;
    }

    /**
     * Find an armour type by the item string
     * @param type String name for the item
     *
     * @return Return the ArmourType, ArmourType.NONE if not found
     */
    public static ArmorType findByType(String type) {
        for (ArmorType a : ArmorType.values())
            if (a.getType().equals(type))
                return a;
        return NONE;
    }

    /**
     * Check if an item id is armour
     * @param id The item ID
     * @return True if the item is a piece of armour
     */
    public static boolean isArmor(int id) {
        for (ArmorType a : ArmorType.values())
            if (a.getId() == id)
                return true;
        return false;
    }

    /**
     * Check if an item id is armour
     * @param type The item material name
     * @return True if the item is a piece of armour
     */
    public static boolean isArmor(String type) {
        for (ArmorType a : ArmorType.values())
            if (a.getType().equals(type))
                return true;
        return false;
    }

    /**
     * Get the Minecraft ID for the Armour Type
     * @return The ID
     */
    public int getId() {
        return this.id;
    }
}