package com.game.lines.logic;

import com.game.lines.common.Common;
import com.game.lines.entity.Cell;

import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Класс Play содержит игровую логику игры Lines: перемещение шара из ячейки в ячейку (проверка возможности
 * перемещения), генерация новых шаров на игровом поле, а также удаление с поля 5-ти шаров одинакового цвета,
 * выстроенных в линию.
 *
 * @author Eugene Ivanov on 24.04.18
 */

public class Play {

    // Добавляем логгер игрового процесса.
    private static Logger playLogger = Logger.getLogger(Play.class.getName());
    // Ячейка, в которую перемещаем изображение.
    private static Cell target;
    // Связанный список ячеек для реализации проверки возможности хода.
    private static List<Cell> visited = new LinkedList<>();
    // Очередь, основанная на связанном списке, необходима для реализации проверки возможности хода.
    private static Queue<Cell> queue = new LinkedList<>();

    public static void getBalls() {
        new Play();
    }

    public static void getMove(Cell previousCell, Cell currentCell) {
        new Play( previousCell, currentCell );
    }

    // Конструктор класса по умолчанию.
    private Play() {
        generateRandomImages();
    }

    private Play(Cell previousCell, Cell currentCell) {
        target = currentCell;
        visited = new LinkedList<>();
        queue = new LinkedList<>();
        boolean moveAbility = traverse(previousCell);

        if (moveAbility) {
            moveImageCell(previousCell, currentCell);
            generateRandomImages();
            playLogger.info("Move complete!");
        } else {
            playLogger.info("Move impossible..");
        }
    }

    // Рекурсивный обход графа пустых ячеек (массив или область пустых ячеек на игровом поле, в которой находится
    // ячейка, ИЗ КОТОРОЙ планируется переместить изображение), с целью "посетить" все пустые ячейки в заданной
    // области. Если среди "посещенных" ячеек будет находится пустая ячейка В КОТОРУЮ планируется переместить
    // изображение, то ход (перемещение) возможен, иначе - ход невозможен.
    private boolean traverse(Cell node) {
        // Получаем потомков, находящихся по соседству от ячейки-родителя.
        List<Cell> children;
        if (!Objects.isNull(node)) {
            children = node.getNeighbors();
            // Если статус потомка "пустой", то добавляем его в список посещенных ячеек и в конец очереди.
            for (Cell child : children) {
                if (child.getState() == State.EMPTY && !visited.contains(child)) {
                    visited.add(child);
                    queue.offer(child);
                }
            }
//            System.out.println(queue.size());
            traverse(queue.poll());
        }
        return visited.contains(target) && queue.size() == 0;
    }

    /**
     * Перемещение изображения из одной ячейки в другую.
     * @param previousCell предыдущая ячейка (с изображением)
     * @param currentCell текущая (пустая) ячейка.
     */
    private void moveImageCell(Cell previousCell, Cell currentCell) {
        // Получаем изображение из предыдущей ячейки.
        String pictureColor = previousCell.getImageColor();
        // Устанавливаем изображение в пустую ячейку.
        currentCell.setIcon(Common.picturesMap().get(pictureColor) );
        // Удаляем изображение из предыдущей ячейки.
        previousCell.setIcon(null);
        // Меняем состояния предыдущей и текущей ячеек.
        previousCell.setState(State.EMPTY);
        currentCell.setState(State.RELEASED);
        // Добавляем предыдущую ячейку в список свободных ячеек и удаляем из этого списка текущую ячейку.
        Cell.emptyCells.add(previousCell);
        Cell.emptyCells.remove(currentCell);
    }

    // Заполнение изображениями 3-х пустых случайных ячеек.
    private void generateRandomImages() {
        for (int i = 0; i < 3; i++) {
            Cell cell = getRandomCell(Cell.emptyCells); // Получаем рандомную ячейку.
            int index = (int) (Math.random() * Common.PICTURES.length); // Подбираем случайный индекс.
            cell.setIcon(Common.PICTURES[index]); // Устанавливаем случайное изображение в ячейку.
            cell.setState(State.RELEASED); // Устанавливаем состояние "ячейка освобождена".
            Cell.emptyCells.remove(cell); // Удаляем ячейку из списка пустых ячеек.
        }
    }

    /**
     * Получение случайной пустой ячейки.
     * @param freeCells список пустых ячеек.
     * @return случайная пустая ячейка.
     */
    private Cell getRandomCell(List<Cell> freeCells) {
        int index = (int) (Math.random() * freeCells.size() );
        return freeCells.get(index);
    }
}