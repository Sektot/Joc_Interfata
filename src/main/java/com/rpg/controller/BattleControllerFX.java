package com.rpg.controller;

import com.rpg.model.characters.Erou;
import com.rpg.model.characters.Inamic;
import com.rpg.service.BattleServiceFX;
import com.rpg.service.EnemyGeneratorRomanesc;
import com.rpg.service.dto.AbilityDTO;
import com.rpg.service.dto.BattleInitDTO;
import com.rpg.utils.DialogHelper;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;

/**
 * BattleControllerFX - Controller îmbunătățit pentru lupte
 */
public class BattleControllerFX {

    private Stage stage;
    private Erou hero;
    private Inamic enemy;
    //private Inamic currentEnemy;
    private BattleServiceFX battleService;

    // UI Components - Hero
    private Label heroNameLabel;
    private Label heroHPLabel;
    private ProgressBar heroHPBar;
    private Label heroResourceLabel;
    private ProgressBar heroResourceBar;

    // UI Components - Enemy
    private Label enemyNameLabel;
    private Label enemyHPLabel;
    private ProgressBar enemyHPBar;

    // Battle Log
    private TextArea battleLog;

    // Action Buttons
    private Button attackButton;
    private Button fleeButton;
    private VBox abilityButtonsPanel;
    private VBox potionButtonsPanel;

    public BattleControllerFX(Stage stage, Erou hero, Inamic enemy) {
        this.stage = stage;
        this.hero = hero;
        this.enemy = enemy;
        this.battleService = new BattleServiceFX();


    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setTop(createHeader());
        root.setCenter(createBattleArea());
        root.setBottom(createActionPanel());

        root.setStyle("-fx-background-color: #0f0f1e;");

        // Inițializează bătălia
        initializeBattle();

        return new Scene(root, 1200, 800);
    }

    /**
     * Header cu titlul
     */
    private VBox createHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #1a1a2e;");

        Label title = new Label("⚔️ LUPTĂ: " + hero.getNume() + " VS " + enemy.getNume() + " ⚔️");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e94560;");

        if (enemy.isBoss()) {
            Label bossLabel = new Label("💀 BOSS BATTLE 💀");
            bossLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ff6b6b;");
            header.getChildren().addAll(title, bossLabel);
        } else {
            header.getChildren().add(title);
        }

