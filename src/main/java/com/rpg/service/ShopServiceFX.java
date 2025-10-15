package com.rpg.service;

import com.rpg.model.characters.Erou;
import com.rpg.model.items.BuffPotion;
import com.rpg.model.items.EnchantScroll;
import com.rpg.model.items.ObiectEchipament;
import com.rpg.service.dto.PurchaseResult;
import com.rpg.service.dto.ShopItemDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * ShopService refactorizat pentru JavaFX
 * NU mai folosește Scanner sau System.out
 * Returnează date pentru UI și gestionează logica de business
 */
public class ShopServiceFX {

    // Categoriile de produse
    public enum ShopCategory {
        POTIUNI("🧪 Poțiuni de Vindecare"),
        BUFF_POTIUNI("💪 Poțiuni de Buff"),
        ECHIPAMENT("⚔️ Echipament"),
        CONSUMABILE("🎁 Consumabile Speciale"),
        PACK_URI("📦 Pack-uri Combo");

        private final String displayName;

        ShopCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Returnează toate produsele disponibile pentru o categorie
     */
    public List<ShopItemDTO> getItemsByCategory(ShopCategory category, int heroLevel) {
        return switch (category) {
            case POTIUNI -> getHealingPotions();
            case BUFF_POTIUNI -> getBuffPotions();
            case ECHIPAMENT -> getEquipment(heroLevel);
            case CONSUMABILE -> getSpecialConsumables();
            case PACK_URI -> getPacks();
        };
    }

    /**
     * Poțiuni de vindecare
     */
    private List<ShopItemDTO> getHealingPotions() {
        List<ShopItemDTO> items = new ArrayList<>();

        items.add(new ShopItemDTO(
                "potiune_mica",
                "🧪 Poțiune Mică",
                "Restabilește 50 HP",
                15,
                ShopCategory.POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "potiune_medie",
                "🧪 Poțiune Medie",
                "Restabilește 100 HP",
                25,
                ShopCategory.POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "potiune_mare",
                "🧪 Poțiune Mare",
                "Restabilește 200 HP",
                45,
                ShopCategory.POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "potiune_maxima",
                "🧪 Poțiune Maximă",
                "Restabilește 500 HP",
                90,
                ShopCategory.POTIUNI,
                1
        ));

        return items;
    }

    /**
     * Poțiuni de buff
     */
    private List<ShopItemDTO> getBuffPotions() {
        List<ShopItemDTO> items = new ArrayList<>();

        // Poțiuni de stat
        items.add(new ShopItemDTO(
                "buff_strength",
                "💪 Poțiune de Strength",
                "+5 Strength pentru 3 lupte",
                50,
                ShopCategory.BUFF_POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "buff_dexterity",
                "🎯 Poțiune de Dexterity",
                "+5 Dexterity pentru 3 lupte",
                50,
                ShopCategory.BUFF_POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "buff_intelligence",
                "🧠 Poțiune de Intelligence",
                "+5 Intelligence pentru 3 lupte",
                50,
                ShopCategory.BUFF_POTIUNI,
                1
        ));

        // Poțiuni de combat
        items.add(new ShopItemDTO(
                "buff_damage",
                "⚔️ Poțiune de Damage",
                "+15% Damage pentru 3 lupte",
                75,
                ShopCategory.BUFF_POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "buff_defense",
                "🛡️ Poțiune de Defense",
                "+15% Defense pentru 3 lupte",
                75,
                ShopCategory.BUFF_POTIUNI,
                1
        ));

        items.add(new ShopItemDTO(
                "buff_critical",
                "💥 Poțiune de Critical",
                "+20% Critical Chance pentru 3 lupte",
                100,
                ShopCategory.BUFF_POTIUNI,
                1
        ));

        return items;
    }

    /**
     * Echipament (generat dinamic pe nivel)
     */
    private List<ShopItemDTO> getEquipment(int heroLevel) {
        List<ShopItemDTO> items = new ArrayList<>();

        // Exemplu: generăm câteva piese random
        int basePrice = 100 + (heroLevel * 20);

        items.add(new ShopItemDTO(
                "weapon_" + heroLevel,
                "⚔️ Armă Nivel " + heroLevel,
                "Damage bonus bazat pe nivel",
                basePrice,
                ShopCategory.ECHIPAMENT,
                1
        ));

        items.add(new ShopItemDTO(
                "armor_" + heroLevel,
                "🛡️ Armură Nivel " + heroLevel,
                "Defense bonus bazat pe nivel",
                basePrice,
                ShopCategory.ECHIPAMENT,
                1
        ));

        return items;
    }

    /**
     * Consumabile speciale
     */
    private List<ShopItemDTO> getSpecialConsumables() {
        List<ShopItemDTO> items = new ArrayList<>();

        items.add(new ShopItemDTO(
                "enchant_scroll",
                "📜 Enchant Scroll",
                "Îmbunătățește un item cu +1 nivel",
                150,
                ShopCategory.CONSUMABILE,
                1
        ));

        items.add(new ShopItemDTO(
                "shaorma_revival",
                "🌯 Șaorma de Revival",
                "Te readuce la viață în luptă!",
                500,
                ShopCategory.CONSUMABILE,
                1
        ));

        return items;
    }

