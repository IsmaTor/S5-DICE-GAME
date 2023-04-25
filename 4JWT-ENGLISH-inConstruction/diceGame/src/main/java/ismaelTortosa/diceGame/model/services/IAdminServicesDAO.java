package ismaelTortosa.diceGame.model.services;

import ismaelTortosa.diceGame.model.dto.AdminDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface IAdminServicesDAO {

    public void add(AdminDTO adminDTO); //POST /admins: create a admin.

    public AdminDTO update(int id, AdminDTO adminDTO); //PUT /admins: change admins names.

    public boolean validateAdminAccess(int id, String token, HttpServletRequest request); //Validate admin's by comparing the token rol with admin rol.

    public boolean validationTokenId(int id, HttpServletRequest request); //Validate admin's by comparing the token id with admin id.
}
