package org.example.view;

import java.util.Scanner;

public class PacienteView {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int opc;
        do {
            System.out.println("\nEspaço Therapy Kids");
            System.out.println("\n1 - Cadastrar");
            System.out.println("2 - Listar Todos Pacientes");
            System.out.println("3 - Pesquisar Paciente por ID");
            System.out.println("4 - Atualizar Dados de Paciente");
            System.out.println("5 - Remover Paciente");
            System.out.println("6 - Atualizar Arquivo de Lista");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha uma Opção: "); opc = sc.nextInt();
            switch (opc) {
                case 1: CadastroPacienteView.main(null); break;
                case 2: ListagemPacienteView.main(null); break;
                case 3: PesquisarPacientePorIdView.main(null); break;
                case 4: AtualizacaoPacienteView.main(null); break;
                case 5: RemoverPacienteView.main(null); break;
                case 6: GerenciarArquivoView.iniciar(); break;
                case 0: System.out.println("Saindo..."); break;
                default: System.out.println("Opção inválida.");
            }
        } while (opc != 0);
    }
}
