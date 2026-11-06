import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


//int readCanvases (InputStream inputStream) - метод којшто од влезен поток на податоци ќе прочита информации за повеќе прозорци на
// кои се исцртуваат квадрати. Во секој ред од потокот е дадена информација за еден прозорец во формат: canvas_id size_1 size_2 size_3 ….
// size_n, каде што canvas_id е ИД-то на прозорецот, а после него следуваат големините на страните на квадратите што се исцртуваат
// во прозорецот. Методот треба да врати цел број што означува колку квадрати за сите прозорци се успешно прочитани.

//void printLargestCanvasTo (OutputStream outputStream) - метод којшто на излезен поток ќе го испечати прозорецот чии квадрати имаат
// најголем периметар. Печатењето да се изврши во форматот canvas_id squares_count total_squares_perimeter.

class Shapes {
    private static List<String> sizes;

    public Shapes() {
        sizes = new ArrayList<>();
    }

    public static Shapes createShapes(String line) {
        String[] parts = line.split("\\+s");
        String ID = parts[0];
        for (int i = 0; i < line.length(); i++) {
            sizes.add(parts[i]);
        }

        return new Shapes(ID, sizes);
    }

}


class ShapesApplication {
    private List<Shapes> shapes;
    public ShapesApplication() {
        shapes = new ArrayList<>();
    }

    public int readCanvases (InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        shapes = reader.lines().
                filter(Objects::nonNull)
                .map(Shapes::createShapes)
                .collect(Collectors.toList());


    }

    void printLargestCanvasTo (OutputStream outputStream) {

    }
}

public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}