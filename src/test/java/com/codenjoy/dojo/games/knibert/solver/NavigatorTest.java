package com.codenjoy.dojo.games.knibert.solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NavigatorTest {

  @Spy
  @InjectMocks
  private Navigator subject;

  @Mock
  private Board board;

  @Mock
  private Cursor cursor;

  @Mock
  private LeeAlgorithm leeAlgorithm;

  @Mock
  private BoardModel field;

  @Mock
  private SnakeBuilder snakeBuilder;

  @Test
  @DisplayName("getMove - sets up field before launching algo")
  void getMove_setupsFieldBeforeAlgo() {
    // given
    doReturn(List.of(mock(Point.class))).when(board).getApples();
    doReturn(Collections.emptyList()).when(leeAlgorithm).getShortestRouteToTarget(any(), any());
    doNothing().when(subject).setupInitialField();

    // when
    subject.getMove();

    // then
    InOrder inOrder = Mockito.inOrder(subject, leeAlgorithm);
    inOrder.verify(subject).setupInitialField();
    inOrder.verify(leeAlgorithm).getShortestRouteToTarget(any(), any());
  }

  @Test
  @DisplayName("getMove - calls leeAlgorithm for shortest route")
  void getMove_callsLeeAlgo() {
    // given
    Point head = mock(Point.class);
    Point apple = mock(Point.class);

    doReturn(head).when(board).getHead();
    doReturn(List.of(apple)).when(board).getApples();
    doReturn(Collections.emptyList()).when(leeAlgorithm).getShortestRouteToTarget(any(), any());
    doNothing().when(subject).setupInitialField();

    // when & then
    subject.getMove();
    verify(leeAlgorithm).getShortestRouteToTarget(head, apple);
  }

  @Test
  @DisplayName("getMove - goes UP if leeAlgorithm returns empty route")
  void getMove_goesUpIfLeeAlgoFails() {
    doReturn(List.of(mock(Point.class))).when(board).getApples();
    doReturn(Collections.emptyList()).when(leeAlgorithm).getShortestRouteToTarget(any(), any());
    doNothing().when(subject).setupInitialField();
    assertEquals(Direction.UP.toString(), subject.getMove());
  }

  @Test
  @DisplayName("getMove - gets first point of leeAlgorithm provided route")
  void getMove_getsFirstLeeAlgoRoutePoint() {
    // given
    Point head = mock(Point.class);
    Point firstRoutePoint = mock(Point.class);
    Point secondRoutePoint = mock(Point.class);

    doReturn(List.of(mock(Point.class))).when(board).getApples();
    doReturn(head).when(board).getHead();
    doReturn(List.of(firstRoutePoint, secondRoutePoint))
        .when(leeAlgorithm)
        .getShortestRouteToTarget(any(), any());
    doReturn(Direction.DOWN).when(cursor)
        .getDirection(any(), any());
    doNothing().when(subject).setupInitialField();


    // when & then
    subject.getMove();
    verify(cursor).getDirection(head, firstRoutePoint);
  }

  @Test
  @DisplayName("getMove - returns string direction from cursor")
  void getMove_returnsStringDirectionFromCursor() {
    // given
    doReturn(List.of(mock(Point.class))).when(board).getApples();
    doReturn(List.of(mock(Point.class)))
        .when(leeAlgorithm)
        .getShortestRouteToTarget(any(), any());
    doReturn(Direction.DOWN).when(cursor)
        .getDirection(any(), any());
    doNothing().when(subject).setupInitialField();


    // when & then
    assertEquals(Direction.DOWN.toString(), subject.getMove());
  }

  @Test
  @DisplayName("setupInitialField - collects snake from board")
  void setupInitialField_collectsSnakeFromBoard() {
    // given
    LinkedList<Point> snake = new LinkedList<>(
        Arrays.asList(mock(Point.class), mock(Point.class)));
    doReturn(snake).when(snakeBuilder).getSnakeFromBoard(any());
    // when
    subject.setupInitialField();
    // then
    verify(snakeBuilder).getSnakeFromBoard(board);
    verify(field).setSnake(snake);
  }

  @Test
  @DisplayName("setupInitialField - provides field to Lee Algori")
  void setupInitialField_providesFieldToAlgorithm() {
    subject.setupInitialField();
    verify(leeAlgorithm).setField(field);
  }

}