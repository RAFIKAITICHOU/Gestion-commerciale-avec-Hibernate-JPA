package org.example.service;

import org.example.dao.CommandeDAO;
import org.example.entities.Commande;

public class CommandeService extends CrudService<Commande, Long> {

    private final CommandeDAO commandeDAO;

    public CommandeService() {
        this(new CommandeDAO());
    }

    private CommandeService(CommandeDAO dao) {
        super(dao);
        this.commandeDAO = dao;
    }

    public Commande getByIdWithLignes(Long id) { return commandeDAO.findByIdWithLignes(id); }
}
