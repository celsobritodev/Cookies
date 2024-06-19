package geral;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CarrinhoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String COOKIE_NAME = "produtos";
	//private static final String COOKIE_NAME = "produtos1";


	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie cookieProdutos = null;
		Cookie cookies[] = request.getCookies();
		PrintWriter pw = response.getWriter();

		if (cookies != null)
			for (Cookie ci : cookies) {
				String sCookieName=ci.getName(); 
				if (sCookieName.equals(COOKIE_NAME))
					cookieProdutos = ci;
			}
		if (cookieProdutos != null)
			if(!cookieProdutos.getValue().isEmpty()) 
  		      pw.append("PRODUTOS: " + cookieProdutos.getValue());
			else
			  pw.append("PRODUTOS: NÃO HÁ NENHUM PRODUTO!");

	}
	

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie cookieProdutos = null;
		Cookie cookies[] = request.getCookies();
		String produto = request.getParameter("produto");
		PrintWriter pw = response.getWriter();

		if (cookies != null)
			for (Cookie ci : cookies) {
				String sCookieName=ci.getName(); 
				if (sCookieName.equals(COOKIE_NAME))
					cookieProdutos = ci;
			}
		Cookie cp = null;
		if (produto != null) {
			if (cookieProdutos != null) {
				cp = new Cookie(COOKIE_NAME, cookieProdutos.getValue() + "/" + produto);
			} else {
				cp = new Cookie(COOKIE_NAME, produto);
			}
			response.addCookie(cp);
			response.setStatus(201);
		} else {
			response.setStatus(400);
			pw.append("Produto não enviado");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie cookieProdutos = null;
		Cookie cookies[] = request.getCookies();
		String produto = request.getParameter("produto");
		PrintWriter pw = response.getWriter();

		if (cookies != null)
			for (Cookie ci : cookies) {
				String sCookieName=ci.getName(); 
				if (sCookieName.equals(COOKIE_NAME))
					cookieProdutos = ci;
			}  
		if (produto != null && cookieProdutos != null) {
			String strProds = cookieProdutos.getValue();
			String prods[] = strProds.split("/");
			List<String> lp = new ArrayList<String>();

			for (String p : prods)
				if (!p.toUpperCase().equals(produto.toUpperCase()))
					lp.add(p);

			strProds = String.join("/", lp);
			Cookie cp = new Cookie(COOKIE_NAME, strProds);
			response.addCookie(cp);
			response.setStatus(200);
		} else {
			response.setStatus(400);
			pw.append("Produto não enviado ou carrinho vazio");
		}
	}

}
