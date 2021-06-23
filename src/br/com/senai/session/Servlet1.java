package br.com.senai.session;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import br.com.senai.modelo.Funcionario;
import br.com.senai.modelo.Vendas;



/**
 * Servlet implementation class Servlet1
 */
@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	PrintWriter out = response.getWriter();
	String userid = request.getParameter("userid");
	String password = request.getParameter("password");
	
	String salt="DGE$5SGr@3VsHYUMas2323E4d57vfBfFSTRU@!DSH(*%FDSdfg13sgfsg";
	String senhaSalt = password+salt;
	String senhaSha1SemSal = DigestUtils.shaHex(senhaSalt);
	
	
	HttpSession session = request.getSession();
	boolean flag = false;
	Funcionario funcionario = new Funcionario();
	Vendas vendas = new Vendas();
	
	
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		Connection con = java.sql.DriverManager.getConnection("jdbc:mysql://localhost/padaria?"
		+"user=root&password=root&useTimezone=true&serverTimezone=UTC");	
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("select * from funcionario");
		
		
		

		
		while(rs.next()) {
			if(userid.equals(rs.getString(3)) && senhaSha1SemSal.equals(rs.getString(4))) {
				session.setAttribute("idFuncionario", userid);
				String nome = rs.getString("nome");
				int iduser = rs.getInt("idFuncionario");
				int cargo = rs.getInt("idCargo");

				
				session.setAttribute("cargo", cargo);
				session.setAttribute("id", iduser);
				session.setAttribute("nome", nome);	
				
				
				funcionario.setCargo(cargo);
				funcionario.setNome(nome);
				funcionario.setId(iduser);
				vendas.setIdFuncionario(iduser);
				
				flag = true;
				response.sendRedirect("Servlet2");

			}
			
		}
		
		if(flag==false) {
			response.sendRedirect("index.html");

		}
		
		
	}catch(Exception p){
		out.print(p);
	}
		
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("Servlet2").forward(request, response);
		doGet(request, response);
	}

}