    /**
     * Pack-uri combo
     */
    private List<ShopItemDTO> getPacks() {
        List<ShopItemDTO> items = new ArrayList<>();

        items.add(new ShopItemDTO(
                "starter_pack",
                "📦 Starter Pack",
                "1x Strength, 1x Dexterity, 1x Intelligence (REDUCERE 10%!)",
                135, // În loc de 150
                ShopCategory.PACK_URI,
                1
        ));

        items.add(new ShopItemDTO(
                "combat_pack",
                "📦 Combat Pack",
                "2x Damage, 2x Defense, 1x Critical (REDUCERE 15%!)",
                350, // În loc de 410
                ShopCategory.PACK_URI,
                1
        ));

        return items;
    }

    /**
     * ACHIZIȚIONEAZĂ un item - logica principală
     */
    public PurchaseResult purchaseItem(Erou erou, ShopItemDTO item, int quantity) {
        int totalCost = item.getPrice() * quantity;

        // Verifică dacă are destul gold
        if (erou.getGold() < totalCost) {
            return new PurchaseResult(
                    false,
                    "Nu ai destul gold! Îți lipsesc " + (totalCost - erou.getGold()) + " gold.",
                    0
            );
        }

        // Scade gold-ul
        erou.scadeGold(totalCost);

        // Adaugă itemul în inventar
        boolean added = addItemToHero(erou, item, quantity);

        if (!added) {
            // Returnează gold-ul dacă nu s-a putut adăuga
            erou.adaugaGold(totalCost);
            return new PurchaseResult(
                    false,
                    "Inventarul este plin sau itemul nu a putut fi adăugat!",
                    0
            );
        }

        return new PurchaseResult(
                true,
                "Ai cumpărat " + quantity + "x " + item.getName() + "!",
                totalCost
        );
    }

    /**
     * Adaugă itemul cumpărat în inventarul eroului
     */
    private boolean addItemToHero(Erou erou, ShopItemDTO item, int quantity) {
        String itemId = item.getId();

        // Poțiuni de vindecare
        if (itemId.startsWith("potiune_")) {
            int healAmount = switch (itemId) {
                case "potiune_mica" -> 50;
                case "potiune_medie" -> 100;
                case "potiune_mare" -> 200;
                case "potiune_maxima" -> 500;
                default -> 0;
            };

            for (int i = 0; i < quantity; i++) {
                erou.addHealthPotion(healAmount);
            }
            return true;
        }

        // Poțiuni de buff
        if (itemId.startsWith("buff_")) {
            BuffPotion.BuffType buffType = switch (itemId) {
                case "buff_strength" -> BuffPotion.BuffType.STRENGTH;
                case "buff_dexterity" -> BuffPotion.BuffType.DEXTERITY;
                case "buff_intelligence" -> BuffPotion.BuffType.INTELLIGENCE;
                case "buff_damage" -> BuffPotion.BuffType.DAMAGE;
                case "buff_defense" -> BuffPotion.BuffType.DEFENSE;
                case "buff_critical" -> BuffPotion.BuffType.CRITICAL;
                default -> null;
            };

            if (buffType != null) {
                erou.addBuffPotion(buffType, quantity);
                return true;
            }
        }

        // Enchant Scroll
        if (itemId.equals("enchant_scroll")) {
            for (int i = 0; i < quantity; i++) {
                erou.addEnchantScroll(new EnchantScroll());
            }
            return true;
        }

        // Șaorma Revival
        if (itemId.equals("shaorma_revival")) {
            erou.adaugaShaormaRevival(quantity);
            return true;
        }

        // Pack-uri
        if (itemId.equals("starter_pack")) {
            erou.addBuffPotion(BuffPotion.BuffType.STRENGTH, quantity);
            erou.addBuffPotion(BuffPotion.BuffType.DEXTERITY, quantity);
            erou.addBuffPotion(BuffPotion.BuffType.INTELLIGENCE, quantity);
            return true;
        }

        if (itemId.equals("combat_pack")) {
            erou.addBuffPotion(BuffPotion.BuffType.DAMAGE, quantity * 2);
            erou.addBuffPotion(BuffPotion.BuffType.DEFENSE, quantity * 2);
            erou.addBuffPotion(BuffPotion.BuffType.CRITICAL, quantity);
            return true;
        }

        // Echipament (aici trebuie generarea efectivă)
        if (itemId.startsWith("weapon_") || itemId.startsWith("armor_")) {
            // TODO: Generează echipamentul real și adaugă în inventar
            // erou.getInventar().addItem(generatedEquipment);
            return true;
        }

        return false;
    }

    /**
     * Vinde un item din inventar
     */
    public PurchaseResult sellItem(Erou erou, ObiectEchipament item) {
        int sellPrice = item.getPret() / 2; // 50% din prețul de cumpărare

        boolean removed = erou.getInventar().removeItem(item);

        if (!removed) {
            return new PurchaseResult(
                    false,
                    "Itemul nu a putut fi vândut!",
                    0
            );
        }

        erou.adaugaGold(sellPrice);

        return new PurchaseResult(
                true,
                "Ai vândut " + item.getNume() + " pentru " + sellPrice + " gold!",
                sellPrice
        );
    }

    /**
     * Verifică dacă eroul poate cumpăra un item
     */
    public boolean canAfford(Erou erou, ShopItemDTO item, int quantity) {
        return erou.getGold() >= (item.getPrice() * quantity);
    }

    /**
     * Calculează discount pentru pack-uri
     */
    public int calculateDiscount(String packId) {
        return switch (packId) {
            case "starter_pack" -> 10; // 10% discount
            case "combat_pack" -> 15; // 15% discount
            default -> 0;
        };
    }
}