import java.sql.*;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) throws SQLException {
    Scanner input = new Scanner(System.in);
    Connection connection = DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/videogamestore_db", "Amministratore", "Segretissimo");
    while (true) {
      System.out.println("Lista operazioni disponibili:");
      System.out.println("1. Mostrare su schermo l'insieme dei videogiochi posseduti da un certo videogiocatore (libreria):");
      System.out.println("   Digitare 'Stampa libreria'");
      System.out.println("2. Mostrare su schermo le informazioni su tutti i videogiochi del catalogo:");
      System.out.println("   Digitare 'Stampa catalogo'");
      System.out.println("3. Registrare all'interno del database una nuova transazione:");
      System.out.println("   Digitare 'Inserisci transazione'");
      System.out.println("4. Mostrare su schermo le informazioni relative ad un certo premio:");
      System.out.println("   Digitare 'Dettagli premio'");
      System.out.println("5. Mostrare su schermo le informazioni delle aziende che trattano di videogiochi" +
              " con almeno un certo numero di dipendenti:");
      System.out.println("   Digitare 'Mostra migliori aziende'");
      System.out.println("6. Disconnettersi dal database e fermare l'esecuzione:");
      System.out.println("   Digitare 'Disconnettiti'");

      String comando = input.nextLine();
      switch (comando) {
        case "Stampa libreria" -> {
          System.out.println("Inserire username del videogiocatore: ");
          comando = input.nextLine();
          GameStoreQueries.stampaLibreriaUtente(connection, comando);
          input.nextLine();
        }
        case "Stampa catalogo" -> {
          GameStoreQueries.ottieniInfoVideogiochi(connection);
          input.nextLine();
        }
        case "Inserisci transazione" -> {
          System.out.println("Inserire l'username del videogiocatore che riceverà il videogioco: ");
          String username = input.nextLine();
          System.out.println("Inserire il nome del videogioco da acquistare: ");
          String videogioco = input.nextLine();
          System.out.println("Inserire l'ID del metodo di pagamento da utilizzare: ");
          int id = input.nextInt();
          input.nextLine();
          System.out.println("Inserire la data della transazione: ");
          String data = input.nextLine();
          GameStoreQueries.inserisciTransazione(connection, username, videogioco, id, data);
          input.nextLine();
        }
        case "Dettagli premio" -> {
          System.out.println("Inserire il codice del premio di cui si vogliono i dettagli: ");
          int codicePremio = input.nextInt();
          input.nextLine();
          GameStoreQueries.mostraInfoPremio(connection, codicePremio);
          input.nextLine();
        }
        case "Mostra migliori aziende" -> {
          System.out.println("Inserire il numero minimo di dipendenti delle aziende da mostrare: ");
          int minNumeroDipendenti = input.nextInt();
          input.nextLine();
          GameStoreQueries.mostraTopAziende(connection, minNumeroDipendenti);
          input.nextLine();
        }
        case "Disconnettiti" -> {
          return;
        }
        default -> {
          System.out.println("Comando non valido, riprovare");
          System.out.println();
          input.nextLine();
        }
      }
    }
  }
}
