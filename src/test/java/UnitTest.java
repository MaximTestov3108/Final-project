import app.Point;
import app.Ray;
import app.Task;
import app.Triangle;
import Misc.CoordinateSystem2d;
import Misc.Vector2d;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс тестирования
 */
public class UnitTest {

    /**
     * Тест
     *
     * @param points        список точек
     * @param rays список лучей
     * @param triangles  список треугольников
     */
    private static void test(ArrayList<Point> points, ArrayList<Ray> rays, ArrayList<Triangle> triangles, int num) {
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), triangles, rays, points);
        int max = -1;
        for (Triangle t : triangles) {
            for (Ray r : rays) {
                int c = 0;
                for (Point p : points) {
                    if (task.check_tri(t, p) && task.check_ray(r, p, task.lastWindowCS))
                        c++;

                }
                if (c > max) {
                    max = c;
                }
            }
        }
        assert max == num;
    }


    /**
     * Первый тест
     */
    @Test
    public void test1() {
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Ray> rays = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(1, 2)));
        points.add(new Point(new Vector2d(0, 0)));
        rays.add(new Ray(new Vector2d(-1, -4), new Vector2d(3, -4)));
        triangles.add(new Triangle(new Vector2d(0,0), new Vector2d(3, 0), new Vector2d(0, 4)));
        triangles.add(new Triangle(new Vector2d(-8,0), new Vector2d(-5, 0), new Vector2d(-3, 6)));



        test(points, rays, triangles, 3);
    }

    /**
     * Второй тест
     */
    @Test
    public void test2() {
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Ray> rays = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(5, 2)));
        points.add(new Point(new Vector2d(-3, -1)));
        points.add(new Point(new Vector2d(0, 0)));
        rays.add(new Ray(new Vector2d(5, 4), new Vector2d(-5, -2)));
        triangles.add(new Triangle(new Vector2d(-4,-2), new Vector2d(-4, 7), new Vector2d(8, 1)));
        triangles.add(new Triangle(new Vector2d(-8,0), new Vector2d(-5, 0), new Vector2d(-3, 6)));



        test(points, rays, triangles, 4);
    }

    /**
     * Второй тест
     */
    @Test
    public void test3() {
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Ray> rays = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        points.add(new Point(new Vector2d(1, 1)));
        points.add(new Point(new Vector2d(5, 2)));
        points.add(new Point(new Vector2d(-3, -1)));
        points.add(new Point(new Vector2d(0, 0)));
        points.add(new Point(new Vector2d(1, -2)));
        points.add(new Point(new Vector2d(3, -6)));
        rays.add(new Ray(new Vector2d(5, -4), new Vector2d(-5, -2)));
        rays.add(new Ray(new Vector2d(2, -4), new Vector2d(3, -4)));
        rays.add(new Ray(new Vector2d(4, -5), new Vector2d(2, -6)));
        triangles.add(new Triangle(new Vector2d(-4,6), new Vector2d(-2, 5), new Vector2d(3, 3)));
        triangles.add(new Triangle(new Vector2d(-8,5), new Vector2d(-5, -3), new Vector2d(-3, 6)));



        test(points, rays, triangles, 0);
    }

}
