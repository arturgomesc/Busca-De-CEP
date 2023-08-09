import Dados.GeradorDeArquivo;
import Dados.Informacoes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner= new Scanner(System.in);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String busca = "";

        while (!busca.equalsIgnoreCase("sair")){
            System.out.println("Digite o seu CEP: ");
            busca = scanner.nextLine();

            if (busca.equalsIgnoreCase("sair")) {
                break;
            }

            String endereco = "https://viacep.com.br/ws/" + busca.replace("-", "") + "/json/";

            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endereco))
                        .build();
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                System.out.println(json);

                Informacoes informacoes = gson.fromJson(json, Informacoes.class);
                System.out.println(informacoes);
                GeradorDeArquivo gerador = new GeradorDeArquivo();
                gerador.salvaJson(informacoes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }catch (NullPointerException e) {
                System.out.println("Não foi possível encontrar o CEP informado. Por favor, digite um válido.");
            } catch (JsonSyntaxException e) {
                System.out.println("O CEP redigido está incorreto. Por favor, adicione apenas números e exatamente 8 dígitos.");
            }
        }
    }
}
