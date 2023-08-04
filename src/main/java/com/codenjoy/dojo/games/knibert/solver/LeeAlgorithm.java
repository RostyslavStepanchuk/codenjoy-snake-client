package com.codenjoy.dojo.games.knibert.solver;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Point;
import com.google.common.annotations.VisibleForTesting;

class LeeAlgorithm {

  private final BoardModel field;
  private final Cursor cursor;

  public LeeAlgorithm(BoardModel field, Cursor cursor) {
    this.field = field;
    this.cursor = cursor;
  }

  @VisibleForTesting
  protected List<Point> getNearestAvailablePoints(Point point) {
    return cursor.getNearestPoints(point)
        .stream()
        .filter(p -> field.isEmpty(p) || field.isApple(p))
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  protected List<Point> getNearestAvailablePoints(List<Point> points) {
    return points
        .stream()
        .map(this::getNearestAvailablePoints)
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }

  @VisibleForTesting
  protected Point getNearestPointWithMark(Point point, int mark) {
    return cursor.getNearestPoints(point)
        .stream()
        .filter(p -> field.get(p) == mark)
        .findFirst()
        .orElseThrow(() -> new AlgorithmErrorException(String.format("Unable to find %d mark "
            + "near point %s", mark, point)));
  }

  public List<Point> getShortestRouteToTarget(Point start, Point target) {
    List<Point> toSearchForward = List.of(start);
    int[] stepNo = new int[1];
    boolean targetFound = false;
    while (!toSearchForward.isEmpty() && !targetFound) {
      toSearchForward = getNearestAvailablePoints(toSearchForward);
      Optional<Point> foundTarget = toSearchForward.stream()
          .filter(target::equals)
          .findFirst();
      if (foundTarget.isPresent()) {
        targetFound = true;
      } else {
        stepNo[0]++;
        toSearchForward.forEach(p -> field.set(p, stepNo[0]));
      }
    }
    if (targetFound) {
      return traceRouteToStart(stepNo, target);
    } else {
      return Collections.emptyList();
    }
  }

  @VisibleForTesting
  protected List<Point> traceRouteToStart(int[] stepNo, Point target) {
    LinkedList<Point> route = new LinkedList<>();
    route.add(target);
    while (stepNo[0] > 0) {
      Point step = getNearestPointWithMark(route.getFirst(), stepNo[0]);
      route.addFirst(step);
      stepNo[0]--;
    }
    return route;
  }

}
