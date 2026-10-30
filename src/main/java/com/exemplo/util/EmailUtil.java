package com.exemplo.util;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailUtil {

    private static final String REMETENTE;
    private static final String SENHA_APP;

    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        String remetente = System.getenv("EMAIL_REMETENTE");
        String senha = System.getenv("EMAIL_SENHA_APP");

        if (remetente == null || remetente.isEmpty()) {
            remetente = dotenv.get("EMAIL_REMETENTE");
        }
        if (senha == null || senha.isEmpty()) {
            senha = dotenv.get("EMAIL_SENHA_APP");
        }

        REMETENTE = remetente;
        SENHA_APP = senha;
    }

    public static boolean configurado() {
        return REMETENTE != null && !REMETENTE.isEmpty() && SENHA_APP != null && !SENHA_APP.isEmpty();
    }

    public static void enviarCodigo(String para, String nome, String codigo, String motivo, int minutos, String caminhoImagem)
            throws MessagingException, UnsupportedEncodingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session sessao = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE, SENHA_APP);
            }
        });

        MimeBodyPart texto = new MimeBodyPart();
        texto.setText(montarTexto(nome, codigo, motivo, minutos), "UTF-8");

        MimeBodyPart html = new MimeBodyPart();
        html.setContent(montarHtml(nome, codigo, motivo, minutos), "text/html; charset=UTF-8");

        MimeBodyPart imagem = new MimeBodyPart();
        imagem.setDataHandler(new DataHandler(new FileDataSource(caminhoImagem)));
        imagem.setHeader("Content-ID", "<banner>");
        imagem.setDisposition(MimeBodyPart.INLINE);

        MimeMultipart htmlComImagem = new MimeMultipart("related");
        htmlComImagem.addBodyPart(html);
        htmlComImagem.addBodyPart(imagem);

        MimeBodyPart parteHtml = new MimeBodyPart();
        parteHtml.setContent(htmlComImagem);

        MimeMultipart partes = new MimeMultipart("alternative");
        partes.addBodyPart(texto);
        partes.addBodyPart(parteHtml);

        MimeMessage mensagem = new MimeMessage(sessao);
        mensagem.setFrom(new InternetAddress(REMETENTE, "Venus"));
        mensagem.setReplyTo(InternetAddress.parse(REMETENTE));
        mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
        mensagem.setSubject(codigo + " é o seu código do Venus", "UTF-8");
        mensagem.setContent(partes);

        Transport.send(mensagem);
    }

    private static String montarTexto(String nome, String codigo, String motivo, int minutos) {
        return "Olá, " + nome.trim().split(" ")[0] + "!\n\n"
                + motivo + "\n\n"
                + "Seu código: " + codigo + "\n\n"
                + "O código vale por " + minutos + " minutos. Não mostre para ninguém.\n"
                + "Se não foi você, ignore este e-mail e troque a sua senha.\n\n"
                + "Venus - Escaneie. Analise. Escolha com confiança.\n"
                + "Instagram: https://www.instagram.com/venus.jef/\n";
    }

    private static String montarHtml(String nome, String codigo, String motivo, int minutos) {
        String primeiroNome = nome.trim().split(" ")[0]
                .replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");

        return "<!DOCTYPE html><html lang=\"pt-BR\"><head><meta charset=\"UTF-8\"></head><body style=\"margin:0;padding:0;background:#f4effc;\">"
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background:#f4effc;padding:32px 12px;\"><tr><td align=\"center\">"
                + "<table role=\"presentation\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width:600px;width:100%;background:#ffffff;border-radius:20px;overflow:hidden;font-family:Arial,Helvetica,sans-serif;\">"
                + "<tr><td><img src=\"cid:banner\" width=\"600\" alt=\"Venus - código de segurança\" style=\"display:block;width:100%;height:auto;border:0;\"></td></tr>"
                + "<tr><td style=\"padding:40px 44px 8px;color:#30135a;\">"
                + "<h1 style=\"margin:0 0 16px;font-size:26px;line-height:1.3;\">Olá, " + primeiroNome + "!</h1>"
                + "<p style=\"margin:0 0 16px;font-size:15px;line-height:1.7;color:#5b4a70;\">" + motivo + "</p>"
                + "</td></tr>"
                + "<tr><td align=\"center\" style=\"padding:16px 44px 28px;\">"
                + "<div style=\"display:inline-block;background:#f4effc;border:2px dashed #b794d5;border-radius:16px;padding:18px 34px;"
                + "font-family:'Courier New',monospace;font-size:38px;font-weight:bold;letter-spacing:12px;color:#6a28c1;\">" + codigo + "</div>"
                + "</td></tr>"
                + "<tr><td style=\"padding:0 44px 32px;\">"
                + "<p style=\"margin:0;padding:14px 18px;background:#f4effc;border-radius:12px;font-size:13px;line-height:1.6;color:#68429c;\">"
                + "&#9201; O código vale por <b>" + minutos + " minutos</b>. Não mostre para ninguém.</p>"
                + "<p style=\"margin:20px 0 0;font-size:13px;line-height:1.6;color:#8b789f;\">Se não foi você, ignore este e-mail e troque a sua senha.</p>"
                + "</td></tr>"
                + "<tr><td style=\"padding:20px 44px;background:#32135b;color:#e9dcff;font-size:12px;text-align:center;\">"
                + "Venus &middot; Escaneie. Analise. Escolha com confiança.<br>"
                + "<a href=\"https://www.instagram.com/venus.jef/\" style=\"color:#e9dcff;\">Instagram @venus.jef</a></td></tr>"
                + "</table></td></tr></table></body></html>";
    }
}
