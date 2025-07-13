package br.edu.ifpb.pweb2.finangest.model;

import java.io.Serializable;
// import java.util.List;
// import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Correntista implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;

    private String email;

    private String senha;

    private boolean admin;

    // private List<Conta> contas;


    // @Override
    // public String toString() {
    //     return "Usuario{" +
    //             "id=" + id +
    //             ", nome='" + nome + '\'' +
    //             ", email='" + email + '\'' +
    //             ", tipo='" + admin + '\'' +
    //             ", contas=" + contas +
    //             '}';
    // }

    // public Integer getId() {
    //     return id;
    // }

    // public void setId(Integer id) {
    //     this.id = id;
    // }

    // public String getNome() {
    //     return nome;
    // }

    // public void setNome(String nome) {
    //     this.nome = nome;
    // }

    // public String getEmail() {
    //     return email;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }

    // public String getSenha() {
    //     return senha;
    // }

    // public void setSenha(String senha) {
    //     this.senha = senha;
    // }

    // public boolean isAdmin() {
    //     return admin;
    // }

    // public void setTipo(boolean admin) {
    //     this.admin = admin;
    // }

    // public List<Conta> getContas() {
    //     return contas;
    // }

    // public void setContas(List<Conta> contas) {
    //     this.contas = contas;
    // }
}