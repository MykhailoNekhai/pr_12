package ua.uni.first;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PaymentLoader {
    public static List<Payment> load(Path csv) throws IOException {
        return loadWithStats(csv).payments();
    }

    public static LoadResult loadWithStats(Path csv) throws IOException {
        List<Payment> payments = new ArrayList<>();
        int invalidLines = 0;

        try (BufferedReader bufferedReader = Files.newBufferedReader(csv)) {
            bufferedReader.readLine();
            String line = bufferedReader.readLine();
            while(line != null){
                if (line.trim().isEmpty()) {
                    invalidLines++;
                    line = bufferedReader.readLine();
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length != 4) {
                    invalidLines++;
                    line = bufferedReader.readLine();
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String email = parts[1].trim();
                    PaymentStatus status = PaymentStatus.valueOf(parts[2].trim());
                    double amountCents = Double.parseDouble(parts[3].trim());

                    if (email.isEmpty()) {
                        invalidLines++;
                    } else {
                        payments.add(new Payment(id, email, status, amountCents));
                    }
                } catch (RuntimeException e) {
                    invalidLines++;
                }

                line = bufferedReader.readLine();
            }
        }
        return new LoadResult(payments, invalidLines);
    }

    public record LoadResult(List<Payment> payments, int invalidLines) {}
}
