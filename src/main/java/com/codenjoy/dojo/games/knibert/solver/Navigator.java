package com.codenjoy.dojo.games.knibert.solver;

import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

public final class Navigator {

  private final Board board;

  public Navigator(Board board) {
    this.board = board;
  }

  public String getMove() {
    BoardModel field = new BoardModel(board);
    System.out.println(field);
    LeeAlgorithm leeAlgorithm = new LeeAlgorithm(field);
    List<Point> route = leeAlgorithm.getShortestRouteToTarget(board.getHead(),
        board.getApples().get(0));
    System.out.println(route);
    System.out.println(field.toString(route));
    if (route.isEmpty()) {
      // TODO: handle scenario
      return Direction.UP.toString();
    }
      return getCommand(route.get(0));
  }

  public String getCommand(Point nextPoint) {
    Point offset = board.getHead().relative(nextPoint);
    if (offset.getX() == -1) {
      return Direction.RIGHT.toString();
    } else if (offset.getX() == 1) {
      return Direction.LEFT.toString();
    } else if (offset.getY() == -1) {
      return Direction.UP.toString();
    } else if (offset.getY() == 1) {
      return Direction.DOWN.toString();
    } else {
      throw new AlgorithmErrorException(String.format("Unable to get command to move from point "
          + "%s to %s", board.getHead(), nextPoint));
    }
  }
}
