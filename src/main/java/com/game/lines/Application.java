package com.game.lines;

import com.game.lines.gui.GameInitializer;

/**
 * Класс Application содержит главный метод для запуска приложения.
 *
 * @author Eugene Ivanov on 31.03.18
 */

public class Application {

    public static void main(String[] args) {
        // 7 X 7
//        new GameInitializer(   420,500, 7, 7);

        // 8 X 8
//        new GameInitializer(   466,555, 8, 8);

        // 9 X 9
//        new GameInitializer(   545, 640, 9, 9);

        // 10 X 10
        new GameInitializer(   600,630, 10, 10);

        // 11 X 11
//        new GameInitializer(   660,690, 11, 11);

        // 12 X 12
//        new GameInitializer(   690,770, 12, 12);

    }
}