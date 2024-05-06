import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analiz {
	private static boolean FonksiyonBul(String line) {

	    Pattern methodDeseni = Pattern.compile("\\b(public|private|protected)\\s+[a-zA-Z0-9<>]+\\s+[a-zA-Z0-9]+\\s*\\(.*\\)\\s*\\{?"); // Burda fonksiyon olma koşullarına bakım ona göre inceliyor
	    Matcher methodEslesici = methodDeseni.matcher(line);
	    boolean metotMu = methodEslesici.matches() && !line.contains("class") && !line.contains("interface");
	    
	    if (!metotMu) {  	
	        String constructorDeseni = "\\b(public|private|protected)\\s+[a-zA-Z0-9<>]+\\s+[a-zA-Z0-9]+\\s*\\(.*\\)\\s*\\{?";   //Burda construcor kontrolü yaptım ve constructor leri de fonksiyon sayısına kattım.
	        Pattern constructorDesen = Pattern.compile(constructorDeseni);
	        Matcher constructorEslesici = constructorDesen.matcher(line);
	        metotMu = constructorEslesici.find();
	    }
	    
	    if (!metotMu) {
	    	metotMu = (line.contains("public") || line.contains("private") || line.contains("protected")) && line.contains("(") && line.contains(") {");
	    }
	    																													// burda yeniden bir kontrol yaptım override kullanan fonksiyonları da sayması için
	    if (!metotMu) {
	    	metotMu = line.contains("@Override") && line.contains("(") && line.contains(")");
	    }


	    if (!metotMu) {
	    	metotMu = line.contains("public") && line.contains("(") && line.contains(")") && !line.contains("void");    	    // Parametre almayan constructor kontrolü yaptım
	    }
	    
	    return metotMu;
	}





    public static void SınıfAnalizi(File javaFile) throws IOException {
    	int toplamSatir = 0;
        int kodSatir = 0;
        int javadoc = 0;
        int yorumSatiri = 0;
        boolean javadocIcerisinde = false;
        boolean yorumSatiriMi = false;
        int fonksiyonSayi = 0;

        try (Scanner fileScanner = new Scanner(javaFile)) {
            while (fileScanner.hasNextLine()) {
                String satir = fileScanner.nextLine().trim();
                
                if (satir.startsWith("/**")) {
   
                    javadocIcerisinde = true;					// javadoc buluyoruz burada
                } else if (satir.startsWith("*/")) {			

                    javadocIcerisinde = false;
                } else if (javadocIcerisinde && satir.startsWith("*")) {

                    javadoc++;
                } else if (satir.startsWith("/*")) {		// yorum satırı buluyoruz burada
                	
                    yorumSatiriMi = true;
                } else if (satir.startsWith("*/")) {

                    yorumSatiriMi = false;
                } else if (yorumSatiriMi && satir.startsWith("*")) {		// yorum satırı mı ve * ile başlıyorsa yorum satırı olarak sayıyoruz

                    yorumSatiri++;
                } else if (satir.contains("//")) {		// yorum satırı buluyoruz burada

                    yorumSatiri++;
                } else if (!satir.isEmpty()) {		// boş olmayan yerleri kod satırı olarak sayıyoruz

                    kodSatir++;
                }
                
                if (FonksiyonBul(satir)) {			// fonksiyon bul fonksiyonuna gidip true değeri alırsak fonksiyon sayısını artırıyor
                    fonksiyonSayi++;
                }
                if (satir.contains("public static void main")) {  // bunu eklemesem public static void main i fonksiyon olarak saymıyordu.
                    fonksiyonSayi++;
                }
                toplamSatir++;
            }
        }

        
        double YG = ((javadoc + yorumSatiri) * 0.8) / fonksiyonSayi;
        double YH = (kodSatir / (double) fonksiyonSayi) * 0.3;  // Yorum sapma yüzdesi hesabı
        double yorumSapmaYuzdesi = ((100 * YG) / YH) - 100;
        
        
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');	
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);										// Burda yorum sapma yüzdesini virgünden sonraki 2 basamağı yazdırmak için kullandım.
        String formatliYorumSapmaYuzdesi;
        if (yorumSapmaYuzdesi < 0) {
            formatliYorumSapmaYuzdesi = "-" + decimalFormat.format(Math.abs(yorumSapmaYuzdesi));
        } else {
            formatliYorumSapmaYuzdesi = decimalFormat.format(Math.abs(yorumSapmaYuzdesi));
        }
        
        
        System.out.println("Sınıf: " + javaFile.getName());
        System.out.println("Javadoc Satır Sayısı: " + javadoc);
        System.out.println("Yorum Satır Sayısı: " + yorumSatiri);
        System.out.println("Kod Satır Sayısı: " + kodSatir);
        System.out.println("LOC: " + toplamSatir);
        System.out.println("Fonksiyon Sayısı: " + fonksiyonSayi);
        System.out.println("Yorum Sapma Yüzdesi: % " + formatliYorumSapmaYuzdesi);
        System.out.println("-------------------------------------------");
    }
}
