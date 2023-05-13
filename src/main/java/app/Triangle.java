package app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import Misc.Vector2d;

import java.util.Objects;

public class Triangle {
    /**
     * Координаты 1-ой точки
     */
    public Vector2d point1;

    /**
     * Координаты 2-ой точки
     */
    public Vector2d point2;

    /**
     * Координаты 3-ой точки
     */
    public Vector2d point3;

    /**
     * Конструктор треугольника
     *
     * @param point1 положение 1-ой точки
     * @param point2 положение 2-ой точки
     * @param point3 положение 3-ой точки
     */
    @JsonCreator
    public Triangle(@JsonProperty("point1") Vector2d point1, @JsonProperty("point2") Vector2d point2, @JsonProperty("point3") Vector2d point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    /**
     * Получить положение 1-ой точки
     *
     * @return point1
     */
    public Vector2d getPoint1() {
        return point1;
    }

    /**
     * Получить положение 2-ой точки
     *
     * @return point2
     */
    public Vector2d getPoint2() {
        return point2;
    }

    /**
     * Получить положение 3-ой точки
     *
     * @return point3
     */
    public Vector2d getPoint3() {
        return point3;
    }

    /**
     * Строковое представление объекта
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return "Triangle{" +
                "point1=" + point1 + ", point2=" + point2 + ", point3=" + point3 +
                '}';
    }

    /**
     * Проверка двух объектов на равенство
     *
     * @param o объект, с которым сравниваем текущий
     * @return флаг, равны ли два объекта
     */
    @Override
    public boolean equals(Object o) {
        // если объект сравнивается сам с собой, тогда объекты равны
        if (this == o) return true;
        // если в аргументе передан null или классы не совпадают, тогда объекты не равны
        if (o == null || getClass() != o.getClass()) return false;
        // приводим переданный в параметрах объект к текущему классу
        Triangle triangle = (Triangle) o;
        return (point1 == triangle.point1) && (point2 == triangle.point2) && (point3 == triangle.point3);
    }

    /**
     * Получить хэш-код объекта
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(point1, point2, point3);
    }
}
