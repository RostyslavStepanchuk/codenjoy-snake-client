package com.codenjoy.dojo.games.knibert.solver;

import java.util.Collections;
import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

final class BoardModel {

  private final static int DIMX = 15;
  private final static int DIMY = 15;
  private final int[][] field;

  final static int BARRIER = -5;
  final static int HEAD = -10;
  final static int SNAKE_TAIL = -20;
  final static int STONE = -4;
  final static int APPLE = 100;
  final static int EMPTY = 0;

  BoardModel(Board board) {
    this.field = new int [DIMX][DIMY];
    setEnvironment(board);
  }

  void setEnvironment(Board board) {
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
