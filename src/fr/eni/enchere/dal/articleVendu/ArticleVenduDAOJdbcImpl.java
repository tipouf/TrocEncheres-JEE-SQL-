package fr.eni.enchere.dal.articleVendu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.eni.enchere.BusinessException;
import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.dal.ConnectionProvider;
import fr.eni.enchere.dal.DAOFactory;

public class ArticleVenduDAOJdbcImpl implements ArticleVenduDAO {

    private static final String GET_ALL = "SELECT * FROM ARTICLES_VENDUS";

    private static final String GET_BY_ID = "SELECT * FROM ARTICLES_VENDUS WHERE no_article =?";

    private static final String DELETE = "DELETE FROM ARTICLES_VENDUS WHERE no_article = ?";

    private static final String INSERT = "INSERT INTO ARTICLES_VENDUS(nom_article,"
            + "description,"
            + "date_debut_encheres,"
            + "date_fin_encheres,"
            + "prix_initial,"
            + "prix_vente,"
            + "no_utilisateur,"
            + "no_categorie) VALUES(?,?,?,?,?,?,?,?)";

    private static final String UPDATE = "UPDATE ARTICLES_VENDUS SET " +
            "nom_article = ?," +
            "description = ?," +
            "date_debut_encheres = ?," +
            "date_fin_encheres = ?," +
            "prix_initial = ?," +
            "prix_vente = ?," +
            "no_utilisateur = ?," +
            "no_categorie = ?," +
            "WHERE no_article = ? ";

    @Override
    public List<ArticleVendu> getAll() throws BusinessException {
        List<ArticleVendu> listes = new ArrayList<>();

        try (Connection cnx = ConnectionProvider.getConnection()) {
            Statement stmt = cnx.createStatement();

            ResultSet rs = stmt.executeQuery(GET_ALL);

            while (rs.next()) {
                ArticleVendu nouvelArticle = new ArticleVendu(rs.getInt("noArticle"),
                        rs.getString("nomArticle"),
                        rs.getString("description"),
                        rs.getDate("date_debut_encheres"),
                        rs.getDate("date_debut_fin"),
                        rs.getInt("prix_initial"),
                        rs.getInt("prix_vente"),
                        DAOFactory.getUtilisateurDAO().getById(rs.getInt("no_utilisateur")),
                        DAOFactory.getCategorieDAO().getById(rs.getInt("no_categorie")));
                listes.add(nouvelArticle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listes;
    }

    @Override
    public ArticleVendu getById(int id) throws BusinessException {
        List<ArticleVendu> listes = new ArrayList<>();

        try (Connection cnx = ConnectionProvider.getConnection()) {
            PreparedStatement pStmt = cnx.prepareStatement(GET_BY_ID);
            pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();

            while (rs.next()) {
                ArticleVendu nouvelArticle = new ArticleVendu(rs.getInt("noArticle"),
                        rs.getString("nomArticle"),
                        rs.getString("description"),
                        rs.getDate("date_debut_encheres"),
                        rs.getDate("date_debut_fin"),
                        rs.getInt("nomArticle"),
                        rs.getInt("nomArticle"),
                        DAOFactory.getUtilisateurDAO().getById(rs.getInt("no_utilisateur")),
                        DAOFactory.getCategorieDAO().getById(rs.getInt("no_categorie")));
                listes.add(nouvelArticle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listes.get(0);
    }

    @Override
    public void insert(ArticleVendu articleVendu) throws BusinessException {
        try (Connection cnx = ConnectionProvider.getConnection()) {

            try {
                PreparedStatement pStmt = cnx.prepareStatement(INSERT);
                pStmt.setString(1, articleVendu.getNomArticle());
                pStmt.setString(2, articleVendu.getDescription());
                pStmt.setDate(3, articleVendu.getDateDebutEncheres());
                pStmt.setDate(4, articleVendu.getDateFinEncheres());
                pStmt.setInt(5, articleVendu.getPrixInitial());
                pStmt.setInt(6, articleVendu.getPrixVente());
                pStmt.setInt(7, articleVendu.getProprietaire().getNoUtilisateur());
                pStmt.setInt(8, articleVendu.getCategorie().getNoCategorie());

                pStmt.executeUpdate();

                cnx.commit();

            } catch (SQLException e) {
                e.printStackTrace();

                cnx.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ArticleVendu articleVendu) throws BusinessException {
        try (Connection cnx = ConnectionProvider.getConnection()) {

            PreparedStatement pStmt = cnx.prepareStatement(UPDATE);

            pStmt.setString(1, articleVendu.getNomArticle());
            pStmt.setString(2, articleVendu.getDescription());
            pStmt.setDate(3, articleVendu.getDateDebutEncheres());
            pStmt.setDate(4, articleVendu.getDateFinEncheres());
            pStmt.setInt(5, articleVendu.getPrixInitial());
            pStmt.setInt(6, articleVendu.getPrixVente());
            pStmt.setInt(7, articleVendu.getProprietaire().getNoUtilisateur());
            pStmt.setInt(8, articleVendu.getCategorie().getNoCategorie());
            pStmt.setInt(9, articleVendu.getNoArticle());

            int rs = pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ArticleVendu articleVendu) throws BusinessException {
        try (Connection cnx = ConnectionProvider.getConnection()) {

            PreparedStatement pStmt = cnx.prepareStatement(DELETE);
            pStmt.setInt(1, articleVendu.getNoArticle());

            pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}