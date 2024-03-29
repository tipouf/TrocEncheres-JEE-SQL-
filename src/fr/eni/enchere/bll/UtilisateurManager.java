package fr.eni.enchere.bll;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

import fr.eni.enchere.BusinessException;
import fr.eni.enchere.bo.Retrait;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.dal.DAOFactory;
import fr.eni.enchere.dal.util.UtilisateurDAO;

public class UtilisateurManager {

	private UtilisateurDAO utilisateurDAO;
	
	public UtilisateurManager() {
		utilisateurDAO = DAOFactory.getUtilisateurDAO();
	}

	public ArrayList<Utilisateur> getAll() { return utilisateurDAO.getAll(); }

	public void update(Utilisateur utilisateur) throws BusinessException {
		utilisateurDAO.update(utilisateur);
	}
	
	public Utilisateur ajouter(Utilisateur utilisateur) throws BusinessException {
		
		utilisateurDAO.insert(utilisateur);
		
		return utilisateur;
	}
	
	public Utilisateur getByEmailOrPseudo(String emailOrPseudo) {
		return utilisateurDAO.getByEmailOrPseudo(emailOrPseudo);
	}

	public Utilisateur getById(int userId) {
		return utilisateurDAO.getById(userId);
	}
	
	public boolean isPseudoAvailable(String pseudo) {
		return (utilisateurDAO.getByPseudo(pseudo) == null);
	}

	public boolean isEmailAvailable(String email) {
		return (utilisateurDAO.getByEmail(email) == null);
	}
	
	public void delete(int userId) throws BusinessException {
		utilisateurDAO.delete(userId);
	}

	/**
	 * Source : https://stackoverflow.com/a/33085670
	 *
	 * Crypte un mot de passe en SHA-512, en utilisant le sel pass� en param�tre
	 * Cryptage suffisant pour notre appli - Sinon utiliser de meilleurs cryptages plus long � casser
	 *
	 * Va g�n�rer un sel de hachage et l'ajouter � la fin de la cha�ne du mot de passe
	 */
	public String encryptPassword(String password, String salt){

		String encryptedPassword = null;

	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-256");
	        md.update(salt.getBytes(StandardCharsets.UTF_8));
	        byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
	        StringBuilder sb = new StringBuilder();
	        for(int i=0; i< bytes.length ;i++){
	            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        encryptedPassword = sb.toString();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }

		encryptedPassword = encryptedPassword.concat(salt);

	    return encryptedPassword;
	}

	/**
	 * Crypte un mot de passe en utilisant SHA-512
	 * G�n�re le sel puis appelle encryptPassword(String, String)
	 */
	public String encryptPassword(String password) {

		Random random = new Random();

		String salt = random.ints(97, 123)
				.limit(16)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();

		return encryptPassword(password, salt);
	}
	/**
	 * Compare un mot de passe avec le mot de passe de l'utilisateur
	 * R�utilise le sel de hachage pr�sent � la fin du mot de passe [user.getMotDePasse()]
	 */
	public boolean passwordMatch(String testPassword, Utilisateur user) {

		String salt = user.getMotDePasse().substring(user.getMotDePasse().length() - 16);
		String userPassword = user.getMotDePasse().replace(salt, "");

		String encryptedTestPassword = encryptPassword(testPassword, salt).replace(salt, "");

		if (encryptedTestPassword.equals(userPassword)) {
			return true;
		}
		return false;
	}
}
