package com.codenjoy.dojo.games.knibert.solver;

import static com.codenjoy.dojo.games.knibert.solver.BoardModel.APPLE;
import static com.codenjoy.dojo.games.knibert.solver.BoardModel.BARRIER;
import static com.codenjoy.dojo.games.knibert.solver.BoardModel.EMPTY;
import static com.codenjoy.dojo.games.knibert.solver.BoardModel.HEAD;
import static com.codenjoy.dojo.games.knibert.solver.BoardModel.SNAKE_STEP;
import static com.codenjoy.dojo.games.knibert.solver.BoardModel.STONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import com.codenjoy.dojo.games.knibert.Board;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardModelTest {

  private BoardModel subject;

  @Mock
  private Board board;

  @BeforeEach
  void setup() {
    subject = new BoardModel(board);
  }

  @Test
  @DisplayName("setEnvironment - copies barriers to model")
  void setEnvironment_setsBarriers() {
    // given
    Point barrier1 = new PointImpl(2, 2);
    Point barrier2 = new PointImpl(4, 1);
    Point barrier3 = new PointImpl(10, 7);
    doReturn(List.of(barrier1, barrier2, barrier3))
        .when(board).getBarriers();

    // when
    subject.setEnvironment(board);

    // then
    assertEquals(BARRIER, subject.get(barrier1));
    assertEquals(BARRIER, subject.get(barrier2));
    assertEquals(BARRIER, subject.get(barrier3));
  }

  @Test
  @DisplayName("setSnake - saves snake")
  void setSnake_setsSnake() {
    // given
    Point snake1 = new PointImpl(4, 8);
    Point snake2 = new PointImpl(5, 8);
    Point snake3 = new PointImpl(5, 7);
    LinkedList<Point> snake = new LinkedList<>(Arrays.asList(snake1, snake2, snake3));

    // when
    subject.setSnake(snake);

    // then
    assertEquals(snake, subject.getSnake());
  }

  @Test
  @DisplayName("setSnake - copies snake to model")
  void setSnake_setsSnakeToField() {
    // given
    Point head = new PointImpl(10, 10);
    Point tail1 = new PointImpl(9, 10);
    Point tail2 = new PointImpl(8, 10);

    // when
    subject.setSnake(new LinkedList<>(Arrays.asList(head, tail1, tail2)));

    // then
    assertEquals(HEAD, subject.get(head));
    assertEquals(HEAD + SNAKE_STEP, subject.get(tail1));
    assertEquals(HEAD + (SNAKE_STEP * 2), subject.get(tail2));
  }

  @Test
  @DisplayName("setEnvironment - copies apples to model")
  void setEnvironment_setsApples() {
    // given
    Point apple1 = new PointImpl(3, 9);
    Point apple2 = new PointImpl(10, 2);
    doReturn(List.of(apple1, apple2))
        .when(board).getApples();

    // when
    subject.setEnvironment(board);

    // then
    assertEquals(APPLE, subject.get(apple1));
    assertEquals(APPLE, subject.get(apple2));
  }

  @Test
  @DisplayName("setEnvironment - copies stones to model")
  void setEnvironment_setsStones() {
    // given
    Point stone1 = new PointImpl(3, 9);
    Point stone2 = new PointImpl(10, 2);
    doReturn(List.of(stone1, stone2))
        .when(board).getStones();

    // when
    subject.setEnvironment(board);

    // then
    assertEquals(STONE, subject.get(stone1));
    assertEquals(STONE, subject.get(stone2));
  }

  @Test
  @DisplayName("get - gets value from field")
  void get() {
    // given
    int value1 = 20;
    int value2 = -50;
    Point point1 = new PointImpl(3, 9);
    Point point2 = new PointImpl(10, 2);
    subject.set(point1, value1);
    subject.set(point2, value2);

    // when & then
    assertEquals(value1, subject.get(point1));
    assertEquals(value2, subject.get(point2));
  }

  @Test
  @DisplayName("set - sets value to field")
  void set() {
    // given
    int value1 = -35;
    int value2 = 90;
    Point point1 = new PointImpl(2, 7);
    Point point2 = new PointImpl(11, 11);

    // when
    subject.set(point1, value1);
    subject.set(point2, value2);

    // then
    assertEquals(value1, subject.get(point1));
    assertEquals(value2, subject.get(point2));

  }

  @Test
  @DisplayName("isEmpty - checks if value in cell is Empty")
  void isEmpty_checksIfEmptyCell() {
    // given
    int otherValue = 9;
    Point head = new PointImpl(1, 1);
    Point snake = new PointImpl(1, 2);
    Point barrier = new PointImpl(1, 3);
    Point apple = new PointImpl(1, 4);
    Point stone = new PointImpl(1, 5);
    Point other = new PointImpl(1, 6);
    Point empty = new PointImpl(1, 7);
    Point notSet = new PointImpl(1, 8);
    subject.set(head, HEAD);
    subject.set(snake, HEAD + SNAKE_STEP + SNAKE_STEP);
    subject.set(barrier, BARRIER);
    subject.set(apple, APPLE);
    subject.set(stone, STONE);
    subject.set(other, otherValue);
    subject.set(empty, EMPTY);

    // when & then
    assertFalse(subject.isEmpty(head));
    assertFalse(subject.isEmpty(snake));
    assertFalse(subject.isEmpty(barrier));
    assertFalse(subject.isEmpty(apple));
    assertFalse(subject.isEmpty(stone));
    assertFalse(subject.isEmpty(other));
    assertTrue(subject.isEmpty(empty));
    assertTrue(subject.isEmpty(notSet));
  }

  @Test
  @DisplayName("isApple - checks if value in cell is Apple")
  void isApple_returnsIfApple() {
    // given
    int otherValue = 9;
    Point head = new PointImpl(1, 1);
    Point snake = new PointImpl(1, 2);
    Point barrier = new PointImpl(1, 3);
    Point apple = new PointImpl(1, 4);
    Point stone = new PointImpl(1, 5);
    Point other = new PointImpl(1, 6);
    Point empty = new PointImpl(1, 7);
    Point notSet = new PointImpl(1, 8);
    subject.set(head, HEAD);
    subject.set(snake, HEAD + SNAKE_STEP + SNAKE_STEP);
    subject.set(barrier, BARRIER);
    subject.set(apple, APPLE);
    subject.set(stone, STONE);
    subject.set(other, otherValue);
    subject.set(empty, EMPTY);

    // when & then
    assertFalse(subject.isApple(head));
    assertFalse(subject.isApple(snake));
    assertFalse(subject.isApple(barrier));
    assertTrue(subject.isApple(apple));
    assertFalse(subject.isApple(stone));
    assertFalse(subject.isApple(other));
    assertFalse(subject.isApple(empty));
    assertFalse(subject.isApple(notSet));
  }

  @Test
  @DisplayName("clear - removes point value")
  void clear_removesValue() {
    Point point = new PointImpl(2, 2);
    subject.set(point, HEAD);

    // when & then
    subject.clear(point);
    assertTrue(subject.isEmpty(point));
  }
}