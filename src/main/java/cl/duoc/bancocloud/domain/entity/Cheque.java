package cl.duoc.bancocloud.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CHEQUES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cheque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String rutCliente;

    @Column(nullable = false)
    private String beneficiario;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fechaEmision;

    @Column(nullable = false)
    private String estado; // EMITIDO, COBRADO, ANULADO, DEVUELTO

    private String motivoDevolucion;
}