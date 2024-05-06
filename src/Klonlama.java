import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Klonlama {
	public static List<File> findJavaFiles(File directory) {
        List<File> javaDosyasi = new ArrayList<>(); // javaDosyası adında arraylist oluşturdum
        for (File dosya : directory.listFiles()) {	// dışardan alınan dosyayı foreach döngüsü ile dosya değişkenine atadım.
            if (dosya.isDirectory()) {
            	javaDosyasi.addAll(findJavaFiles(dosya));
            } else if (dosya.getName().endsWith(".java") && !InterfaceVarMi(dosya)) {  // interface değilse sınıfı java olarak kabul ediyoruz.
            	javaDosyasi.add(dosya);
            }
        }
        return javaDosyasi;
    }

    private static boolean InterfaceVarMi(File dosya) { //   Bu fonksiyonda alınan sınıf interface ise onu analiz bölümüne taşımamak istedik.
        try (Scanner fileScanner = new Scanner(dosya)) {
            while (fileScanner.hasNextLine()) {
                String ara = fileScanner.nextLine().trim();
                if (ara.contains("interface")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    
}
