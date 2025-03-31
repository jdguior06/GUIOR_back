package com.sistema.pos.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;

    public void enviarFacturaPorCorreo(String destinatario, byte[] archivoPDF, String nombreArchivo) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true); // "true" habilita multipart para adjuntos

        helper.setTo(destinatario);
        helper.setSubject("Factura de Venta");
        helper.setText("Adjuntamos la factura correspondiente a su compra.");
        helper.addAttachment(nombreArchivo, new ByteArrayResource(archivoPDF));

        mailSender.send(mensaje);
    }
    
    public void enviarCorreoBienvenida(String destinatario, String nombre, String password) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destinatario);
        helper.setSubject("¡Bienvenido a Cafetería PAYEJALI!");

        String contenidoHtml = "<html>" +
            "<head>" +
            "    <style>" +
            "        body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f4; padding: 20px; }" +
            "        .container { background: white; padding: 20px; border-radius: 10px; box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1); }" +
            "        h3 { color: #8B0000; }" +
            "        .credentials { background: #eee; padding: 10px; border-radius: 5px; text-align: left; display: inline-block; }" +
            "        .footer { font-size: 12px; color: #555; margin-top: 20px; }" +
            "    </style>" +
            "</head>" +
            "<body>" +
            "    <div class='container'>" +
            "        <h3>¡Hola, " + nombre + "!</h3>" +
            "        <p>Es un placer darte la bienvenida a <b>Cafetería PAYEJALI</b>. Nos alegra que formes parte de nuestra comunidad.</p>" +
            "        <p>Tu cuenta ha sido creada exitosamente y ahora tienes acceso a nuestra plataforma de gestión.</p>" +
            "        <p>Aquí están tus credenciales de acceso:</p>" +
            "        <div class='credentials'>" +
            "            <p><b>Usuario:</b> " + destinatario + "</p>" +
            "            <p><b>Contraseña:</b> " + password + "</p>" +
            "        </div>" +
            "        <p><i>Por razones de seguridad, te recomendamos cambiar tu contraseña al ingresar.</i></p>" +
            "        <p>Puedes acceder a nuestra plataforma a través del siguiente enlace:</p>" +
            "        <p><a href='https://guior.vercel.app/' target='_blank' style='color: #8B0000; font-weight: bold;'>Acceder a PAYEJALI</a></p>" +
            "        <div class='footer'>" +
            "            <p>Si tienes alguna duda o inconveniente, no dudes en contactarnos.</p>" +
            "            <p>&copy; 2024 Cafetería PAYEJALI. Todos los derechos reservados.</p>" +
            "        </div>" +
            "    </div>" +
            "</body>" +
            "</html>";

        helper.setText(contenidoHtml, true);

        mailSender.send(mensaje);
    }

    
    public void enviarCorreoCambioContrasena(String destinatario, String nombre) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destinatario);
        helper.setSubject("Confirmación de Cambio de Contraseña - PAYEJALI");

        String contenidoHtml = "<html>" +
            "<head>" +
            "    <style>" +
            "        body { font-family: Arial, sans-serif; text-align: center; background-color: #f4f4f4; padding: 20px; }" +
            "        .container { background: white; padding: 20px; border-radius: 10px; box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1); }" +
            "        h3 { color: #8B0000; }" +
            "        p { color: #333; font-size: 16px; }" +
            "        .footer { font-size: 12px; color: #555; margin-top: 20px; }" +
            "    </style>" +
            "</head>" +
            "<body>" +
            "    <div class='container'>" +
            "        <h3>¡Hola, " + nombre + "!</h3>" +
            "        <p>Queremos informarte que tu contraseña ha sido cambiada exitosamente.</p>" +
            "        <p>Si no realizaste este cambio, por favor contacta de inmediato a nuestro equipo de soporte.</p>" +
            "        <p><b>Soporte:</b> j.d.guior010602@gmail.com</p>" +
            "        <p>Si necesitas ingresar nuevamente a nuestra plataforma, puedes hacerlo aquí:</p>" +
            "        <p><a href='https://guior.vercel.app/' target='_blank' style='color: #8B0000; font-weight: bold;'>Acceder a PAYEJALI</a></p>" +
            "        <div class='footer'>" +
            "            <p>Si tienes alguna duda o inconveniente, no dudes en contactarnos.</p>" +
            "            <p>&copy; 2024 Cafetería PAYEJALI. Todos los derechos reservados.</p>" +
            "        </div>" +
            "    </div>" +
            "</body>" +
            "</html>";

        helper.setText(contenidoHtml, true);

        mailSender.send(mensaje);
    }


}
