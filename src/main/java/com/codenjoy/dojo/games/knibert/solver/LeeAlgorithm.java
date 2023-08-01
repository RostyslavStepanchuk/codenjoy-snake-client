package com.codenjoy.dojo.games.knibert.solver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

final class LeeAlgorithm {

  private final List<Point> deltas;

  private final BoardModel field;

  public LeeAlgorithm(BoardModel field) {
    this.field = field;
    deltas = new ArrayList<>();
    deltas.add(new PointImpl(0, -1));
    deltas.add(new PointImpl(-1, 0));
    deltas.add(new PointImpl(1, 0));
    deltas.add(new PointImpl(0, 1));
  }

  private List<Point> getNearestAvailablePoints(Point point) {
    return deltas
        .stream()
        .map(d -> moveFromPoint(point, d))
        .filter(p -> field.isEmpty(p) || field.isApple(p))
        .collect(Collectors.toList());
  }

  private Point getNearestPointWithMark(Point point, int mark) {
    return deltas
        .stream()
        .map(d -> moveFromPoint(point, d))
        .filter(p -> field.get(p) == mark)
        .findFirst()
        .orElseThrow(()->new AlgorithmErrorException(String.format("Unable to find %d mark "
            + "near point %s", mark, point)));
  }

  private List<Point> getNearestAvailablePoints(List<Point> points) {
    return points
        .stream()
        .map(this::getNearestAvailablePoints)
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toList());
  }

  private Point moveFromPoint(Point point, Point delta) {
    Point cursor = point.copy();
    cursor.moveDelta(delta);
    return cursor;
  }

  List<Point> getShortestRouteToTarget(Point start, Point target) {
    List<Point> toSearchForward = List.of(start);
    int [] stepNo = new int[1];
    boolean targetFound = false;
    while (!toSearchForward.isEmpty()) {
      toSearchForward = getNearestAvailablePoints(toSearchForward);
      Optional<Point> foundTarget = toSearchForward.stream()
          .filter(target::equals)
          .findFirst();
      if (foundTarget.isPresent()) {
        toSearchForward = Collections.emptyList();
        targetFound = true;
      } else {
        stepNo[0]++;
        toSearchForward.forEach(p -> field.set(p, stepNo[0]));
        System.out.println(field);
      }
    }
      if (targetFound) {
        return traceRouteToStart(stepNo, target);
      } else {
        return Collections.emptyList();
      }
  }

  private List<Point> traceRouteToStart(int[] stepNo, Point target) {
    LinkedList<Point> route = new LinkedList<>();
    route.add(target);
    while (stepNo[0] > 0){
      Point cursor = getNearestPointWithMark(route.getFirst(), stepNo[0]);
      route.addFirst(cursor);
      stepNo[0]--;
    }
    return route;
  }

}
