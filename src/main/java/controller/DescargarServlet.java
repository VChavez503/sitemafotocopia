package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;

@WebServlet(name = "DescargarServlet", urlPatterns = {"/descargar"})
public class DescargarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String rutaRelativa = request.getParameter("ruta"); // ej: uploads/UUID_archivo.pdf

        if (rutaRelativa == null || rutaRelativa.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Falta parámetro 'ruta'");
            return;
        }

        // Nombre del archivo (UUID_archivo.pdf) aunque venga "uploads/UUID_archivo.pdf"
        String nombreArchivo = new File(rutaRelativa).getName();

        // Usar la MISMA lógica que ArchivoServlet
        String rutaBase = getServletContext().getRealPath("/uploads");
        if (rutaBase == null) {
            rutaBase = System.getProperty("user.dir") + File.separator + "uploads";
        }

        File archivo = new File(rutaBase, nombreArchivo);

        System.out.println("[DescargarServlet] rutaRelativa = " + rutaRelativa);
        System.out.println("[DescargarServlet] rutaBase    = " + rutaBase);
        System.out.println("[DescargarServlet] archivo     = " + archivo.getAbsolutePath());

        if (!archivo.exists() || !archivo.isFile()) {
            System.out.println("[DescargarServlet] Archivo NO encontrado en disco");
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado");
            return;
        }

        // Tipo MIME
        String mime = getServletContext().getMimeType(archivo.getName());
        if (mime == null) {
            mime = "application/octet-stream";
        }
        response.setContentType(mime);

        // Forzar descarga
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + archivo.getName() + "\"");

        try (FileInputStream fis = new FileInputStream(archivo);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesLeidos;
            while ((bytesLeidos = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesLeidos);
            }
        }
    }
}
