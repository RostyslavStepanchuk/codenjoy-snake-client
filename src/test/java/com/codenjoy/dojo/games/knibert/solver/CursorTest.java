package com.codenjoy.dojo.games.knibert.solver;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CursorTest {

  private Cursor subject = Cursor.getCursor();

  @Test
  @DisplayName("getNearestPoints - gets nearest points")
  void getNearestPoints_getsNeighbors() {
    PointImpl start = new PointImpl(2, 2);
    PointImpl upper = new PointImpl(2, 3);
    PointImpl lower = new PointImpl(2, 1);
    PointImpl left = new PointImpl(1, 2);
    PointImpl right = new PointImpl(3, 2);

    List<Point> actual = subject.getNearestPoints(start);

    assertEquals(4, actual.size());
    assertTrue(actual.contains(upper));
    assertTrue(actual.contains(lower));
    assertTrue(actual.contains(left));
    assertTrue(actual.contains(right));
  }

  @Test
  @DisplayName("getNearestPoints - gets nearest points")
  void getNearestPoints_doesNotMutateStartPoint() {
    PointImpl start = new PointImpl(2, 2);
    subject.getNearestPoints(start);
    assertEquals(new PointImpl(2, 2), start);
  }

  @Test
  @DisplayName("getDirection - gets direction by delta point")
  void getDirection_returnsDirectionOfDelta() {
    PointImpl start = new PointImpl(2, 2);
    PointImpl upper = new PointImpl(2, 3);
    PointImpl lower = new PointImpl(2, 1);
    PointImpl left = new PointImpl(1, 2);
    PointImpl right = new PointImpl(3, 2);

    assertEquals(Direction.UP, subject.getDirection(start, upper));
    assertEquals(Direction.DOWN, subject.getDirection(start, lower));
    assertEquals(Direction.LEFT, subject.getDirection(start, left));
    assertEquals(Direction.RIGHT, subject.getDirection(start, right));
  }

  @Test
  @DisplayName("getDirection - throws error if target is distant")
  void getDirection_throwsErrorIfTargetNotNear() {
    PointImpl start = new PointImpl(2, 2);
    PointImpl diagonal = new PointImpl(3, 3);
    PointImpl farUp = new PointImpl(2, 4);
    PointImpl farRight = new PointImpl(4, 2);

    assertThrows(AlgorithmErrorException.class, () -> subject.getDirection(start, farUp));
    assertThrows(AlgorithmErrorException.class, () -> subject.getDirection(start, diagonal));
    assertThrows(AlgorithmErrorException.class, () -> subject.getDirection(start, farRight));
  }
}