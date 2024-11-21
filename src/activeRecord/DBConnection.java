        package activeRecord;

        import java.sql.Connection;
        import java.sql.DriverManager;
        import java.sql.SQLException;
        import java.sql.Statement;
        import java.util.Properties;

        public class DBConnection {

            // attribut de connection
            private String userName = "root";
            String password = "root";
            String serverName = "localhost";
            String portNumber = "8889";

            // la connection
            static private Connection conn;
            // lien vers la base � la cr�ation de la connection
            // permet de g�rer plusieurs connections si besoin (test versus application)
            static private String dbName;

            /**
             * constructeur prive de connection
             *
             * @throws SQLException exception � la creation de la connection
             */
            private DBConnection() {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Properties connectionProps = new Properties();
                    connectionProps.put("user", this.userName);
                    connectionProps.put("password", this.password);
                    String urlDB = "jdbc:mysql://" + this.serverName + ":";
                    urlDB += this.portNumber + "/" + DBConnection.dbName;
                    conn = DriverManager.getConnection(urlDB, connectionProps);
                } catch (SQLException e) {
                    System.out.println("*** ERREUR SQL ***");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("*** ERREUR lors du chargement du driver ***");
                    e.printStackTrace();
                } catch (Exception e) {
                    System.out.println("*** ERREUR inconnue... ***");
                    e.printStackTrace();
                }
            }

            /**
             * permet de modifier le nom de la base on peut ainsi differencier les bases en
             * fonction de l'application ou des classes de test
             *
             * @param DB nom de la base de donnees
             */
            public static void setNomDb(String DB) {
                dbName = DB;
                // reinitialisation de la connection a null
                conn = null;
            }

            /**
             * permet de creer ou recuperer une connection (pattern singleton)
             *
             * @return la connection nouvellement cr��e
             * @throws SQLException probleme lors de la creation de la connection.
             */
            public synchronized static Connection getConnection() throws SQLException {
                if (conn == null) {
                    new DBConnection();
                }
                return (conn);
            }

            /**
             * permet d'executer une commande SQL
             *
             * @param command la commande a executer
             * @throws SQLException en cas de probleme de connection
             */
            public static void executeUpdate(String command) throws SQLException {
                Statement stmt = null;
                Connection connect = getConnection();
                try {
                    stmt = connect.createStatement();
                    stmt.executeUpdate(command);
                } finally {
                    if (stmt != null) {
                        stmt.close();
                    }
                }

            }
        }
