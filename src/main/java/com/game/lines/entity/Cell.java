package com.game.lines.entity;

import com.game.lines.logic.Play;
import com.game.lines.logic.State;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Класс Cell хранит в себе состояние ячейки игрового поля: координаты, игровое состояние и т.д., а также
 * предоставляет необходимые методы для работы с ячейкой.
 * Статические коллекции "запоминают" информацию о всех ячейках в игре.
 * Класс наследует {@link} AbstractCell, который реализует интерфейс {@link} Clickable.
 * Над объектами класса (ячейками) действия выполняются с помощью кликов мышью.
 *
 * @author Eugene Ivanov on 01.04.18
 */

public class Cell extends AbstractCell {

    // Добавляем логгер ячейки.
    private Logger cellLogger = Logger.getLogger(Cell.class.getName());

    // Карта ячеек, где Ключ - координаты, а Значение - ячейка.
    public static Map<Pair<Integer, Integer>, Cell> cellMap = new Hashtable<>();

    // Список пустых ячеек (this.State == State.EMPTY), которые могут быть заполнены изображениями.
    public static List<Cell> emptyCells = new LinkedList<>();

    // Предыдущая нажатая ячейка.
    private static Cell previousCell;

    private int Xx; // Положение ячейки по оси координат X.
    private int Yy; // Положение ячейки по оси координат Y.
    private State state; // Состояние ячейки.

    // Сеттеры и геттеры координат и состояния ячейки.
    public void setXx(int xx) {
        this.Xx = xx;
    }

    public int getXx() {
        return Xx;
    }

    public void setYy(int yy) {
        this.Yy = yy;
    }

    public int getYy() {
        return Yy;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    /**
     * @return true или false в зависимости есть ли изображение в ячейке.
     */
    public boolean containsImage() {
        return this.getIcon() != null;
    }

    // Метод устанавливает выделение границ ячейки и статус "ячейка выбрана".
    @Override
    public void select() {
        if ( (containsImage()) ) {
            // Устанавливается выделение границ нажатой ячейки.
            setBorder(BorderFactory.createLineBorder(Color.RED, 5));
            // Устанавливается статус нажатой ячейки.
            setState(State.SELECTED);
            // Ячейке, нажатой в прошлый раз, присваивается текущая нажатая ячейка.
            previousCell = this;
        }
    }

    // Метод устанавливает стандартные границы ячейки и статус "ячейка освобождена".
    @Override
    public void release() {
        if ( containsImage() ) {
            // Устанавливается статус нажатой ячейки.
            setState(State.RELEASED);
        }
        // Устанавливается выделение границ нажатой ячейки.
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    /**
     * Реализация абстрактного метода.
     * @return объект Pair c координатами ячейки (x,y).
     */
    @Override
    public Pair<Integer, Integer> getCoordinates() {
        return new Pair<>(getXx(), getYy());
    }

    /**
     * Реализация абстрактного метода.
     * @return список ячеек, находящихся по соседству от данной ячейки.
     */
    public List<Cell> getNeighbors() {
        List<Cell> neighborsList = new LinkedList<>();
        int gridLength = getGridLength();
        // Поиск соседей для ячеек, располагающихся не у края поля.
        if ( (getXx() > 1 && getXx() < gridLength) && (getYy() > 1 && getYy() < gridLength) ) {
            neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() - 1) ));
            neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() + 1) ));
            neighborsList.add(cellMap.get(new Pair<>(getXx() - 1, getYy()) ));
            neighborsList.add(cellMap.get(new Pair<>(getXx() + 1, getYy()) ));
        }
        // Поиск соседей для ячеек, занимающих крайний нижний или крайний верхний ряд,
        // (за исключением крайних правой и левой ячеек).
        else if ( getXx() > 1 && getXx() < gridLength ) {
            neighborsList.add(cellMap.get(new Pair<>(getXx() - 1, getYy()) ));
            neighborsList.add(cellMap.get(new Pair<>(getXx() + 1, getYy()) ));
            if ( getYy() == 1 ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() + 1)));
            } else if ( getYy() == gridLength ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() - 1)));
            }
        }
        // Поиск соседей для ячеек, занимающих крайний левый и крайний правый ряд,
        // (за исключением крайних нижней и верхней ячеек).
        else if ( getYy() > 1 && getYy() < gridLength ) {
            neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() + 1) ));
            neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() - 1) ));
            if ( getXx() == 1 ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx() + 1, getYy()) ));
            } else if ( getXx() == gridLength ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx() - 1, getYy()) ));
            }
        }
        // Поиск соседей для ячеек, находящихся "в углах" игрового поля.
        else if ( getXx() == 1 ) {
            neighborsList.add(cellMap.get(new Pair<>(getXx() + 1, getYy()) ));
            if ( getYy() == 1 ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() + 1) ));
            } else if ( getYy() == gridLength ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() - 1) ));
            }
        } else if ( getXx() == gridLength ) {
            neighborsList.add(cellMap.get(new Pair<>(getXx() - 1, getYy()) ));
            if ( getYy() == 1 ) {
                neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() + 1) ));
            } else if (getYy() == gridLength) {
                neighborsList.add(cellMap.get(new Pair<>(getXx(), getYy() - 1) ));
            }
        }
//        System.out.println("neighborsList.size() = " + neighborsList.size());
//        neighborsList.forEach(e -> e.setBackground(Color.YELLOW));
        return neighborsList;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Cell currentCell = this;
        currentCell.getNeighbors();
        // В зависимости от состояние нажатой ячейки, выполняется определенный код:
        switch ( currentCell.getState() ) {
            // Если ячейка выбрана (выделена цветом), то при нажатии на неё, она деактивируется.
            case SELECTED:
                currentCell.release();
                cellLogger.info("Cell released");
                break;
            // Если ячейка не выбрана, то она выбирается (выделяется цветом), предыдущая ячейка,
            // в свою очередь, деактивируется.
            case RELEASED:
                if ( !Objects.isNull(previousCell) ) {
                    previousCell.release();
                }
                currentCell.select();
                cellLogger.info("Cell selected");
                break;
            // Если ячейка пуста, то проверяется состояние предыдущей ячейки.
            // Если предыдущая ячейка была выбрана, то изображение из неё переносится в текущую (пустую) ячейку.
            // Таким образом, осуществляется один игровой ход.
            case EMPTY:
                if ( !Objects.isNull(previousCell) && (previousCell.getState() == State.SELECTED) ) {
                    previousCell.release();
                    // Выполнение игрового хода.
                    Play.getMove(previousCell, currentCell);
                }
                // Обнуляем предыдущую ячейку.
                previousCell = null;
                break;
        }
//        if ( this.state == State.SELECTED) { cellLogger.info("Cell selected"); }
//        if ( this.state == State.RELEASED) { cellLogger.info("Cell released"); }
//        if ( this.state == State.EMPTY )   { cellLogger.info("Cell is empty"); }
    }

    // Переопределение методов equals() и hashCode().
    @Override
    public boolean equals(Object obj) {
        boolean value = false;
        if ( !(obj instanceof JButton) ) {
            Cell other = (Cell) obj;
            value = (this.Xx == other.Xx && this.Yy == other.Yy );
        }
        return value;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime + result + this.Xx;
        result = prime + result + this.Yy;
        return result;
    }
}