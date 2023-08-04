package com.codenjoy.dojo.games.knibert.solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

class Cursor {

  private static Cursor cursor;

  private final Map<Direction, Point> deltas = new HashMap<>();
  private final Point DELTA_UP = new PointImpl(0, 1);
  private final Point DELTA_DOWN = new PointImpl(0, -1);
  private final Point DELTA_LEFT = new PointImpl(-1, 0);
  private final Point DELTA_RIGHT = new PointImpl(1, 0);

  public static Cursor getCursor() {
    if (cursor == null) {
      cursor = new Cursor();
    }
    return cursor;
  }

  private Cursor() {
    deltas.put(Direction.UP, DELTA_UP);
    deltas.put(Direction.DOWN, DELTA_DOWN);
    deltas.put(Direction.RIGHT, DELTA_RIGHT);
    deltas.put(Direction.LEFT, DELTA_LEFT);
  }

  public List<Point> getNearestPoints(Point point) {
    return deltas
        .values()
        .stream()
        .map(d -> moveFromPoint(point, d))
        .collect(Collectors.toList());
  }

  private Point moveFromPoint(Point point, Point delta) {
    Point nextPoint = point.copy();
    nextPoint.moveDelta(delta);
    return nextPoint;
  }

  public Direction getDirection(Point current, Point target) {
    Point offset = target.relative(current);
    return deltas.entrySet()
        .stream()
        .filter(entry -> offset.equals(entry.getValue()))
        .findFirst()
        .map(Map.Entry::getKey)
        .orElseThrow(()-> new AlgorithmErrorException(String.format("Unable to get direction "
            + "from point %s to %s", current, target)));
  }
}
