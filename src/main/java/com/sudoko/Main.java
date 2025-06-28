package com.sudoko;

import com.sudoko.uicustom.button.DifficultyButton;
import com.sudoko.uicustom.screen.MainScreen;

public class Main {

    public static void main(String[] args) {
        Generator generator = new Generator();
        DifficultyButton difficultyButton = new DifficultyButton();
        MainScreen mainScreen = new MainScreen(generator, difficultyButton);
        mainScreen.buildMainScreen();
    }
}