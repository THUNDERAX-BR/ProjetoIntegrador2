package projetointegrador.objects;

public class LoginsGerente extends Logins {

    public LoginsGerente() {
    }

    @Override
    public String getAcesso() {
        return "Gerente";
    }

    @Override
    public boolean permissaoGerenciar() {
        return true;
    }
}
