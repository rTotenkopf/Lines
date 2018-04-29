package com.game.lines.logic;

import java.awt.event.MouseListener;

/**
 * @author Eugene Ivanov on 11.04.18
 */

public interface Clickable extends MouseListener {
    void select();
    void release();
}