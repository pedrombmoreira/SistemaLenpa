package com.projeto.sistema_lenpa.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.projeto.sistema_lenpa.model.entrega.Entrega;
import com.projeto.sistema_lenpa.model.entrega.RelatorioEntregasDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

    public void gerarPdfRelatorioEntregas(HttpServletResponse response, RelatorioEntregasDTO relatorio) throws IOException {
        Document document = new Document(PageSize.A4.rotate()); // Página deitada para caber a tabela
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Relatório de Entregas", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Linha em branco

        // Tabela com 7 colunas
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);

        // Primeiro, definimos uma fonte para o cabeçalho
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12); // Fonte Helvetica, Negrito, Tamanho 12

        // Criamos uma célula para cada cabeçalho, aplicando a fonte
        String[] headers = {"Comprador","Associado CESPOL", "Planta", "Quantidade", "Data", "Destino", "Valor (R$)"};

        for (String headerTitle : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(headerTitle, headerFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Centraliza o texto do cabeçalho
            headerCell.setPadding(5); // Adiciona um pequeno espaçamento interno
            table.addCell(headerCell);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Locale brLocale = new Locale("pt", "BR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(brLocale);

        // Dados
        for (Entrega entrega : relatorio.entregas()) {
            table.addCell(entrega.getComprador().getNome());
            String associadoStr = (entrega.getComprador().getAssociado_cespol() != null && entrega.getComprador().getAssociado_cespol())
                    ? "Sim" : "Não";
            table.addCell(associadoStr);
            table.addCell(entrega.getPlanta().getNome_popular());
            table.addCell(String.valueOf(entrega.getQuantidade_mudas()));
            table.addCell(entrega.getDataEntrega().format(formatter));
            table.addCell(entrega.getDestino());

            String valorFormatado;
            // Verifica se o valor é nulo ou igual/menor que zero
            if (entrega.getValor() == null || entrega.getValor().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                valorFormatado = "Doação";
            } else {
                // Senão, formata como moeda
                valorFormatado = currencyFormatter.format(entrega.getValor());
            }
            table.addCell(valorFormatado);
        }

        document.add(table);

        // Adiciona o valor total no final
        Paragraph total = new Paragraph("Valor Total das Vendas: " + currencyFormatter.format(relatorio.valorTotalVendas()), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14));
        total.setAlignment(Element.ALIGN_RIGHT);
        document.add(new Paragraph(" "));
        document.add(total);

        document.close();
    }
}
