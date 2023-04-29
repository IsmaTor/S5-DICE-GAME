package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.dto.AdminDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IAdminServicesDAO {

    public void add(AdminDTO adminDTO); //POST /admins: create a admin.

    public AdminDTO update(int id, AdminDTO adminDTO); //PUT /admins: change admins names.

    public boolean validateAdminAccess(int id, String token, HttpServletRequest request); //Validate admin's by comparing the token role & id with admin role & id.

    public void deletePlayers(int id); //DEL /admins: remove players in the game.

    public void deleteAdmins(int id); //DEL /admins: remove admins in the game.

    public boolean playerExists(Integer id); //Check if the player exists in the game.

    public boolean adminExists(Integer id); //Check if the admin exists in the game.

    public boolean validateAdminAccessMaster(int id, String token, HttpServletRequest request); //Validate admin's by comparing the token role with admin role.

}
