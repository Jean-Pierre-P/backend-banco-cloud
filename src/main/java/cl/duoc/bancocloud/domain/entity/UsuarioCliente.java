package cl.duoc.bancocloud.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "USUARIOS_CLIENTES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean accesoAutorizado;
}