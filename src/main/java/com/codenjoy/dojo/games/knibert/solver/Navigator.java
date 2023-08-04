package com.codenjoy.dojo.games.knibert.solver;

import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

public class Navigator {

  private final Board board;
  private final Cursor cursor;
  private final LeeAlgorithm leeAlgorithm;

  public Navigator(Board board) {
    this(
        board,
        Cursor.getCursor(),
        new LeeAlgorithm(new BoardModel(board), Cursor.getCursor()));
  }

  public Navigator(Board board, Cursor cursor, LeeAlgorithm leeAlgorithm) {
    this.board = board;
    this.cursor = cursor;
    this.leeAlgorithm = leeAlgorithm;
  }

  public String getMove() {
    List<Point> shortestRoute = leeAlgorithm
        .getShortestRouteToTarget(board.getHead(), board.getApples().get(0));
    if (shortestRoute.isEmpty()) {
      // TODO: handle scenario
      return Direction.UP.toString();
    }
      return cursor.getDirection(board.getHead(), shortestRoute.get(0)).toString();
  }
}
