package app;

import Misc.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.*;
import lombok.Getter;
import panels.PanelLog;

import java.security.KeyStore;
import java.util.ArrayList;

import static app.Colors.*;

/**
 * Класс задачи
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество "широких лучей" и множество треугольников. 
             Найти такую пару "широкий луч"-треугольник, что фигура, находящаяся 
             внутри "широкого луча" и треугольника, имеет максимальную площадь. """;

    /**
     * Вещественная система координат задачи
     */
    @Getter
    private final CoordinateSystem2d ownCS;
    /**
     * Список треугольников
     */
    @Getter
    private final ArrayList<Triangle> triangles;
    /**
     * Список лучей
     */
    @Getter
    private final ArrayList<Ray> rays;

    /**
     * Список точек для треугольников
     */
    private static Vector2d[] ct = new Vector2d[3];

    /**
     * подсчет точек для треугольников
     */
    private static int ctn = 0;

    private static Triangle t1;

    private static Ray r1;

    /**
     * Список точек для лучей
     */
    private static Vector2d[] cl = new Vector2d[2];

    /**
     * подсчет точек для лучей
     */
    private static int cln = 0;

    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 2;
    /**
     * последняя СК окна
     */
    public CoordinateSystem2i lastWindowCS;
    /**
     * Флаг, решена ли задача
     */
    private boolean solved;
    /**
     * Порядок разделителя сетки, т.е. раз в сколько отсечек
     * будет нарисована увеличенная
     */
    private static final int DELIMITER_ORDER = 10;
    /**
     * коэффициент колёсика мыши
     */
    private static final float WHEEL_SENSITIVE = 0.001f;

    /**
     * Список точек
     */
    @Getter
    private final ArrayList<Point> points;


    /**
     * Задача
     *
     * @param ownCS     СК задачи
     * @param triangles массив точек
     * @param points    массив точек
     */
    @JsonCreator
    public Task(
            @JsonProperty("ownCS") CoordinateSystem2d ownCS,
            @JsonProperty("triangles") ArrayList<Triangle> triangles,
            @JsonProperty("rays") ArrayList<Ray> rays,
            @JsonProperty("points") ArrayList<Point> points

    ) {
        this.ownCS = ownCS;
        this.triangles = triangles;
        this.rays = rays;
        this.points = points;
    }

    /**
     * Рисование
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;
        // рисуем координатную сетку
        renderGrid(canvas, lastWindowCS);
        // рисуем задачу
        renderTask(canvas, windowCS);
    }


    /**
     * Рисование луча
     *
     * @param pos1 положение первой точки
     * @param pos2 положение второй точки
     */
    public void paintRay(Canvas canvas, Vector2i pos1, Vector2i pos2, Paint p, int maxDist) {
        // отрезок AB
        Vector2i AB = Vector2i.subtract(pos1, pos2);

        // создаём вектор направления для рисования условно бесконечной полосы
        Vector2d dir = new Vector2d(AB);
        //System.out.print("fx:"+dir.x+" fy:"+dir.y);
        dir = dir.rotated(Math.PI / 2).norm();
        dir.mult(maxDist);
        Vector2i direction = new Vector2i((int) dir.x, (int) dir.y);
        // получаем точки рисования
        //System.out.println(" x:"+dir.x+" y:"+dir.y);
        Vector2i renderPointC = Vector2i.sum(pos1, direction);
        Vector2i renderPointD = Vector2i.sum(pos2, direction);

        // рисуем отрезки
        float oldW = p.getStrokeWidth();
        p.setStrokeWidth(3);
        canvas.drawLine(pos1.x, pos1.y, pos2.x, pos2.y, p);
        canvas.drawLine(pos1.x, pos1.y, renderPointC.x, renderPointC.y, p);
        canvas.drawLine(pos2.x, pos2.y, renderPointD.x, renderPointD.y, p);
        p.setStrokeWidth(oldW);
        // сохраняем цвет рисования
        int paintColor = p.getColor();
        // задаём красный цвет
        p.setColor(Misc.getColor(200, 255, 0, 0));
        // рисуем исходные точки
        canvas.drawRRect(RRect.makeXYWH(pos1.x - 3, pos1.y - 3, 6, 6, 3), p);
        canvas.drawRRect(RRect.makeXYWH(pos2.x - 3, pos2.y - 3, 6, 6, 3), p);
        // восстанавливаем исходный цвет рисования
        p.setColor(paintColor);


    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    private void renderTask(Canvas canvas, CoordinateSystem2i windowCS) {
        canvas.save();
        // создаём перо
        try (var paint = new Paint()) {
            for (Triangle t : triangles) {
                paint.setColor(TRIANGLE_COLOR);
                // y-координату разворачиваем, потому что у СК окна ось y направлена вниз,
                // а в классическом представлении - вверх
                Vector2i Pos1 = windowCS.getCoords(t.point1.x, t.point1.y, ownCS);
                canvas.drawRRect(RRect.makeXYWH(Pos1.x - POINT_SIZE * 2, lastWindowCS.getMax().y - (Pos1.y + POINT_SIZE * 2), POINT_SIZE * 2, POINT_SIZE * 2, POINT_SIZE), paint);
                Vector2i Pos2 = windowCS.getCoords(t.point2.x, t.point2.y, ownCS);
                canvas.drawRRect(RRect.makeXYWH(Pos2.x - POINT_SIZE * 2, lastWindowCS.getMax().y - (Pos2.y + POINT_SIZE * 2), POINT_SIZE * 2, POINT_SIZE * 2, POINT_SIZE), paint);
                Vector2i Pos3 = windowCS.getCoords(t.point3.x, t.point3.y, ownCS);
                canvas.drawRRect(RRect.makeXYWH(Pos3.x - POINT_SIZE * 2, lastWindowCS.getMax().y - (Pos3.y + POINT_SIZE * 2), POINT_SIZE * 2, POINT_SIZE * 2, POINT_SIZE), paint);

                canvas.drawLine((float) Pos1.x - POINT_SIZE, (float) lastWindowCS.getMax().y - (Pos1.y + POINT_SIZE), (float) Pos2.x - POINT_SIZE, (float) lastWindowCS.getMax().y - (Pos2.y + POINT_SIZE), paint);
                canvas.drawLine((float) Pos3.x - POINT_SIZE, (float) lastWindowCS.getMax().y - (Pos3.y + POINT_SIZE), (float) Pos2.x - POINT_SIZE, (float) lastWindowCS.getMax().y - (Pos2.y + POINT_SIZE), paint);
                canvas.drawLine((float) Pos1.x - POINT_SIZE, (float) lastWindowCS.getMax().y - (Pos1.y + POINT_SIZE), (float) Pos3.x - POINT_SIZE, (float) lastWindowCS.getMax().y - (Pos3.y + POINT_SIZE), paint);
            }
            for (Ray r : rays) {
                paint.setColor(RAY_COLOR);

                // а в классическом представлении - вверх
                Vector2i windowPos1 = windowCS.getCoords(r.pos1.x, r.pos1.y, ownCS);
                Vector2i windowPos2 = windowCS.getCoords(r.pos2.x, r.pos2.y, ownCS);
                windowPos1.y = lastWindowCS.getMax().y - windowPos1.y;
                windowPos2.y = lastWindowCS.getMax().y - windowPos2.y;

                // рисуем луч
                // получаем максимальную длину отрезка на экране, как длину диагонали экрана
                int maxDistance = (int) windowCS.getSize().length();
                paintRay(canvas, windowPos1, windowPos2, paint, maxDistance);
            }

            for (Point p : points){
                if (check_tri(t1, p) && check_ray(r1, p, lastWindowCS))
                    paint.setColor(CROSSED_COLOR);
                else
                    paint.setColor(SUBTRACTED_COLOR);
                Vector2i Pos1 = windowCS.getCoords(p.pos.x, p.pos.y, ownCS);
                canvas.drawRRect(RRect.makeXYWH(Pos1.x - POINT_SIZE * 2, lastWindowCS.getMax().y - (Pos1.y + POINT_SIZE * 2), POINT_SIZE * 2, POINT_SIZE * 2, POINT_SIZE), paint);
            }


        }

        canvas.restore();
    }

    /**
     * Рисование сетки
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void renderGrid(Canvas canvas, CoordinateSystem2i windowCS) {
        // сохраняем область рисования
        canvas.save();
        // получаем ширину штриха(т.е. по факту толщину линии)
        float strokeWidth = 0.03f / (float) ownCS.getSimilarity(windowCS).y + 0.5f;
        // создаём перо соответствующей толщины
        try (var paint = new Paint().setMode(PaintMode.STROKE).setStrokeWidth(strokeWidth).setColor(TASK_GRID_COLOR)) {
            // перебираем все целочисленные отсчёты нашей СК по оси X
            for (int i = (int) (ownCS.getMin().x); i <= (int) (ownCS.getMax().x); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(i, 0, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % DELIMITER_ORDER == 0 ? 5 : 2;
                // рисуем вертикальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y + strokeHeight, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x, windowPos.y - strokeHeight, paint);
            }
            // перебираем все целочисленные отсчёты нашей СК по оси Y
            for (int i = (int) (ownCS.getMin().y); i <= (int) (ownCS.getMax().y); i++) {
                // находим положение этих штрихов на экране
                Vector2i windowPos = windowCS.getCoords(0, i, ownCS);
                // каждый 10 штрих увеличенного размера
                float strokeHeight = i % 10 == 0 ? 5 : 2;
                // рисуем горизонтальный штрих
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x + strokeHeight, windowPos.y, paint);
                canvas.drawLine(windowPos.x, windowPos.y, windowPos.x - strokeHeight, windowPos.y, paint);
            }
        }
        // восстанавливаем область рисования
        canvas.restore();
    }


    /**
     * Рисование курсора мыши
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     * @param font     шрифт
     * @param pos      положение курсора мыши
     */
    public void paintMouse(Canvas canvas, CoordinateSystem2i windowCS, Font font, Vector2i pos) {
        // создаём перо
        try (var paint = new Paint().setColor(TASK_GRID_COLOR)) {
            // сохраняем область рисования
            canvas.save();
            // рисуем перекрестие
            canvas.drawRect(Rect.makeXYWH(0, pos.y - 1, windowCS.getSize().x, 2), paint);
            canvas.drawRect(Rect.makeXYWH(pos.x - 1, 0, 2, windowCS.getSize().y), paint);
            // смещаемся немного для красивого вывода текста
            canvas.translate(pos.x + 3, pos.y - 5);
            // положение курсора в пространстве задачи
            Vector2d realPos = getRealPos(pos.x, pos.y, lastWindowCS);
            // выводим координаты
            canvas.drawString(realPos.toString(), 0, 0, font, paint);
            // восстанавливаем область рисования
            canvas.restore();
        }
    }


    /**
     * Масштабирование области просмотра задачи
     *
     * @param delta  прокрутка колеса
     * @param center центр масштабирования
     */
    public void scale(float delta, Vector2i center) {
        if (lastWindowCS == null) return;
        // получаем координаты центра масштабирования в СК задачи
        Vector2d realCenter = ownCS.getCoords(center, lastWindowCS);
        // выполняем масштабирование
        ownCS.scale(1 - delta * WHEEL_SENSITIVE, realCenter);
    }


    /**
     * Получить положение курсора мыши в СК задачи
     *
     * @param x        координата X курсора
     * @param y        координата Y курсора
     * @param windowCS СК окна
     * @return вещественный вектор положения в СК задачи
     */
    @JsonIgnore
    public Vector2d getRealPos(int x, int y, CoordinateSystem2i windowCS) {
        return ownCS.getCoords(x, y, windowCS);
    }

    /**
     * Клик мыши по пространству задачи
     *
     * @param pos         положение мыши
     * @param mouseButton кнопка мыши
     */
    public void click(Vector2i pos, MouseButton mouseButton) {
        if (lastWindowCS == null) return;
        // переворачиваем y
        Vector2i pos1 = new Vector2i(pos.x, lastWindowCS.getMax().y - pos.y);
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos1, lastWindowCS);
        // если левая кнопка мыши, добавляем в первое множество
        if (mouseButton.equals(MouseButton.PRIMARY)) {
            if (ctn == 2) {
                ct[2] = taskPos;
                addTriangle(ct[0], ct[1], ct[2]);
                ct = new Vector2d[3];
                ctn = 0;
            } else if (ctn == 0) {
                ct[0] = taskPos;
                ctn++;
            } else if (ctn == 1) {
                ct[1] = taskPos;
                ctn++;
            }


            //addTriangle();
            // если правая, то во второе
        } else if (mouseButton.equals(MouseButton.SECONDARY)) {
            // addPoint(taskPos, Point.PointSet.SECOND_SET);
            if (cln == 0) {
                cl[0] = taskPos;
                cln++;
            } else if (cln == 1) {
                cl[1] = taskPos;
                addRay(cl[0], cl[1]);
                cln = 0;
                cl = new Vector2d[2];
            }
        }
    }

    public void addTriangle(Vector2d point1, Vector2d point2, Vector2d point3) {
        solved = false;
        Triangle newTriangle = new Triangle(point1, point2, point3);
        triangles.add(newTriangle);
        PanelLog.info("треугольник " + newTriangle + " добавлен в задачу");
    }

    /**
     * Добавить точку
     *
     * @param pos      положение
     *
     */
    public void addPoint(Vector2d pos) {
        solved = false;
        Point newPoint = new Point(pos);
        points.add(newPoint);
    }


    /**
     * Добавить случайные треугольники
     *
     * @param cnt кол-во случайных треугольников
     */
    public void addRandomTriangles(int cnt) {
        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты вершин
            Vector2d pos1 = ownCS.getRandomCoords();
            Vector2d pos2 = ownCS.getRandomCoords();
            while (pos1 == pos2)
                pos2 = ownCS.getRandomCoords();
            Vector2d pos3 = ownCS.getRandomCoords();
            while (pos1 == pos3 || pos2 == pos3)
                pos3 = ownCS.getRandomCoords();

            addTriangle(pos1, pos2, pos3);
        }
    }

    /**
     * Добавить Луч
     *
     * @param pos1 положение первой точки
     * @param pos2 положение второй точки
     */
    public void addRay(Vector2d pos1, Vector2d pos2) {
        solved = false;
        Ray newRay = new Ray(pos1, pos2);
        rays.add(newRay);
        PanelLog.info("луч " + newRay + " добавлен в задачу");
    }

    /**
     * Добавить случайные лучи
     *
     * @param cnt кол-во случайных лучей
     */
    public void addRandomRays(int cnt) {
        // повторяем заданное количество раз
        for (int i = 0; i < cnt; i++) {
            // получаем случайные координаты для двух точек
            Vector2d pos1 = ownCS.getRandomCoords();
            Vector2d pos2 = ownCS.getRandomCoords();
            addRay(pos1, pos2);
        }
    }

    /**
     * Очистить задачу
     */
    public void clear() {
        //points.clear();
        rays.clear();
        triangles.clear();
        points.clear();
        solved = false;
    }

    /**
     * Решить задачу
     */
    public void solve() {
        // выделяем область в которой будем раскидывать точки
        if (triangles.size() != 0 && rays.size() != 0) {
            int[] na = new int[triangles.size()];
            int[] da = new int[rays.size()];

            for (int i = 0; i < 500; i++) {
                Vector2d pos = ownCS.getRandomCoords();
                addPoint(pos);
            }
            int max = -1;
            for (Triangle t : triangles) {
                for (Ray r : rays) {
                    int c = 0;
                    for (Point p : points) {
                        if (check_tri(t, p) && check_ray(r, p, lastWindowCS))
                            c++;

                    }
                    if (c > max) {
                        max = c;
                        t1 = t;
                        r1 = r;
                    }
                }
            }

            // задача решена
            solved = true;
        } else{
            PanelLog.info("добавьте лучи и треугольники");
        }
    }

    /**
     * Отмена решения задачи
     */
    public void cancel() {
        solved = false;
    }

    /**
     * проверка, решена ли задача
     *
     * @return флаг
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     *
     * лежит ли точка в треугольнике
     */
    public boolean check_tri(Triangle a, Point b) {
        double st = Math.abs((a.point2.x - a.point1.x) * (a.point3.y - a.point1.y) - (a.point3.x - a.point1.x) * (a.point2.y - a.point1.y));
        double st1 = Math.abs((a.point2.x - b.pos.x) * (a.point3.y - b.pos.y) - (a.point3.x - b.pos.x) * (a.point2.y - b.pos.y));
        double st2 = Math.abs((b.pos.x - a.point1.x) * (a.point3.y - a.point1.y) - (a.point3.x - a.point1.x) * (b.pos.y - a.point1.y));
        double st3 = Math.abs((a.point2.x - a.point1.x) * (b.pos.y - a.point1.y) - (b.pos.x - a.point1.x) * (a.point2.y - a.point1.y));
        if (Math.abs(st - (st1 + st2 + st3)) < 0.000005)
            return true;
        else
            return false;
    }

    /**
     *
     * лежит ли точка в луче
     */
    public boolean check_ray(Ray a, Point b , CoordinateSystem2i windowCS) {
        Vector2d AB = Vector2d.subtract(a.pos1, a.pos2);

        // создаём вектор направления для рисования условно бесконечной полосы
        Vector2d dir = AB;
        //System.out.print("fx:"+dir.x+" fy:"+dir.y);
        dir = dir.rotated(Math.PI / 2 + Math.PI).norm();
        dir.mult(50);
        Vector2d direction = new Vector2d((int) dir.x, (int) dir.y);
        // получаем точки рисования
        //System.out.println(" x:"+dir.x+" y:"+dir.y);
        Vector2d renderPointC = Vector2d.sum(a.pos1, direction);
        Vector2d renderPointD = Vector2d.sum(a.pos2, direction);

        double sta = Math.abs((a.pos2.x - a.pos1.x) * (renderPointC.y - a.pos1.y) - (renderPointC.x - a.pos1.x) * (a.pos2.y - a.pos1.y)) / 2;
        double stb = Math.abs((a.pos2.x - renderPointD.x) * (renderPointC.y - renderPointD.y) - (renderPointC.x - renderPointD.x) * (a.pos2.y - renderPointD.y)) / 2;
        double st1 = Math.abs((a.pos2.x - a.pos1.x) * (b.pos.y - a.pos1.y) - (b.pos.x - a.pos1.x) * (a.pos2.y - a.pos1.y)) / 2;
        double st2 = Math.abs((a.pos2.x - renderPointD.x) * (b.pos.y - renderPointD.y) - (b.pos.x - renderPointD.x) * (a.pos2.y - renderPointD.y)) / 2;
        double st3 = Math.abs((renderPointC.x - renderPointD.x) * (b.pos.y - renderPointD.y) - (b.pos.x - renderPointD.x) * (renderPointC.y - renderPointD.y)) / 2;
        double st4 = Math.abs((renderPointC.x - a.pos1.x) * (b.pos.y - a.pos1.y) - (b.pos.x - a.pos1.x) * (renderPointC.y - a.pos1.y)) / 2;
        if (Math.abs(sta + stb - (st1+st2+st3+st4)) < 0.00005)
            return true;
        else
            return false;
    }
}
