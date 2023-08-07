package com.codenjoy.dojo.games.knibert.solver;

import static com.codenjoy.dojo.games.knibert.Element.HEAD_DOWN;
import static com.codenjoy.dojo.games.knibert.Element.HEAD_LEFT;
import static com.codenjoy.dojo.games.knibert.Element.HEAD_RIGHT;
import static com.codenjoy.dojo.games.knibert.Element.HEAD_UP;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_END_DOWN;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_END_LEFT;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_END_RIGHT;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_END_UP;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_HORIZONTAL;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_LEFT_DOWN;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_LEFT_UP;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_RIGHT_DOWN;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_RIGHT_UP;
import static com.codenjoy.dojo.games.knibert.Element.TAIL_VERTICAL;
import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.LEFT;
import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;

import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.games.knibert.Element;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.annotations.VisibleForTesting;

public class SnakeBuilder {

  @VisibleForTesting
  protected static final Map<Element, List<Direction>> tailDirections = new EnumMap<>(Element.class);

  private final Cursor cursor;

  static {
    tailDirections.put(HEAD_DOWN, List.of(UP));
    tailDirections.put(HEAD_LEFT, List.of(RIGHT));
    tailDirections.put(HEAD_RIGHT, List.of(LEFT));
    tailDirections.put(HEAD_UP, List.of(DOWN));
    tailDirections.put(TAIL_HORIZONTAL, List.of(LEFT, RIGHT));
    tailDirections.put(TAIL_VERTICAL, List.of(UP, DOWN));
    tailDirections.put(TAIL_LEFT_DOWN, List.of(LEFT, DOWN));
    tailDirections.put(TAIL_LEFT_UP, List.of(LEFT, UP));
    tailDirections.put(TAIL_RIGHT_DOWN, List.of(RIGHT, DOWN));
    tailDirections.put(TAIL_RIGHT_UP, List.of(RIGHT, UP));
    tailDirections.put(TAIL_END_DOWN, Collections.emptyList());
    tailDirections.put(TAIL_END_LEFT, Collections.emptyList());
    tailDirections.put(TAIL_END_UP, Collections.emptyList());
    tailDirections.put(TAIL_END_RIGHT, Collections.emptyList());
  }

  public SnakeBuilder(Cursor cursor) {
    this.cursor = cursor;
  }

  public LinkedList<Point> getSnakeFromBoard(Board board) {

    LinkedList<Point> snake = new LinkedList<>();
    Optional<Point> nextPoint = Optional.of(board.getHead());

    while (nextPoint.isPresent()){
      snake.add(nextPoint.get());
      nextPoint = tailDirections.get(board.getAt(snake.getLast()))
          .stream()
          .map(d -> cursor.moveToDirection(snake.getLast(), d))
          .filter(p -> isNextTailPoint(p, snake))
          .findFirst();
    }

    if (snake.size() != board.getHero().size()) {
      throw new AlgorithmErrorException(
          String.format("Unable to recreate snake%n board hero: %s%n snake: %s",
              board.getHero(), snake));
    }
    return snake;
  }

  /**
   * Most tail points have continuation in both directions. This function checks whether the point
   * is the next on the way from head to tail but not reverse
   * @param snakePoint point that was retrieved
   * @param snake snake points in the order from head to tail built so far
   * @return boolean if the point is on the way from previous snake point to tail, not head
   */
  @VisibleForTesting
    protected boolean isNextTailPoint(Point snakePoint, List<Point> snake) {
    if (snake.size() < 2) { // on stage when only head collected yet,
      // there can be only one further direction and no tail points to compare new point with
      return true;
    }
    return !snakePoint.equals(snake.get(snake.size()-2));
  }


}
