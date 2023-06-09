import java.sql.*;

public class GameStoreQueries {
  public static void stampaLibreriaUtente(Connection connection, String utente) throws SQLException {
    CallableStatement callableStatement = null;
    ResultSet resultSet = null;
    try {
      System.out.println("Libreria di " + utente + ":");
      callableStatement = connection.prepareCall("{call mostraLibreriaUtente(?, ?)}");
      callableStatement.setString(1, utente);
      callableStatement.registerOutParameter(2, Types.INTEGER);
      boolean hadResults = callableStatement.execute();
      while (hadResults) {
        resultSet = callableStatement.getResultSet();
        while (resultSet.next()) {
          String videogioco = resultSet.getString("Videogioco");
          String tipoMetodoDiPagamento = resultSet.getString("Tipo");
          String dataDiRilascio = resultSet.getString("Data");
          System.out.printf("%-25s%s", videogioco, String.format
                  (" Acquistato giorno %s utilizzando come metodo di pagamento: %s%n", dataDiRilascio, tipoMetodoDiPagamento));
        }
        hadResults = callableStatement.getMoreResults();
      }
      int numeroDiVideogiochi = callableStatement.getInt(2);
      System.out.println("Ci sono " + numeroDiVideogiochi + " videogiochi in totale.");
    } catch (SQLException exception) {
      System.err.println("Username non valido o errore di connessione, riprovare");
    } finally {
      if (callableStatement != null) {
        callableStatement.close();
      }
      if (resultSet != null) {
        resultSet.close();
      }
    }
  }

  public static void ottieniInfoVideogiochi(Connection connection) {
    System.out.printf("%-30s %-20s %-20s %-20s %-20s %-20s %-20s %n",
            "Nome", "Prezzo", "Data di rilascio", "Numero di vendite", "Sviluppatore", "Editore", "Numero di premi ottenuti");
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery("select * from informazionivideogiochi")) {
      while (resultSet.next()) {
        for (int forIndex = 1; forIndex <= 7; forIndex++) {
          if (forIndex == 1) {
            System.out.printf("%-30s ", resultSet.getString(forIndex));
          } else {
            System.out.printf("%-20s ", resultSet.getString(forIndex));
          }
        }
        System.out.println();
      }
    } catch (SQLException exception) {
      System.err.println("Errore, riprovare");
    }
  }

  public static void inserisciTransazione
          (Connection connection, String utente, String nomeVideogioco, int metodoDiPagamento, String dataTransazione) {
    try (CallableStatement callableStatement = connection.prepareCall("{call inserisciTransazione(?, ?, ?, ?)}")) {
      callableStatement.setString(1, utente);
      callableStatement.setString(2, nomeVideogioco);
      callableStatement.setString(3, dataTransazione);
      callableStatement.setInt(4, metodoDiPagamento);
      callableStatement.execute();
      System.out.println("Transazione registrata con successo");
    } catch (SQLException exception) {
      System.err.println("Informazioni non valide o errore di connessione, riprovare");
    }
  }

  public static void mostraInfoPremio(Connection connection, int codicePremio) throws SQLException {
    CallableStatement callableStatement = null;
    ResultSet resultSet = null;
    try {
      callableStatement = connection.prepareCall("{call infoPremio(?)}");
      callableStatement.setInt(1, codicePremio);
      boolean hadResults = callableStatement.execute();
      while (hadResults) {
        resultSet = callableStatement.getResultSet();
        while (resultSet.next()) {
          for (int forIndex = 1; forIndex <= 8; forIndex++) {
            switch (forIndex) {
              case 1 -> System.out.printf("%-15s", "Nome: ");
              case 2 -> System.out.printf("%-15s", "Categoria: ");
              case 3 -> System.out.printf("%-15s", "Anno: ");
              case 4 -> System.out.printf("%-15s", "Ente: ");
              case 5 -> System.out.printf("%-15s", "Tipo: ");
              case 6 -> System.out.printf("%-15s", "Videogioco: ");
              case 7 -> System.out.printf("%-15s", "Sviluppatore: ");
              case 8 -> System.out.printf("%-15s", "Editore: ");
            }
            System.out.println(resultSet.getString(forIndex));
          }
        }
        hadResults = callableStatement.getMoreResults();
      }
    } catch (SQLException exception) {
      System.err.println("Codice premio non valido o errore di connessione, riprovare");
    } finally {
      if (callableStatement != null) {
        callableStatement.close();
      }
      if (resultSet != null) {
        resultSet.close();
      }
    }
  }

  public static void mostraTopAziende(Connection connection, int minimoDipendenti) throws SQLException {
    CallableStatement callableStatement = null;
    ResultSet resultSet = null;
    System.out.printf("%-20s %-20s %-20s %-20s %-20s %n",
            "Nome", "Tipo", "Totale dipendenti", "Sede", "Prezzo videogioco più caro");
    try {
      callableStatement = connection.prepareCall("{call mostraAziende(?)}");
      callableStatement.setInt(1, minimoDipendenti);
      boolean hadResults = callableStatement.execute();
      while (hadResults) {
        resultSet = callableStatement.getResultSet();
        while (resultSet.next()) {
          for (int forIndex = 1; forIndex < 6; forIndex++) {
            System.out.printf("%-20s ", resultSet.getString(forIndex));
          }
          System.out.println();
        }
        hadResults = callableStatement.getMoreResults();
      }
    } catch (SQLException exception) {
      System.err.println("Input non valido o errore, riprovare");
    } finally {
      if (callableStatement != null) {
        callableStatement.close();
      }
      if (resultSet != null) {
        resultSet.close();
      }
    }
  }
}
