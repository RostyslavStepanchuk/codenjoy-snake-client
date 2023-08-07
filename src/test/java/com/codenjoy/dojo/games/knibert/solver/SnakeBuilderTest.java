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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.games.knibert.solver.exceptions.AlgorithmErrorException;
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
class SnakeBuilderTest {

  @Spy
  @InjectMocks
  SnakeBuilder subject;

  @Mock
  Cursor cursor;

  @Test
  @DisplayName("tailDirections - uses proper Directions for snake elements")
  void tailDirections_readsElementsProperly() {
    assertEquals(List.of(UP), SnakeBuilder.tailDirections.get(HEAD_DOWN));
    assertEquals(List.of(DOWN), SnakeBuilder.tailDirections.get(HEAD_UP));
    assertEquals(List.of(RIGHT), SnakeBuilder.tailDirections.get(HEAD_LEFT));
    assertEquals(List.of(LEFT), SnakeBuilder.tailDirections.get(HEAD_RIGHT));
    assertEquals(List.of(LEFT, RIGHT), SnakeBuilder.tailDirections.get(TAIL_HORIZONTAL));
    assertEquals(List.of(UP, DOWN), SnakeBuilder.tailDirections.get(TAIL_VERTICAL));
    assertEquals(List.of(LEFT, DOWN), SnakeBuilder.tailDirections.get(TAIL_LEFT_DOWN));
    assertEquals(List.of(LEFT, UP), SnakeBuilder.tailDirections.get(TAIL_LEFT_UP));
    assertEquals(List.of(RIGHT, DOWN), SnakeBuilder.tailDirections.get(TAIL_RIGHT_DOWN));
    assertEquals(List.of(RIGHT, UP), SnakeBuilder.tailDirections.get(TAIL_RIGHT_UP));
    assertEquals(Collections.emptyList(), SnakeBuilder.tailDirections.get(TAIL_END_DOWN));
    assertEquals(Collections.emptyList(), SnakeBuilder.tailDirections.get(TAIL_END_LEFT));
    assertEquals(Collections.emptyList(), SnakeBuilder.tailDirections.get(TAIL_END_UP));
    assertEquals(Collections.emptyList(), SnakeBuilder.tailDirections.get(TAIL_END_RIGHT));
  }

  @Test
  @DisplayName("getSnakeFromBoard - gets points in direction of elements")
  void getSnakeFromBoard_getsPointsInDirectionOfElements() {
    // given
    Board board = mock(Board.class);
    Point head = mock(Point.class);
    Point tail1 = mock(Point.class);
    List<Point> boardSnake = List.of(head, tail1);

    doReturn(true).when(subject).isNextTailPoint(any(), any());
    doReturn(boardSnake).when(board).getHero();

    doReturn(head).when(board).getHead();
    doReturn(HEAD_UP).when(board).getAt(head);
    doReturn(tail1).when(cursor).moveToDirection(head, DOWN);
    doReturn(TAIL_END_UP).when(board).getAt(tail1);

    // when
    subject.getSnakeFromBoard(board);

    // then
    InOrder inOrder = Mockito.inOrder(board, cursor);
    inOrder.verify(board).getAt(head);
    inOrder.verify(cursor).moveToDirection(head, DOWN);
  }

  @Test
  @DisplayName("getSnakeFromBoard - collects snake from head to tail")
  void getSnakeFromBoard_collectsSnakeFromHeadToTail() {
    // given
    Board board = mock(Board.class);
    Point head = mock(Point.class);
    Point tail1 = mock(Point.class);
    List<Point> wrongOrderSnake = List.of(tail1, head);

    doReturn(true).when(subject).isNextTailPoint(any(), any());
    doReturn(wrongOrderSnake).when(board).getHero();

    doReturn(head).when(board).getHead();
    doReturn(HEAD_UP).when(board).getAt(head);
    doReturn(tail1).when(cursor).moveToDirection(head, DOWN);
    doReturn(TAIL_END_UP).when(board).getAt(tail1);

    // when
    LinkedList<Point> actual = subject.getSnakeFromBoard(board);

    // then
    assertEquals(head, actual.getFirst());
    assertEquals(tail1, actual.getLast());
  }

  @Test
  @DisplayName("getSnakeFromBoard - checks each point if its valid for next tail point")
  void getSnakeFromBoard_checkValidityOfEachPoint() {
    // given
    Board board = mock(Board.class);
    Point head = mock(Point.class);
    Point tail1 = mock(Point.class);
    List<Point> wrongOrderSnake = List.of(head);

    doReturn(wrongOrderSnake).when(board).getHero();

    doReturn(head).when(board).getHead();
    doReturn(HEAD_UP).when(board).getAt(head);
    doReturn(tail1).when(cursor).moveToDirection(head, DOWN);
    doReturn(false).when(subject).isNextTailPoint(eq(tail1), any());

    // when
    LinkedList<Point> actual = subject.getSnakeFromBoard(board);

    // then
    assertEquals(1, actual.size());
    verify(subject).isNextTailPoint(eq(tail1), any());
  }

  @Test
  @DisplayName("getSnakeFromBoard - throws error if collected snake smaller than from board")
  void getSnakeFromBoard_throwsErrorIfCollectedSnakeIsSmallerThanBoard() {
    // given
    Board board = mock(Board.class);
    Point head = mock(Point.class);
    Point tail1 = mock(Point.class);
    List<Point> boardSnake = List.of(tail1, head, mock(Point.class));

    doReturn(true).when(subject).isNextTailPoint(any(), any());
    doReturn(boardSnake).when(board).getHero();

    doReturn(head).when(board).getHead();
    doReturn(HEAD_UP).when(board).getAt(head);
    doReturn(tail1).when(cursor).moveToDirection(head, DOWN);
    doReturn(TAIL_END_UP).when(board).getAt(tail1);

    // when
    assertThrows(AlgorithmErrorException.class, () -> subject.getSnakeFromBoard(board));
  }


  @Test
  @DisplayName("isNextTailPoint - returns true if point not equal to the one before the last")
  void isNextTailPoint_trueIfNotTheOneBeforeTheLastOne() {
    Point head = mock(Point.class);
    Point tail1 = mock(Point.class);
    Point tail2 = mock(Point.class);
    Point tail3 = mock(Point.class);
    List<Point> snake = List.of(head, tail1, tail2);
    assertTrue(subject.isNextTailPoint(tail3, snake));
  }

  @Test
  @DisplayName("isNextTailPoint - returns false if point equals to the one before the last")
  void isNextTailPoint_falseIfTheOneBeforeTheLastOne() {
    Point head = mock(Point.class);
    Point tail1 = mock(Point.class);
    Point tail2 = mock(Point.class);
    List<Point> snake = List.of(head, tail1, tail2);
    assertFalse(subject.isNextTailPoint(tail1, snake));
  }

  @Test
  @DisplayName("isNextTailPoint - returns true if only head collected yet")
  void isNextTailPoint_trueIfSnakeHasOnlyHeadYet() {
    assertTrue(subject.isNextTailPoint(null, List.of(mock(Point.class))));
  }
}