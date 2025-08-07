package projetointegrador.objects;

public class LoginsAdministrador extends Logins {

    public LoginsAdministrador() {
    }

    @Override
    public String getAcesso() {
        return "Administrador";
    }

    @Override
    public boolean permissaoGerenciar() {
        return true;
    }

    @Override
    public boolean permissaoGerenciarLogins() {
        return true;
    }
}