        return header;
    }

    /**
     * Zona de luptă - Hero, Log, Enemy
     */
    private HBox createBattleArea() {
        HBox battleArea = new HBox(20);
        battleArea.setPadding(new Insets(20));
        battleArea.setAlignment(Pos.CENTER);

        VBox heroPanel = createHeroPanel();
        VBox logPanel = createLogPanel();
        VBox enemyPanel = createEnemyPanel();

        battleArea.getChildren().addAll(heroPanel, logPanel, enemyPanel);

        return battleArea;
    }

    /**
     * Panel erou
     */
    private VBox createHeroPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.CENTER);
        panel.setStyle(
                "-fx-background-color: #16213e; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: #27ae60; " +
                        "-fx-border-width: 3; " +
                        "-fx-border-radius: 15;"
        );
        panel.setPrefWidth(300);

        heroNameLabel = new Label(hero.getNume());
        heroNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #27ae60;");

        Label hpTextLabel = new Label("❤️ HP");
        hpTextLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        heroHPBar = new ProgressBar(1.0);
        heroHPBar.setPrefWidth(250);
        heroHPBar.setPrefHeight(25);
        heroHPBar.setStyle("-fx-accent: #e74c3c;");

        heroHPLabel = new Label(hero.getViata() + " / " + hero.getViataMaxima());
        heroHPLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label resourceTextLabel = new Label("💙 " + hero.getTipResursa());
        resourceTextLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        heroResourceBar = new ProgressBar(1.0);
        heroResourceBar.setPrefWidth(250);
        heroResourceBar.setPrefHeight(20);
        heroResourceBar.setStyle("-fx-accent: #3498db;");

        heroResourceLabel = new Label(hero.getResursaCurenta() + " / " + hero.getResursaMaxima());
        heroResourceLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        panel.getChildren().addAll(
                heroNameLabel,
                hpTextLabel, heroHPBar, heroHPLabel,
                resourceTextLabel, heroResourceBar, heroResourceLabel
        );

        return panel;
    }

    /**
     * Panel inamic
     */
    private VBox createEnemyPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.CENTER);
        panel.setStyle(
                "-fx-background-color: #16213e; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: #e74c3c; " +
                        "-fx-border-width: 3; " +
                        "-fx-border-radius: 15;"
        );
        panel.setPrefWidth(300);

        enemyNameLabel = new Label(enemy.getNume());
        enemyNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");

        if (enemy.isBoss()) {
            Label bossIndicator = new Label("💀 BOSS");
            bossIndicator.setStyle("-fx-font-size: 14px; -fx-text-fill: #ff6b6b; -fx-font-weight: bold;");
            panel.getChildren().add(bossIndicator);
        }

        Label hpTextLabel = new Label("❤️ HP");
        hpTextLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        enemyHPBar = new ProgressBar(1.0);
        enemyHPBar.setPrefWidth(250);
        enemyHPBar.setPrefHeight(25);
        enemyHPBar.setStyle("-fx-accent: #e74c3c;");

        enemyHPLabel = new Label(enemy.getViata() + " / " + enemy.getViataMaxima());
        enemyHPLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

        panel.getChildren().addAll(
                enemyNameLabel,
                hpTextLabel, enemyHPBar, enemyHPLabel
        );

        return panel;
    }

    /**
     * Panel log
     */
    private VBox createLogPanel() {
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #16213e; -fx-background-radius: 10;");

        Label logLabel = new Label("📜 Battle Log");
        logLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

        battleLog = new TextArea();
        battleLog.setEditable(false);
        battleLog.setWrapText(true);
        battleLog.setStyle(
                "-fx-control-inner-background: #0f0f1e; " +
                        "-fx-text-fill: #f1f1f1; " +
                        "-fx-font-family: 'Courier New'; " +
                        "-fx-font-size: 13px;"
        );
        battleLog.setPrefWidth(400);
        battleLog.setPrefHeight(400);
        VBox.setVgrow(battleLog, Priority.ALWAYS);

        panel.getChildren().addAll(logLabel, battleLog);
        return panel;
    }

    /**
     * Panel acțiuni
     */
    private VBox createActionPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #1a1a2e;");

        // Butoane principale
        HBox mainButtons = new HBox(10);
        mainButtons.setAlignment(Pos.CENTER);

        attackButton = createActionButton("⚔️ ATAC NORMAL", "#27ae60");
        attackButton.setOnAction(e -> handleNormalAttack());

        fleeButton = createActionButton("🏃 FUGI", "#e67e22");
        fleeButton.setOnAction(e -> handleFlee());

        mainButtons.getChildren().addAll(attackButton, fleeButton);

        // Panel abilități
        Label abilitiesLabel = new Label("✨ Abilități:");
        abilitiesLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");

        abilityButtonsPanel = new VBox(5);
        abilityButtonsPanel.setAlignment(Pos.CENTER);

        // Panel poțiuni
        Label potionsLabel = new Label("🧪 Poțiuni:");
        potionsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white; -fx-font-weight: bold;");

        potionButtonsPanel = new VBox(5);
        potionButtonsPanel.setAlignment(Pos.CENTER);

        HBox bottomPanels = new HBox(30);
        bottomPanels.setAlignment(Pos.CENTER);

        VBox abilitiesContainer = new VBox(5, abilitiesLabel, abilityButtonsPanel);
        abilitiesContainer.setAlignment(Pos.CENTER);

        VBox potionsContainer = new VBox(5, potionsLabel, potionButtonsPanel);
        potionsContainer.setAlignment(Pos.CENTER);

        bottomPanels.getChildren().addAll(abilitiesContainer, potionsContainer);

        panel.getChildren().addAll(mainButtons, bottomPanels);
        return panel;
    }

    /**
     * Creează un buton de acțiune
     */
    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 12px 30px; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e ->
                btn.setStyle(
                        "-fx-font-size: 16px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: derive(" + color + ", 20%); " +
                                "-fx-text-fill: white; " +
                                "-fx-padding: 12px 30px; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                )
        );

        btn.setOnMouseExited(e ->
                btn.setStyle(
                        "-fx-font-size: 16px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-background-color: " + color + "; " +
                                "-fx-text-fill: white; " +
                                "-fx-padding: 12px 30px; " +
                                "-fx-background-radius: 10; " +
                                "-fx-cursor: hand;"
                )
        );

        return btn;
    }

    // ==================== BATTLE LOGIC ====================

    private void initializeBattle() {
        EnemyGeneratorRomanesc generator = new EnemyGeneratorRomanesc();
        List<Inamic> enemies = generator.genereazaInamici(hero.getNivel());
        this.enemy = enemies.get(0);
        BattleInitDTO initData = battleService.initializeBattle(hero, enemy);

        updateUI(new AbilityDTO.BattleStateDTO(
                initData.getHeroHP(),
                initData.getHeroMaxHP(),
                initData.getHeroResource(),
                initData.getHeroMaxResource(),
                initData.getEnemyHP(),
                initData.getEnemyMaxHP(),
                initData.getAbilities()
        ));

        addToLog("⚔️ Bătălia începe!");
        addToLog(hero.getNume() + " vs " + enemy.getNume());

        if (enemy.isBoss()) {
            addToLog("💀 BOSS BATTLE! Pregătește-te!");
        }
        addToLog("━━━━━━━━━━━━━━━━━━━━━━━");
    }

    private void handleNormalAttack() {
        disableAllButtons();

        AbilityDTO.BattleTurnResultDTO result = battleService.executeNormalAttack(hero, enemy);

        addToLog(result.getLog());

        if (result.isBattleOver()) {
            handleBattleEnd(result);
        } else {
            updateUI(result.getCurrentState());
            enableAllButtons();
        }
    }

    private void handleFlee() {
        if (enemy.isBoss()) {
            addToLog("❌ Nu poți fugi de la un BOSS!");
            return;
        }

        if (DialogHelper.showConfirmation("Fugă", "Ești sigur că vrei să fugi din luptă?")) {
            disableAllButtons();

            AbilityDTO.BattleTurnResultDTO result = battleService.attemptFlee(hero, enemy);
            addToLog(result.getLog());

            if (result.hasFled()) {
                DialogHelper.showInfo("Fugă Reușită", "Ai scăpat din luptă!");
                returnToTown();
            } else if (result.isBattleOver()) {
                handleBattleEnd(result);
            } else {
                updateUI(result.getCurrentState());
                enableAllButtons();
            }
        }
    }

    private void handleAbilityUse(String abilityName) {
        disableAllButtons();

        AbilityDTO.BattleTurnResultDTO result = battleService.executeAbility(hero, enemy, abilityName);

        if (!result.isSuccess()) {
            // Abilitatea nu a putut fi folosită
            addToLog(result.getLog());
            enableAllButtons();
            return;
        }

        addToLog(result.getLog());

        if (result.isBattleOver()) {
            handleBattleEnd(result);
        } else {
            updateUI(result.getCurrentState());
            enableAllButtons();
        }
    }

    private void handlePotionUse(int healAmount) {
        disableAllButtons();

        AbilityDTO.BattleTurnResultDTO result = battleService.usePotion(hero, enemy, healAmount);

        addToLog(result.getLog());

        if (result.isBattleOver()) {
            handleBattleEnd(result);
        } else {
            updateUI(result.getCurrentState());
            enableAllButtons();
        }
    }

    private void handleBattleEnd(AbilityDTO.BattleTurnResultDTO turnResult) {
        AbilityDTO.BattleResultDTO result = turnResult.getFinalResult();

        if (result == null) {
            // Fled
            returnToTown();
            return;
        }

        if (result.isVictory()) {
            showVictoryScreen(result);
        } else {
            showDefeatScreen();
        }
    }

    private void showVictoryScreen(AbilityDTO.BattleResultDTO result) {
        StringBuilder victoryMsg = new StringBuilder();
        victoryMsg.append("🎉 VICTORIE! 🎉\n\n");
        victoryMsg.append("Recompense:\n");
        victoryMsg.append("💰 Gold: ").append(result.getGoldEarned()).append("\n");
        victoryMsg.append("⭐ Experiență: ").append(result.getExperienceEarned()).append("\n");

        if (result.getShaormaReward() > 0) {
            victoryMsg.append("🌯 Șaorma Revival: ").append(result.getShaormaReward()).append("\n");
        }

        if (result.hasLoot()) {
            victoryMsg.append("\n📦 Loot primit:\n");
            for (var item : result.getLoot()) {
                victoryMsg.append("  • ").append(item.getNume()).append("\n");
            }
        }

        DialogHelper.showSuccess("Victorie!", victoryMsg.toString());

        // Aplică recompensele
        hero.adaugaGold(result.getGoldEarned());
        hero.adaugaXp(result.getExperienceEarned());

        if (result.getShaormaReward() > 0) {
            hero.adaugaShaormaRevival(result.getShaormaReward());
        }

        if (result.hasLoot()) {
            for (var item : result.getLoot()) {
                hero.getInventar().addItem(item);
            }
        }

        returnToTown();
    }

    private void showDefeatScreen() {
        DialogHelper.showError("Înfrângere!", "Ai fost învins!\n💀 Game Over");
        // Aici poți implementa logica pentru moarte (ex: șaorma revival)
        returnToTown();
    }

    private void returnToTown() {
        TownMenuController townController = new TownMenuController(stage, hero);
        stage.setScene(townController.createScene());
    }

    // ==================== UI UPDATE ====================

    private void updateUI(AbilityDTO.BattleStateDTO state) {
        // Update hero
        heroHPLabel.setText(state.getHeroHP() + " / " + state.getHeroMaxHP());
        heroHPBar.setProgress((double) state.getHeroHP() / state.getHeroMaxHP());
        animateHealthBar(heroHPBar);

        heroResourceLabel.setText(state.getHeroResource() + " / " + state.getHeroMaxResource());
        heroResourceBar.setProgress((double) state.getHeroResource() / state.getHeroMaxResource());

        // Update enemy
        enemyHPLabel.setText(state.getEnemyHP() + " / " + state.getEnemyMaxHP());
        enemyHPBar.setProgress((double) state.getEnemyHP() / state.getEnemyMaxHP());
        animateHealthBar(enemyHPBar);

        // Update abilities
        updateAbilityButtons(state.getAbilities());

        // Update potions
        updatePotionButtons();
    }

    private void updateAbilityButtons(java.util.List<AbilityDTO> abilities) {
        abilityButtonsPanel.getChildren().clear();

        for (AbilityDTO ability : abilities) {
            Button btn = new Button(ability.getDisplayName());
            btn.setDisable(!ability.isAvailable());

            String color = ability.isAvailable() ? "#9b59b6" : "#7f8c8d";
            btn.setStyle(
                    "-fx-font-size: 14px; " +
                            "-fx-background-color: " + color + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-padding: 8px 20px; " +
                            "-fx-background-radius: 8; " +
                            "-fx-min-width: 200px;"
            );

            btn.setOnAction(e -> handleAbilityUse(ability.getName()));

            abilityButtonsPanel.getChildren().add(btn);
        }
    }

    private void updatePotionButtons() {
        potionButtonsPanel.getChildren().clear();

        Map<Integer, Integer> potions = hero.getInventar().getHealthPotions();

        for (Map.Entry<Integer, Integer> entry : potions.entrySet()) {
            int healAmount = entry.getKey();
            int count = entry.getValue();

            if (count > 0) {
                Button btn = new Button("🧪 Poțiune (" + healAmount + " HP) x" + count);
                btn.setStyle(
                        "-fx-font-size: 14px; " +
                                "-fx-background-color: #27ae60; " +
                                "-fx-text-fill: white; " +
                                "-fx-padding: 8px 20px; " +
                                "-fx-background-radius: 8; " +
                                "-fx-min-width: 200px;"
                );

                btn.setOnAction(e -> handlePotionUse(healAmount));

                potionButtonsPanel.getChildren().add(btn);
            }
        }
    }

    private void addToLog(String message) {
        battleLog.appendText(message + "\n");
        battleLog.setScrollTop(Double.MAX_VALUE);
    }

    private void disableAllButtons() {
        attackButton.setDisable(true);
        fleeButton.setDisable(true);
        abilityButtonsPanel.getChildren().forEach(node ->
                ((Button)node).setDisable(true)
        );
        potionButtonsPanel.getChildren().forEach(node ->
                ((Button)node).setDisable(true)
        );
    }

    private void enableAllButtons() {
        attackButton.setDisable(false);
        fleeButton.setDisable(false);
        // Abilities și potions vor fi update-ate prin updateUI
    }

    private void animateHealthBar(ProgressBar bar) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), bar);
        ft.setFromValue(0.5);
        ft.setToValue(1.0);
        ft.play();
    }
}