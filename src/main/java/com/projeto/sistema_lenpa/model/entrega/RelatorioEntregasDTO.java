package com.projeto.sistema_lenpa.model.entrega;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioEntregasDTO(List<Entrega> entregas,
                                  BigDecimal valorTotalVendas)
{}
