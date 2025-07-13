package br.edu.ifpb.pweb2.finangest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.ifpb.pweb2.finangest.Util.PasswordUtil;

@SpringBootApplication
public class FinangestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinangestApplication.class, args);
		String senha = "123";
		System.out.println(PasswordUtil.hashPassword(senha));
	}

}
