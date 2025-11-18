package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UsuarioDAO;
import model.Rol;
import model.Usuario;

import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    // 👉 3 = CLIENTE (asegúrate que exista en la tabla roles con id 3)
    private static final int ROL_POR_DEFECTO_ID = 3;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "login";

        switch (accion) {
            case "registro":
                // Mostrar formulario de registro
                request.getRequestDispatcher("registro.jsp").forward(request, response);
                break;

            case "logout":
                // Cerrar sesión y mandar al login
                HttpSession sesion = request.getSession(false);
                if (sesion != null) {
                    sesion.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/auth?accion=login");
                break;

            default:
                // Mostrar formulario de login
                request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) accion = "login";

        if ("registro".equals(accion)) {
            procesarRegistro(request, response);
        } else {
            procesarLogin(request, response);
        }
    }

    private void procesarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("usuario");
        String pass = request.getParameter("contrasena");

        System.out.println("LOGIN - usuario=" + user + ", pass=" + pass);

        Usuario u = usuarioDAO.login(user, pass);

        if (u != null) {
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuarioLogueado", u);

            // ✅ Redirección correcta usando el contexto del proyecto
            response.sendRedirect(request.getContextPath() + "/home.jsp");
        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void procesarRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String usuario = request.getParameter("usuario");
        String contrasena = request.getParameter("contrasena");

        System.out.println("REGISTRO - Datos recibidos:");
        System.out.println("nombre = " + nombre);
        System.out.println("correo = " + correo);
        System.out.println("usuario = " + usuario);
        System.out.println("contrasena = " + contrasena);

        // Rol por defecto = CLIENTE (id = 3)
        Rol rolCliente = new Rol();
        rolCliente.setId(ROL_POR_DEFECTO_ID);

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setUsuario(usuario);
        u.setContrasena(contrasena);
        u.setRol(rolCliente);
        u.setActivo(true);

        boolean ok = usuarioDAO.registrar(u);

        if (ok) {
            request.setAttribute("mensaje", "Cuenta creada correctamente. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "No se pudo crear la cuenta.");
            request.getRequestDispatcher("registro.jsp").forward(request, response);
        }
    }
}
