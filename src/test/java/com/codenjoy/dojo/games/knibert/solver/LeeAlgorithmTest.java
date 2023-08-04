package com.codenjoy.dojo.games.knibert.solver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Collections;
import java.util.List;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeeAlgorithmTest {

  @Spy
  @InjectMocks
  private LeeAlgorithm subject;

  @Mock
  private BoardModel field;

  @Mock
  private Cursor cursor;

  @Mock
  private Point start;

  @Test
  void getShortestRouteToTarget() {
  }

  @Test
  @DisplayName("getNearestAvailablePoints - gets neighbors from cursor")
  void getNearestAvailablePoints_getsPointsFromCursor() {
    // given
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(cursor)
        .getNearestPoints(any());
    doReturn(true).when(field).isEmpty(neighbor1);
    doReturn(true).when(field).isApple(neighbor2);

    // when
    List<Point> actual = subject.getNearestAvailablePoints(start);

    // then
    verify(cursor).getNearestPoints(start);
    assertEquals(2, actual.size());
    assertTrue(actual.contains(neighbor1));
    assertTrue(actual.contains(neighbor2));
  }

  @Test
  @DisplayName("getNearestAvailablePoints - avoids not empty cells")
  void getNearestAvailablePoints_avoidsNotEmptySells() {
    // given
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(cursor)
        .getNearestPoints(any());
    doReturn(false).when(field).isEmpty(neighbor1);
    doReturn(false).when(field).isApple(neighbor1);
    doReturn(true).when(field).isEmpty(neighbor2);

    // when
    List<Point> actual = subject.getNearestAvailablePoints(start);

    // then
    verify(cursor).getNearestPoints(start);
    assertEquals(1, actual.size());
    assertTrue(actual.contains(neighbor2));
  }

  @Test
  @DisplayName("getNearestAvailablePoints - calls getNearestAvailablePoints for each point")
  void getNearestAvailablePoints_callsNearestAvailableForEachPoint() {
    // given
    Point point1 = mock(Point.class);
    Point point2 = mock(Point.class);
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);
    Point neighbor3 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(subject)
        .getNearestAvailablePoints(point1);
    doReturn(List.of(neighbor3)).when(subject)
        .getNearestAvailablePoints(point2);
    doCallRealMethod().when(subject)
        .getNearestAvailablePoints(List.of(point1, point2));

    // when
    List<Point> actual = subject.getNearestAvailablePoints(List.of(point1, point2));

    // then
    verify(subject).getNearestAvailablePoints(point1);
    verify(subject).getNearestAvailablePoints(point2);
    assertEquals(3, actual.size());
    assertTrue(actual.contains(neighbor1));
    assertTrue(actual.contains(neighbor2));
    assertTrue(actual.contains(neighbor3));
  }

  @Test
  @DisplayName("getNearestAvailablePoints - removes duplicating neighbors")
  void getNearestAvailablePoints_removesDuplicatingNeighbors() {
    // given
    Point point1 = mock(Point.class);
    Point point2 = mock(Point.class);
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(subject)
        .getNearestAvailablePoints(point1);
    doReturn(List.of(neighbor2)).when(subject)
        .getNearestAvailablePoints(point2);
    doCallRealMethod().when(subject)
        .getNearestAvailablePoints(List.of(point1, point2));

    // when
    List<Point> actual = subject.getNearestAvailablePoints(List.of(point1, point2));

    // then
    assertEquals(2, actual.size());
    assertTrue(actual.contains(neighbor1));
    assertTrue(actual.contains(neighbor2));
  }

  @Test
  @DisplayName("getNearestPointWithMark - gets nearest points by Cursor")
  void getNearestPointWithMark_getsNearestPointsByCursor() {
    // given
    Point point1 = mock(Point.class);
    Point point2 = mock(Point.class);
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(subject)
        .getNearestAvailablePoints(point1);
    doReturn(List.of(neighbor2)).when(subject)
        .getNearestAvailablePoints(point2);
    doCallRealMethod().when(subject)
        .getNearestAvailablePoints(List.of(point1, point2));

    // when
    List<Point> actual = subject.getNearestAvailablePoints(List.of(point1, point2));

    // then
    assertEquals(2, actual.size());
    assertTrue(actual.contains(neighbor1));
    assertTrue(actual.contains(neighbor2));
  }

  @Test
  @DisplayName("getNearestPointWithMark - gets point with mark")
  void getNearestPointWithMark_getsPointThatContainsMark() {
    // given
    int mark = 3;
    int wrongMark = 2;
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(cursor)
        .getNearestPoints(start);
    doReturn(wrongMark).when(field).get(neighbor1);
    doReturn(mark).when(field).get(neighbor2);

    // when
    Point actual = subject.getNearestPointWithMark(start, mark);

    // then
    verify(cursor).getNearestPoints(start);
    assertEquals(neighbor2, actual);
  }

  @Test
  @DisplayName("getNearestPointWithMark - throws error if no mark near")
  void getNearestPointWithMark_throwsErrorIfNoMarkNear() {
    // given
    int mark = 5;
    int wrongMark1 = 3;
    int wrongMark2 = 2;
    Point neighbor1 = mock(Point.class);
    Point neighbor2 = mock(Point.class);

    doReturn(List.of(neighbor1, neighbor2)).when(cursor)
        .getNearestPoints(start);
    doReturn(wrongMark2).when(field).get(neighbor1);
    doReturn(wrongMark1).when(field).get(neighbor2);

    // when & then
    assertThrows(AlgorithmErrorException.class,
        () -> subject.getNearestPointWithMark(start, mark));
  }

  @Test
  @DisplayName("traceRouteToStart - creates route to point following marks")
  void traceRouteToStart_createsRouteFollowingMarks() {
    // given
    int step = 3;
    Point point2 = mock(Point.class);
    Point point3 = mock(Point.class);
    Point point4 = mock(Point.class);

    doReturn(point2).when(subject)
        .getNearestPointWithMark(start, step);
    doReturn(point3).when(subject)
        .getNearestPointWithMark(point2, step - 1);
    doReturn(point4).when(subject)
        .getNearestPointWithMark(point3, step - 2);

    // when
    List<Point> actual = subject.traceRouteToStart(new int[]{step}, start);

    // then
    List<Point> expected = List.of(point4, point3, point2, start);
    assertEquals(expected, actual);
    verify(subject).getNearestPointWithMark(start, step);
    verify(subject).getNearestPointWithMark(point2, step - 1);
    verify(subject).getNearestPointWithMark(point3, step - 2);
  }

  @Test
  @DisplayName("getShortestRouteToTarget - creates shortest route to target")
  void getShortestRouteToTarget_createsShortestRouteToTarget() {
    int[] expectedSteps = {2};
    Point target = mock(Point.class);
    Point point1 = mock(Point.class);
    Point point2 = mock(Point.class);
    Point point3 = mock(Point.class);
    List<Point> expected = List.of(point1, point3, target);

    doReturn(List.of(point1, point2)).when(subject).getNearestAvailablePoints(List.of(start));
    doReturn(List.of(point3)).when(subject).getNearestAvailablePoints(List.of(point1, point2));
    doReturn(List.of(target)).when(subject).getNearestAvailablePoints(List.of(point3));
    doReturn(expected).when(subject).traceRouteToStart(expectedSteps, target);

    // when
    List<Point> actual = subject.getShortestRouteToTarget(start, target);

    // then
    assertEquals(expected, actual);
    verify(subject, times(3))
        .getNearestAvailablePoints(anyList());
  }

  @Test
  @DisplayName("getShortestRouteToTarget - marks field cells on the route")
  void getShortestRouteToTarget_marksFieldSells() {
    int[] expectedSteps = {2};
    Point target = mock(Point.class);
    Point point1 = mock(Point.class);
    Point point2 = mock(Point.class);
    Point point3 = mock(Point.class);

    doReturn(List.of(point1, point2)).when(subject).getNearestAvailablePoints(List.of(start));
    doReturn(List.of(point3)).when(subject).getNearestAvailablePoints(List.of(point1, point2));
    doReturn(List.of(target)).when(subject).getNearestAvailablePoints(List.of(point3));
    doReturn(Collections.emptyList()).when(subject).traceRouteToStart(expectedSteps, target);

    // when
    subject.getShortestRouteToTarget(start, target);

    // then
    verify(field).set(point1, 1);
    verify(field).set(point2, 1);
    verify(field).set(point3, 2);
  }

  @Test
  @DisplayName("getShortestRouteToTarget - returns empty list if no route found")
  void getShortestRouteToTarget_returnsEmptyListIfNotFound() {
    Point target = mock(Point.class);
    Point point1 = mock(Point.class);
    Point point2 = mock(Point.class);
    Point point3 = mock(Point.class);

    doReturn(List.of(point1, point2)).when(subject).getNearestAvailablePoints(List.of(start));
    doReturn(List.of(point3)).when(subject).getNearestAvailablePoints(List.of(point1, point2));
    doReturn(Collections.emptyList()).when(subject).getNearestAvailablePoints(List.of(point3));

    // when
    List<Point> actual = subject.getShortestRouteToTarget(start, target);

    // then
    assertTrue(actual.isEmpty());
  }
}