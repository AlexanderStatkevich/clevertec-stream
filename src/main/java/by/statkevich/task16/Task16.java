package by.statkevich.task16;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task16 {
    public static void main(String[] args) {
        List<Headphone> headphones = List.of(
                new Headphone("Sony", true, "White", true),
                new Headphone("Huawei", false, "Red", false),
                new Headphone("JBL", true, "Blue", true),
                new Headphone("Audio-Technica", true, "Green", false),
                new Headphone("Apple", true, "Purple", false),
                new Headphone("Xiaomi", false, "Grey", false),
                new Headphone("Technics", true, "Black", true),
                new Headphone("Beats", true, "Orange", false),
                new Headphone("Sony", true, "White", true),
                new Headphone("Sennheiser", false, "White", false),
                new Headphone("Sony", true, "White", true));

        Map<Boolean, Long> headphoneMap = headphones.stream()
                .distinct()
                .sorted(Comparator.comparing(Headphone::name).thenComparing(Headphone::color))
                .filter(Headphone::isDomestic)
                .collect(Collectors.partitioningBy(Headphone::isWireless, Collectors.counting()));

        for (Map.Entry<Boolean, Long> entry : headphoneMap.entrySet()) {
            Boolean isWireless = entry.getKey();
            Long quantity = entry.getValue();
            System.out.println(isWireless + " " + quantity);
        }
    }

    public record Headphone(String name, boolean isWireless, String color, boolean isDomestic) {
    }
}

