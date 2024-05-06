import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String depoUrl = "";

        // Kullanıcı doğru bir git adresi girene kadar döngüyü tekrarlamasını istedim.
        while (true) {
            System.out.println("Lütfen GitHub deposunun URL'sini girin:");
            depoUrl = scanner.nextLine();

            // Git adresi doğruysa döngüyü sonlandır ve devam et.
            if (depoUrl.startsWith("https://github.com/")) {
                break;
            } else {
                System.out.println("Geçersiz git adresi. Lütfen doğru bir git adresi girin.");
            }
        }

        scanner.close();

        try {
        	//Burda rastgele klasör ismi oluşturup projeleri o oluşturduğum klasörlere atadım.
        	UUID id = UUID.randomUUID();
            Git.cloneRepository()	// klonlama işlemlerini yaptım burada.
                .setURI(depoUrl)
                .setDirectory(new File("alınanProje_"+id))
                .call();

            List<File> javaDosya = Klonlama.findJavaFiles(new File("alınanProje_"+id));			// java dosyalarını Klonlama sınıfına giderek javadosya listesine atadım.

            for (File dosya : javaDosya) { 			
                Analiz.SınıfAnalizi(dosya);		//foreach döngüsü ile java dosyalarını Analiz sınıfının Sınıf analizi fonksiyonuna gönderdim.
            }

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    
}
