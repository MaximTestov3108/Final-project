package panels;

import Misc.Vector2d;
import app.Task;
import app.Point;

import java.awt.*;
import java.util.ArrayList;
import controls.Input;
import controls.InputFactory;
import controls.Label;
import controls.MultiLineLabel;
import controls.Button;
import dialogs.PanelInfo;
import io.github.humbleui.jwm.*;
import io.github.humbleui.jwm.Event;
import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import Misc.CoordinateSystem2i;
import Misc.Vector2i;

import java.util.List;

import static app.Application.PANEL_PADDING;
import static app.Colors.FIELD_BACKGROUND_COLOR;
import static app.Colors.FIELD_TEXT_COLOR;

/**
 * Панель управления
 */
public class PanelControl extends GridPanel {
    /**
     * Текст задания
     */
    MultiLineLabel task;
    /**
     * Заголовки
     */
    public List<Label> labels;
    /**
     * Поля ввода
     */
    public List<Input> inputs;
    /**
     * Кнопки
     */
    public List<Button> buttons;
    /**
     * Кнопка "решить"
     */
    private final Button solve;
    /**
     * Панель управления
     *
     * @param window     окно
     * @param drawBG     флаг, нужно ли рисовать подложку
     * @param color      цвет подложки
     * @param padding    отступы
     * @param gridWidth  кол-во ячеек сетки по ширине
     * @param gridHeight кол-во ячеек сетки по высоте
     * @param gridX      координата в сетке x
     * @param gridY      координата в сетке y
     * @param colspan    кол-во колонок, занимаемых панелью
     * @param rowspan    кол-во строк, занимаемых панелью
     */
    public PanelControl(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        // создаём списки
        inputs = new ArrayList<>();
        labels = new ArrayList<>();
        buttons = new ArrayList<>();

        // задание
        task = new MultiLineLabel(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 0, 6, 2, Task.TASK_TEXT,
                false, true);
        // добавление вручную

        // new
        int d = 11;
        Label xt1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, d, 0, 2, 1, 1, "X1", true, true);
        labels.add(xt1Label);
        Input xt1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, d, 1, 2, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xt1Field);
        Label yt1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, d, 2, 2, 1, 1, "Y1", true, true);
        labels.add(yt1Label);
        Input yt1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, d, 3, 2, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yt1Field);

        Label xt2Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, d, 0, 3, 1, 1, "X2", true, true);
        labels.add(xt2Label);
        Input xt2Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, d, 1, 3, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xt2Field);
        Label yt2Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, d, 2, 3, 1, 1, "Y2", true, true);
        labels.add(yt2Label);
        Input yt2Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, d, 3, 3, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yt2Field);

        Label xt3Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, d, 0, 4, 1, 1, "X3", true, true);
        labels.add(xt3Label);
        Input xt3Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, d, 1, 4, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(xt3Field);
        Label yt3Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, d, 2, 4, 1, 1, "Y3", true, true);
        labels.add(yt3Label);
        Input yt3Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, d, 3, 4, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(yt3Field);


        // для широких лучей
        Label x1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 4, 1, 1, "X1", true, true);
        labels.add(x1Label);
        Input x1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 4, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(x1Field);

        Label x2Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 5, 1, 1, "X2", true, true);
        labels.add(x2Label);
        Input x2Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 5, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(x2Field);

        Label y1Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 2, 4, 1, 1, "Y1", true, true);
        labels.add(y1Label);
        Input y1Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 3, 4, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(y1Field);
        Label y2Label = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 2, 5, 1, 1, "Y2", true, true);
        labels.add(y2Label);
        Input y2Field = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 3, 5, 1, 1, "0.0", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(y2Field);



        Button addTriangle = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 11, 2, 3, 6, 1, "Добавить\nтреугольник",
                true, true);
        addTriangle.setOnClick(() -> {
            // если числа введены верно
            if (!xt1Field.hasValidDoubleValue()) {
                PanelLog.warning("X1 координата введена неверно");
            } else if (!yt1Field.hasValidDoubleValue()) {
                PanelLog.warning("Y1 координата введена неверно");
            } else if (!xt2Field.hasValidDoubleValue()) {
                PanelLog.warning("X2 координата введена неверно");
            } else if (!yt2Field.hasValidDoubleValue()) {
                PanelLog.warning("Y2 координата введена неверно");
            } else if (!xt3Field.hasValidDoubleValue()) {
                PanelLog.warning("X3 координата введена неверно");
            } else if (!yt3Field.hasValidDoubleValue()) {
                PanelLog.warning("Y3 координата введена неверно");
            } else if ((xt1Field.doubleValue()==xt2Field.doubleValue() && yt1Field.doubleValue()==yt2Field.doubleValue()) ||
                    (xt1Field.doubleValue()==xt3Field.doubleValue() && yt1Field.doubleValue()==yt3Field.doubleValue()) ||
                    (xt3Field.doubleValue()==xt2Field.doubleValue() && yt3Field.doubleValue()==yt2Field.doubleValue()))
                PanelLog.warning("Одинаковые координаты");
            else {
                PanelRendering.task.addTriangle(
                        new Vector2d(xt1Field.doubleValue(), yt1Field.doubleValue()),
                        new Vector2d(xt2Field.doubleValue(), yt2Field.doubleValue()),
                        new Vector2d(xt3Field.doubleValue(), yt3Field.doubleValue())
                );
            }
        });
        buttons.add(addTriangle);

        Button addRay = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 4, 4, 2, 2, "Добавить\n луч",
                true, true);
        addRay.setOnClick(() -> {
            // если числа введены верно
            if (!x1Field.hasValidDoubleValue() || !x2Field.hasValidDoubleValue()) {
                PanelLog.warning("X координата введена неверно");
            } else if (!y1Field.hasValidDoubleValue() || !y2Field.hasValidDoubleValue())
                PanelLog.warning("Y координата введена неверно");
            else if (x1Field.doubleValue()==x2Field.doubleValue() && y1Field.doubleValue()==y2Field.doubleValue())
                PanelLog.warning("Одинаковые координаты");
            else{
               // PanelRendering.task.addPoint(
                   //     new Vector2d(xField.doubleValue(), yField.doubleValue()), Point.PointSet.SECOND_SET);
                PanelRendering.task.addRay(new Vector2d(x1Field.doubleValue(), y1Field.doubleValue()),new Vector2d(x2Field.doubleValue(), y2Field.doubleValue()));
            }
        });
        buttons.add(addRay);

        // случайное добавление
        Label cntLabel = new Label(window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 6, 1, 1, "Кол-во", true, true);
        labels.add(cntLabel);

        Input cntField = InputFactory.getInput(window, false, FIELD_BACKGROUND_COLOR, PANEL_PADDING,
                6, 9, 1, 6, 1, 1, "5", true,
                FIELD_TEXT_COLOR, true);
        inputs.add(cntField);

        Button addRandomTriangles = new Button(
                window, false,backgroundColor, PANEL_PADDING,
                6, 9, 2, 6, 2, 1, "Добавить\nслучайные треугольники",
                true, true);
        addRandomTriangles.setOnClick(() -> {
            // если числа введены верно
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во указано неверно");
            } else {
                PanelRendering.task.addRandomTriangles(cntField.intValue());
            }
        });

        Button addRandomRay = new Button(
                window, false,backgroundColor, PANEL_PADDING,
                6, 9, 4, 6, 2, 1, "Добавить\nслучайные лучи",
                true, true);
        addRandomRay.setOnClick(() -> {
            // если числа введены верно
            if (!cntField.hasValidIntValue()) {
                PanelLog.warning("кол-во лучей указано неверно");
            } else
                PanelRendering.task.addRandomRays(cntField.intValue());
        });
        buttons.add(addRandomTriangles);
        buttons.add(addRandomRay);

        // управление
        Button load = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 7, 3, 1, "Загрузить",
                true, true);
        load.setOnClick(() -> {
            PanelRendering.load();
            cancelTask();
        });
        buttons.add(load);

        Button save = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 7, 3, 1, "Сохранить",
                true, true);
        save.setOnClick(PanelRendering::save);
        buttons.add(save);

        Button clear = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 0, 8, 3, 1, "Очистить",
                true, true);
        clear.setOnClick(() -> PanelRendering.task.clear());
        buttons.add(clear);

        solve = new Button(
                window, false, backgroundColor, PANEL_PADDING,
                6, 9, 3, 8, 3, 1, "Решить",
                true, true);
        solve.setOnClick(() -> {
            if (!PanelRendering.task.isSolved()) {
                PanelRendering.task.solve();
                String s = "Задача решена\n";
                PanelInfo.show(s + "\n\nНажмите Esc, чтобы вернуться");
                PanelLog.success(s);
            } else {
                cancelTask();
            }
            window.requestFrame();
        });
        buttons.add(solve);

    }

    /**
     * Обработчик событий
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // вызываем обработчик предка
        super.accept(e);
        // событие движения мыши
        if (e instanceof EventMouseMove ee) {
            for (Input input : inputs)
                input.accept(ee);

            for (Button button : buttons) {
                if (lastWindowCS != null)
                    button.checkOver(lastWindowCS.getRelativePos(new Vector2i(ee)));
            }
            // событие нажатия мыши
        } else if (e instanceof EventMouseButton ee) {
            if (!lastInside)
                return;

            Vector2i relPos = lastWindowCS.getRelativePos(lastMove);

            // пробуем кликнуть по всем кнопкам
            for (Button button : buttons) {
                if (ee.isPressed())
                    button.click(relPos);
            }

            // перебираем поля ввода
            for (Input input : inputs) {
                // если клик внутри этого поля
                if (input.contains(relPos)) {
                    // переводим фокус на это поле ввода
                    input.setFocus();
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода текста
        } else if (e instanceof EventTextInput ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
            // обработчик ввода клавиш
        } else if (e instanceof EventKey ee) {
            for (Input input : inputs) {
                if (input.isFocused()) {
                    input.accept(ee);
                }
            }
            // перерисовываем окно
            window.requestFrame();
        }
    }

    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        task.paint(canvas, windowCS);
        // выводим кнопки
        for (Button button : buttons) {
            button.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Input input : inputs) {
            input.paint(canvas, windowCS);
        }
        // выводим поля ввода
        for (Label label : labels) {
            label.paint(canvas, windowCS);
        }
    }
    /**
     * Сброс решения задачи
     */
    private void cancelTask() {
        PanelRendering.task.cancel();
        // Задаём новый текст кнопке решения
        solve.text = "Решить";
    }
}
