package com.codenjoy.dojo.games.knibert.solver;

import java.util.Collections;
import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.google.common.annotations.VisibleForTesting;

class BoardModel {

  private static final int DIMX = 15;
  private static final int DIMY = 15;
  private final int[][] field;

  static final int BARRIER = -5;
  static final int HEAD = -10;
  static final int SNAKE_TAIL = -20;
  static final int STONE = -4;
  static final int APPLE = 100;
  static final int EMPTY = 0;

  BoardModel(Board board) {
    this.field = new int [DIMX][DIMY];
    setEnvironment(board);
  }

  @VisibleForTesting
  protected void setEnvironment(Board board) {
    board.getBarriers().forEach(barrier->set(barrier,BARRIER));
    board.getHero().forEach(tailPoint->set(tailPoint, SNAKE_TAIL));
    set (board.getHead(), HEAD);
    board.getApples().forEach(apple -> set(apple, APPLE));
    board.getStones().forEach(stone->set(stone,STONE));
  }

  int get(Point point) {
    return field[point.getY()][point.getX()];
  }

  void set(Point point, int val) {
    field[point.getY()][point.getX()] = val;
  }

  boolean isEmpty (Point point) {
    return get(point) == 0;
  }

  boolean isApple (Point point) {
    return get(point) == APPLE;
  }

  private String toStringElement (Point point, List<Point> path) {
    int val = get(point);
    switch (val) {
      case EMPTY: return " . ";
      case BARRIER: return "XXX";
      case SNAKE_TAIL: return " o ";
      case HEAD: return " O ";
      case APPLE: return " A ";
      case STONE: return " S ";
      default:
        if (path.isEmpty() || path.contains(point)) {
          return String.format("%3d", val);
        } else {
          return " . ";
        }
    }
  }

  @Override
  public String toString(){
    return toString(Collections.emptyList());
  }

  public String toString (List<Point> rout) {
    StringBuilder sb = new StringBuilder();
    for (int c = -1; c < DIMX; c++){
      sb.append(String.format("%3s", c));
    }
    sb.append("\n");
    for (int y = 0; y < DIMY; y++) {
      sb.append(String.format("%3s", y));
      for (int x = 0; x < DIMX; x++) {
        PointImpl point = new PointImpl(x, y);
        sb.append(toStringElement(point, rout));
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
