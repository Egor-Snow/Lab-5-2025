import functions.*;
import functions.basic.*;

public class Main {

    private static FunctionPoint[] toPoints(TabulatedFunction f) {
        FunctionPoint[] pts = new FunctionPoint[f.getPointsCount()];
        for (int i = 0; i < pts.length; i++) {
            pts[i] = new FunctionPoint(f.getPointX(i), f.getPointY(i));
        }
        return pts;
    }

    private static void printFunction(String title, TabulatedFunction f) {
        System.out.println(title);
        System.out.println("Количество точек: " + f.getPointsCount());
        for (int i = 0; i < f.getPointsCount(); i++) {
            System.out.printf("  Точка %d: (%.6f; %.6f)%n", i, f.getPointX(i), f.getPointY(i));
        }
        System.out.println("Строковое представление: " + f);
        System.out.println("hashCode: " + f.hashCode());
        System.out.println();
    }

    public static void main(String[] args) {

        Function sin = new Sin();
        Function cos = new Cos();

        // ArrayTabulatedFunction
        TabulatedFunction arr1 = TabulatedFunctions.tabulate(sin, 0, Math.PI, 5);
        TabulatedFunction arr2 = TabulatedFunctions.tabulate(sin, 0, Math.PI, 5);
        TabulatedFunction arr3 = TabulatedFunctions.tabulate(sin, 0, Math.PI, 6);

        //  LinkedListTabulatedFunction
        TabulatedFunction list1 = new LinkedListTabulatedFunction(toPoints(arr1));
        TabulatedFunction list2 = new LinkedListTabulatedFunction(toPoints(arr1));
        TabulatedFunction list3 = new LinkedListTabulatedFunction(toPoints(TabulatedFunctions.tabulate(cos, 0, Math.PI, 5)));

        // FunctionPoint
        FunctionPoint p1 = new FunctionPoint(1.0, 2.0);
        FunctionPoint p2 = new FunctionPoint(1.0, 2.0);
        FunctionPoint p3 = new FunctionPoint(1.0, 2.0000001);

        // FunctionPoint: вывод + equals + hash
        System.out.println("=====================================================");
        System.out.println("      ТЕСТЫ КЛАССА FunctionPoint");
        System.out.println("=====================================================");

        System.out.println("p1 = " + p1 + ", hash = " + p1.hashCode());
        System.out.println("p2 = " + p2 + ", hash = " + p2.hashCode());
        System.out.println("p3 = " + p3 + ", hash = " + p3.hashCode());

        System.out.println("\nСравнение точек:");
        System.out.println("p1.equals(p2) → " + p1.equals(p2) + " (должно быть true)");
        System.out.println("p1.equals(p3) → " + p1.equals(p3) + " (должно быть false)");


        //  Табулированные функции: подробный вывод всех точек
        System.out.println("\n=====================================================");
        System.out.println("      ТЕСТЫ КЛАССОВ ТАБУЛИРОВАННЫХ ФУНКЦИЙ");
        System.out.println("=====================================================\n");

        printFunction("ARRAY arr1 (Sin, 5 точек):", arr1);
        printFunction("ARRAY arr2 (Sin, 5 точек — копия arr1):", arr2);
        printFunction("ARRAY arr3 (Sin, 6 точек — отличается от arr1):", arr3);

        printFunction("LIST  list1 (Sin, 5 точек):", list1);
        printFunction("LIST  list2 (Sin, 5 точек — копия list1):", list2);
        printFunction("LIST  list3 (Cos, 5 точек — отличается от list1):", list3);

        // equals() и hashCode() для табулированных функций
        System.out.println("\n=====================================================");
        System.out.println("      СРАВНЕНИЕ equals() И hashCode()");
        System.out.println("=====================================================\n");

        System.out.println("arr1.equals(arr2) → " + arr1.equals(arr2) + " (ожидается: true)");
        System.out.println("arr1.equals(arr3) → " + arr1.equals(arr3) + " (ожидается: false — разное число точек)");
        System.out.println("arr1.equals(list1) → " + arr1.equals(list1) + " (ожидается: true — одинаковые точки)");
        System.out.println("list1.equals(list3) → " + list1.equals(list3) + " (ожидается: false — другая функция cos)");

        // Проверка изменения hash
        TabulatedFunction arrMod = TabulatedFunctions.tabulate(sin, 0, Math.PI, 5);
        int hashBefore = arrMod.hashCode();
        arrMod.setPointY(1, 999);
        int hashAfter = arrMod.hashCode();

        System.out.println("\nПроверка изменения hashCode при изменении данных:");
        System.out.println("hash до изменения   = " + hashBefore);
        System.out.println("hash после изменения = " + hashAfter);

        // 4. clone()
        System.out.println("\n=====================================================");
        System.out.println("      ТЕСТЫ ГЛУБОКОГО КЛОНИРОВАНИЯ");
        System.out.println("=====================================================\n");

        System.out.println(" Клонирование ArrayTabulatedFunction:");
        try {
            TabulatedFunction arrClone = (TabulatedFunction) arr1.clone();

            System.out.println("До изменения:");
            System.out.println("  arr1[2] = " + arr1.getPointY(2));
            System.out.println("  clone[2] = " + arrClone.getPointY(2));

            arr1.setPointY(2, 500);

            System.out.println("После изменения arr1:");
            System.out.println("  arr1[2] = " + arr1.getPointY(2));
            System.out.println("  clone[2] = " + arrClone.getPointY(2) + "  (должно НЕ измениться)");

        } catch (Exception e) {
            System.out.println("Ошибка: клон функции создать нельзя!");
        }


        System.out.println("\n Клонирование LinkedListTabulatedFunction:");
        try {
            TabulatedFunction listClone = (TabulatedFunction) list1.clone();

            System.out.println("До изменения:");
            System.out.println("  list1[2] = " + list1.getPointY(2));
            System.out.println("  clone[2] = " + listClone.getPointY(2));

            list1.setPointY(2, -500);

            System.out.println("После изменения list1:");
            System.out.println("  list1[2] = " + list1.getPointY(2));
            System.out.println("  clone[2] = " + listClone.getPointY(2) + "  (должно НЕ измениться)");

        } catch (Exception e) {
            System.out.println("Ошибка: клон функции создать нельзя!");
        }
    }
}
