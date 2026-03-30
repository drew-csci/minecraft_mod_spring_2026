package com.example.worldsettings.superenchant;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SuperEnchantmentTableGUI {

    public static final String MENU_TITLE = ChatColor.DARK_PURPLE + "Super Enchantment Table";
    public static final int INPUT_SLOT = 11;
    private static final int CLOSE_SLOT = 26;
    private static final int[] OPTION_SLOTS = {12, 13, 14, 15, 16, 17};

    public static void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, MENU_TITLE);
        ItemStack filler = createBackgroundPane();
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, filler);
        }

        inventory.setItem(4, createTitleItem());
        inventory.setItem(INPUT_SLOT, createInputPlaceholder());

        SuperEnchantmentManager.EnchantmentOption[] options = SuperEnchantmentManager.EnchantmentOption.values();
        for (int i = 0; i < options.length; i++) {
            inventory.setItem(OPTION_SLOTS[i], createOptionButton(options[i]));
        }

        inventory.setItem(CLOSE_SLOT, createCloseButton());
        inventory.setItem(22, createInfoBook());
        player.openInventory(inventory);
    }

    private static ItemStack createBackgroundPane() {
        return createItem(Material.GRAY_STAINED_GLASS_PANE, " ", null);
    }

    private static ItemStack createTitleItem() {
        return createItem(Material.ENCHANTED_BOOK,
            ChatColor.GOLD + "Super Enchantment Console",
            Arrays.asList(
                ChatColor.GRAY + "Place the item you want to empower in slot 6.",
                ChatColor.GRAY + "Click any enchantment button to apply a bonus.",
                ChatColor.GRAY + "You must carry the required lapis and XP."
            ));
    }

    private static ItemStack createInputPlaceholder() {
        return createItem(Material.BOOK,
            ChatColor.YELLOW + "Insert Item Here",
            Arrays.asList(
                ChatColor.GRAY + "Place your weapon, armor, or tool here.",
                ChatColor.GRAY + "Then click an enchantment to apply it."
            ));
    }

    private static ItemStack createOptionButton(SuperEnchantmentManager.EnchantmentOption option) {
        return createItem(Material.LAPIS_LAZULI,
            ChatColor.AQUA + option.getDisplayName(),
            Arrays.asList(
                ChatColor.GRAY + option.getDescription(),
                "",
                ChatColor.BLUE + "Lapis: " + ChatColor.WHITE + option.getLapisCost(),
                ChatColor.BLUE + "XP Levels: " + ChatColor.WHITE + option.getXpCost(),
                ChatColor.DARK_GRAY + "Click to enchant the item in slot 6."
            ));
    }

    private static ItemStack createCloseButton() {
        return createItem(Material.REDSTONE_BLOCK,
            ChatColor.RED + "Close Super Table",
            Collections.singletonList(ChatColor.GRAY + "Click to return to gameplay."));
    }

    private static ItemStack createInfoBook() {
        return createItem(Material.WRITTEN_BOOK,
            ChatColor.LIGHT_PURPLE + "Super Enchantment Guide",
            Arrays.asList(
                ChatColor.GRAY + "Sharpness VII, Protection V, Unbreaking IV, Mending,",
                ChatColor.GRAY + "Soulbound, and Temporal Strike are available here.",
                ChatColor.GRAY + "Rare materials drop from powerful mobs.",
                ChatColor.GRAY + "Use lapis and experience to unlock each cast."
            ));
    }

    private static ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    public static boolean isOptionSlot(int rawSlot) {
        for (int slot : OPTION_SLOTS) {
            if (slot == rawSlot) {
                return true;
            }
        }
        return false;
    }

    public static int toOptionIndex(int rawSlot) {
        for (int i = 0; i < OPTION_SLOTS.length; i++) {
            if (OPTION_SLOTS[i] == rawSlot) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isCloseSlot(int rawSlot) {
        return rawSlot == CLOSE_SLOT;
    }
}
