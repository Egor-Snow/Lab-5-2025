package functions;
import java.io.*;

//Класс, содержащий вспомогательные статические методы для работы с табулированными функциями

public final class TabulatedFunctions {

    private static final double EPS = 1e-9;

    //Приватный конструктор запрещает создание объектов этого класса
    private TabulatedFunctions() {

    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {

        //Проверка на корректность входных параметров

        if (leftX >= rightX - EPS) {
            throw new IllegalArgumentException("Left border must be less than right border");
        }

        //Выбрасывает исключение, если счетчик точек меньше двух
        if (pointsCount < 2) {
            throw new IllegalArgumentException("At least two points required");
        }

        //Проверка границ табулирования относительно области определения функции

        double domainLeft = function.getLeftDomainBorder();
        double domainRight = function.getRightDomainBorder();

        //leftX должен быть >= domainLeft
        if (leftX < domainLeft && Math.abs(leftX - domainLeft) > EPS) {
            throw new IllegalArgumentException("The left tabulation boundary ( " + leftX + ") extends beyond the function's domain ( " + domainLeft + ").");
        }

        //rightX должен быть <= domainRight (с учетом EPS)
        if (rightX > domainRight && Math.abs(rightX - domainRight) > EPS) {
            throw new IllegalArgumentException("The right tabulation boundary (' + rightX + ') extends beyond the function's domain (' + domainRight + ').");
        }

        //Табулирование

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);

        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;

            // Если мы достигли последней точки, принудительно устанавливаем x = rightX, чтобы избежать накопления погрешности
            if (i == pointsCount - 1) {
                x = rightX;
            }

            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }

        return new ArrayTabulatedFunction(points);
    }

    // Выводит табулированную функцию в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream outputStream) throws IOException {

        // DataOutputStream позволяет удобно записывать примитивные типы данных (int, double)
        DataOutputStream out = new DataOutputStream(outputStream);

        // Записываем количество точек
        out.writeInt(function.getPointsCount());

        // Записываем пары (x, y)
        for (int i = 0; i < function.getPointsCount(); i++) {
            out.writeDouble(function.getPointX(i));
            out.writeDouble(function.getPointY(i));
        }
    }

    // Считывает табулированную функцию из байтового потока, создает и настраивает её объект, и возвращает его из метода
    public static TabulatedFunction inputTabulatedFunction(InputStream inputStream) throws IOException {

        // DataInputStream позволяет считывать примитивные типы данных
        DataInputStream in = new DataInputStream(inputStream);

        // Считываем количество точек
        int pointsCount = in.readInt();

        // Создаем массив для точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        // Считываем пары (x, y)
        for (int i = 0; i < pointsCount; i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        // Возвращаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }


    // Записывает табулированную функцию в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer writer) {

        // PrintWriter позволяет удобно записывать текст с форматированием
        PrintWriter out = new PrintWriter(writer);

        // Записываем количество точек
        out.println(function.getPointsCount());

        // Записываем пары (x, y)
        for (int i = 0; i < function.getPointsCount(); i++) {
            // Записываем X и Y через пробел.
            out.println(function.getPointX(i) + " " + function.getPointY(i));
        }
        out.flush(); // Сброс буфера для PrintWriter
    }

    // Считывает табулированную функцию из символьного потока, создает и настраивает её объект, возвращает его из метода
    public static TabulatedFunction readTabulatedFunction(Reader reader) throws IOException {
        // StreamTokenizer - класс для парсинга числовых данных из потока
        StreamTokenizer tokenizer = new StreamTokenizer(reader);
        tokenizer.parseNumbers(); // Настраиваем токенизатор на чтение чисел

        // Считываем количество точек, первый токен должен быть количеством точек
        tokenizer.nextToken();
        int pointsCount = (int) tokenizer.nval;

        // Массив для точек
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        //  Считываем пары (x, y)
        for (int i = 0; i < pointsCount; i++) {

            // Считываем X
            tokenizer.nextToken();
            double x = tokenizer.nval;

            // Считываем Y
            tokenizer.nextToken();
            double y = tokenizer.nval;

            points[i] = new FunctionPoint(x, y);
        }

        // Возвращаем табулированную функцию
        return new ArrayTabulatedFunction(points);
    }

    //Сериализует (сохраняет) табулированную функцию в байтовый поток
    public static void serialize(TabulatedFunction function, OutputStream outputStream) throws IOException {
        // ObjectOutputStream записывает объект в бинарном виде
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(function);
        }
    }

    //Десериализует (восстанавливает) табулированную функцию из байтового потока

    public static TabulatedFunction deserialize(InputStream inputStream) throws IOException, ClassNotFoundException {
        // ObjectInputStream считывает байты и восстанавливает объект
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            // Читаем объект и приводим его к типу TabulatedFunction
            return (TabulatedFunction) in.readObject();
        }
    }
}