package fr.eni.enchere.dal.util;

import java.util.ArrayList;

import fr.eni.enchere.BusinessException;
import fr.eni.enchere.bo.Utilisateur;

public interface UtilisateurDAO {

	public void insert(Utilisateur utilisateur) throws BusinessException;

	public ArrayList<Utilisateur> getAll();

	public void update(Utilisateur utilisateur);

	public void delete(Utilisateur utilisateur);

	public void delete(int noUtilisateur);


	public Utilisateur getByEmail(String email);

	public Utilisateur getById(int id);

	public Utilisateur getByEmailOrPseudo(String emailOrPseudo);

	public Utilisateur getByPseudo(String email);

}
