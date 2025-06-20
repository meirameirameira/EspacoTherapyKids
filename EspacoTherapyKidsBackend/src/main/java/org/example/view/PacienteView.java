package org.example.view;

import org.example.factory.ConnectionFactory;

import java.sql.Connection;
import java.util.Scanner;
import java.util.concurrent.*;

public class PacienteView {
    private static final int TENTATIVAS = 3;
    private static final long MENU_TIMEOUT = 60;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        boolean connected = false;
        int attempts = 0;

        while (!connected && attempts < TENTATIVAS) {
            System.out.print("Digite seu Usuário: ");
            String user = sc.nextLine().trim();
            System.out.print("Digite sua Senha: ");
            String senha = sc.nextLine().trim();
            System.out.print("Aguarde...\n");

            ConnectionFactory.setCredentials(user, senha);
            attempts++;
            try (Connection conexao = ConnectionFactory.getConnection()) {
                connected = true;
                System.out.println("\nConectado ao banco com sucesso!");
            } catch (Exception e) {
                System.out.println("\nFalha ao conectar com credenciais fornecidas: " + e.getMessage());
                if (attempts < TENTATIVAS) {
                    System.out.println("Tente novamente. Restam " + (TENTATIVAS - attempts) + " tentativas.\n");
                } else {
                    System.out.println("Número máximo de tentativas excedido. Encerrando.");
                    exec.shutdownNow();
                    System.exit(1);
                }
            }
        }

        int opc = -1;
        do {
            System.out.println("\nEspaço Therapy Kids");
            System.out.println("\n1 - Cadastrar");
            System.out.println("2 - Listar Todos Pacientes");
            System.out.println("3 - Pesquisar Paciente por ID");
            System.out.println("4 - Atualizar Dados de Paciente");
            System.out.println("5 - Remover Paciente");
            System.out.println("6 - Atualizar Arquivo de Lista");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha uma Opção: ");
            try {
                Future<String> future = exec.submit(sc::nextLine);
                String line = future.get(MENU_TIMEOUT, TimeUnit.SECONDS);
                opc = Integer.parseInt(line.trim());
            } catch (TimeoutException te) {
                System.out.println("Inatividade detectada. Encerrando.");
                exec.shutdownNow();
                System.exit(1);
            } catch (NumberFormatException nfe) {
                System.out.println("Opção inválida: informe um número.");
                continue;
            }

            switch (opc) {
                case 1:
                    CadastroPacienteView.main(null);
                    break;
                case 2:
                    ListagemPacienteView.main(null);
                    break;
                case 3:
                    PesquisarPacientePorIdView.main(null);
                    break;
                case 4:
                    AtualizacaoPacienteView.main(null);
                    break;
                case 5:
                    RemoverPacienteView.main(null);
                    break;
                case 6:
                    GerenciarArquivoView.iniciar();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opc != 0);

        exec.shutdownNow();
        sc.close();
    }
}
