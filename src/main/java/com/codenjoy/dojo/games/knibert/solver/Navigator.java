package com.codenjoy.dojo.games.knibert.solver;

import java.util.LinkedList;
import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.annotations.VisibleForTesting;

public class Navigator {

  private final Board board;
  private final Cursor cursor;
  private final LeeAlgorithm leeAlgorithm;
  private final SnakeBuilder snakeBuilder;
  private final BoardModel field;

  public Navigator(Board board) {
    this(board,
        Cursor.getCursor(),
        new LeeAlgorithm(Cursor.getCursor()),
        new SnakeBuilder(Cursor.getCursor()),
        new BoardModel(board));
  }

  public Navigator(Board board,
                   Cursor cursor,
                   LeeAlgorithm leeAlgorithm,
                   SnakeBuilder snakeBuilder,
                   BoardModel field) {
    this.board = board;
    this.cursor = cursor;
    this.leeAlgorithm = leeAlgorithm;
    this.snakeBuilder = snakeBuilder;
    this.field = field;
  }

  public String findSolution() {
    try {
      return getMove();
    } catch (Exception e) {
      return moveNearestSafePoint(board);
    }
  }

  @VisibleForTesting
  protected String getMove() {
    setupInitialField();
    List<Point> shortestRoute = leeAlgorithm.getShortestRouteToTarget(
        board.getHead(),
        board.getApples().get(0));
    if (shortestRoute.isEmpty()) {
      return moveNearestSafePoint(board);
    }
    return cursor.getDirection(board.getHead(), shortestRoute.get(0)).toString();
  }

  @VisibleForTesting
  protected void setupInitialField() {
    LinkedList<Point> snake = snakeBuilder.getSnakeFromBoard(board);
    field.setSnake(snake);
    leeAlgorithm.setField(field);
  }

  protected String moveNearestSafePoint(Board board) {
    Point nextPoint = cursor.getNearestPoints(board.getHead())
        .stream()
        .filter(p -> !board.getHero().contains(p))
        .filter(p -> !board.getBarriers().contains(p))
        .findFirst()
        // direction doesn't matter we're f****d up anyway
        .orElseGet(() -> cursor.moveToDirection(board.getHead(), Direction.UP));
    return cursor.getDirection(board.getHead(), nextPoint).toString();
  }

}
