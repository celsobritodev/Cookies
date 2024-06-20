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

	// private static final String COOKIE_NAME = "COOKIE_001";
	private static final String COOKIE_NAME = "COOKIE_002";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie myCookieProdutos = null;
		Cookie cookies[] = request.getCookies(); // devolve array de cookies
		PrintWriter pw = response.getWriter();

		if (cookies != null)
			for (Cookie cookieAtual : cookies) {
				String sCookieName = cookieAtual.getName();
				if (sCookieName.equals(COOKIE_NAME)) // é o meu cookie?
					myCookieProdutos = cookieAtual;
			}
		if (myCookieProdutos != null) {
			if (!myCookieProdutos.getValue().isEmpty())
				pw.append("PRODUTOS: " + myCookieProdutos.getValue());
			else
				pw.append("PRODUTOS: O CARRINHO ESTÁ VAZIO!");
		} else 
			pw.append("PRODUTOS: CARRINHO NÃO ENCONTRADO!");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie myCookieProdutos = null;
		Cookie cookies[] = request.getCookies();
		String produto = request.getParameter("produto");
		PrintWriter pw = response.getWriter();

		if (cookies != null)
			for (Cookie cookieAtual : cookies) {
				String sCookieName = cookieAtual.getName();
				if (sCookieName.equals(COOKIE_NAME))
					myCookieProdutos = cookieAtual;
			}
		Cookie novoCookie = null;
		if (produto != null) {
			if (myCookieProdutos != null) {
				String sItensNoCarrinho = myCookieProdutos.getValue();
				novoCookie = new Cookie(COOKIE_NAME, sItensNoCarrinho + "/" + produto);
			} else {
				novoCookie = new Cookie(COOKIE_NAME, produto);
			}
			response.addCookie(novoCookie);
			pw.append("PRODUTO ADICIONADO!");
			response.setStatus(201);
		} else {
			response.setStatus(400);
			pw.append("Produto não enviado");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Cookie myCookieProdutos = null;
		Cookie cookies[] = request.getCookies();
		String produto = request.getParameter("produto");
		PrintWriter pw = response.getWriter();

		if (cookies != null)
			for (Cookie cookieAtual : cookies) {
				String sCookieName = cookieAtual.getName();
				if (sCookieName.equals(COOKIE_NAME))
					myCookieProdutos = cookieAtual;
			}
		if (produto != null && myCookieProdutos != null) {
			String sItensNoCarrinho = myCookieProdutos.getValue(); // pega itens do carrinho
			String antigaListaProdutos[] = sItensNoCarrinho.split("/");
			List<String> novaListaProdutos = new ArrayList<String>();

			int iQtdAntigaProdutos = 0;
			for (String produtoAtual : antigaListaProdutos) {
				iQtdAntigaProdutos++;
				if (!produtoAtual.toUpperCase().equals(produto.toUpperCase()))
					novaListaProdutos.add(produtoAtual);
			}
			int iQtdNovaProdutos = novaListaProdutos.size();
			if (iQtdAntigaProdutos != iQtdNovaProdutos) {
				sItensNoCarrinho = String.join("/", novaListaProdutos);
				Cookie novoCookie = new Cookie(COOKIE_NAME, sItensNoCarrinho);
				response.addCookie(novoCookie);
				pw.append("PRODUTO REMOVIDO!");
			} else {
				pw.append("PRODUTO NAO ENCONTRADO!");
			}
			response.setStatus(200);
		} else {
			response.setStatus(400);
			pw.append("Produto não enviado ou carrinho vazio");
		}
	}

}
