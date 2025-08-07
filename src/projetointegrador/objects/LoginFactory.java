package projetointegrador.objects;

import java.util.ArrayList;
import java.util.List;

public class LoginFactory {

    public List<String> getTiposLogin() {
        List<String> lista = new ArrayList<>();
        lista.add("Gerente");
        lista.add("Administrador");
        return lista;
    }

    public String passarParaBancoDeDados(String acesso) {
        if (acesso == null) {
            return null;
        }
        switch (acesso) {
            case "Administrador" -> {
                return "a";
            }
            case "Gerente" -> {
                return "g";
            }
            default -> {
                return null;
            }
        }
    }

    public Logins getLogin(char acesso) {
        switch (acesso) {
            case 'a' -> {
                return new LoginsAdministrador();
            }
            case 'g' -> {
                return new LoginsGerente();
            }
            default -> {
                return new Logins();
            }
        }
    }
}
