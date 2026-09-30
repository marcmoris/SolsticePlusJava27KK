package org.compiere.util;
// EmailMicrosoftGraphApi  -  Java 8 compatible

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import java.util.Base64;
import org.compiere.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;

public final class EmailMicrosoftGraphApi implements Serializable
{
    private static final long serialVersionUID = 2L;

    // Azure AD / Graph config
    private String m_tenantId;
    private String m_clientId;
    private String m_clientSecret;

    private static final String GRAPH_API_BASE    = "https://graph.microsoft.com/v1.0";
    private static final String TOKEN_URL_TEMPLATE =
            "https://login.microsoftonline.com/%s/oauth2/v2.0/token";

    // Champs email
    private String            m_from;
    private ArrayList<String> m_to  = new ArrayList<String>();
    private ArrayList<String> m_cc  = new ArrayList<String>();
    private ArrayList<String> m_bcc = new ArrayList<String>();
    private String            m_replyTo;
    private String            m_subject;
    private String            m_messageText;
    private String            m_messageHTML;
    private ArrayList<Object> m_attachments;
    private Properties        m_ctx;
    private boolean           m_valid   = false;
    private String            m_sentMsg = null;

    public static final String SENT_OK = "OK";
    protected static CLogger log = CLogger.getCLogger(EMail.class);

    // =========================================================
    // Constructeurs
    // =========================================================

    /** Constructeur via MClient */
    public EmailMicrosoftGraphApi(MClient client, String from, String to,
                 String subject, String message)
    {
        this(client.getCtx(),
             client.getMSGraphTenantId(),
             client.getMSGraphClientId(),
             client.getMSGraphClientSecret(),
             from, to, subject, message);
    }

    /** Constructeur complet */
    public EmailMicrosoftGraphApi(Properties ctx,
                 String tenantId, String clientId, String clientSecret,
                 String from, String to, String subject, String message)
    {
        m_ctx = ctx;
        m_tenantId = tenantId; m_clientId = clientId; m_clientSecret = clientSecret;
        setFrom(from); addTo(to);
        setSubject(subject == null || subject.isEmpty() ? "." : subject);
        if (message != null && !message.isEmpty()) setMessageText(message);
        m_valid = isValid(true);
    }

    // =========================================================
    // send()  -  utilise HttpURLConnection (Java 8)
    // =========================================================

    public String send()
    {
        log.info("(Graph API) " + m_from + " -> " + m_to);
        m_sentMsg = null;
        if (!isValid(true)) { m_sentMsg = "Invalid Data"; return m_sentMsg; }
        try
        {
            String accessToken = getAccessToken();
            String jsonPayload = buildMessagePayload();

            String endpoint = GRAPH_API_BASE + "/users/"
                    + URLEncoder.encode(m_from, "UTF-8") + "/sendMail";

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            byte[] payloadBytes = jsonPayload.getBytes("UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(payloadBytes.length));

            OutputStream os = conn.getOutputStream();
            os.write(payloadBytes);
            os.flush();
            os.close();

            int statusCode = conn.getResponseCode();

            if (statusCode == 202)   // 202 Accepted = succes
            {
                log.fine("success - Graph API sendMail accepted");
                m_sentMsg = SENT_OK;
            }
            else
            {
                String responseBody = readStream(conn.getErrorStream());
                m_sentMsg = "Graph API error " + statusCode + ": " + responseBody;
                log.log(Level.WARNING, m_sentMsg);
            }
            conn.disconnect();
        }
        catch (Exception e)
        {
            log.log(Level.SEVERE, "send()", e);
            m_sentMsg = e.getLocalizedMessage();
        }
        return m_sentMsg;
    }

    // =========================================================
    // OAuth2  -  client credentials flow (Java 8)
    // =========================================================

    private String getAccessToken() throws Exception
    {
        String tokenUrl = String.format(TOKEN_URL_TEMPLATE, m_tenantId);
        String body = "grant_type=client_credentials"
                + "&client_id="     + URLEncoder.encode(m_clientId,     "UTF-8")
                + "&client_secret=" + URLEncoder.encode(m_clientSecret, "UTF-8")
                + "&scope="         + URLEncoder.encode(
                        "https://graph.microsoft.com/.default", "UTF-8");

        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        byte[] bodyBytes = body.getBytes("UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(bodyBytes.length));

        OutputStream os = conn.getOutputStream();
        os.write(bodyBytes);
        os.flush();
        os.close();

        int statusCode = conn.getResponseCode();
        if (statusCode != 200)
        {
            String error = readStream(conn.getErrorStream());
            conn.disconnect();
            throw new Exception("Token failed (" + statusCode + "): " + error);
        }

        String responseBody = readStream(conn.getInputStream());
        conn.disconnect();

        return new ObjectMapper().readTree(responseBody).get("access_token").asText();
    }

    // =========================================================
    // Utilitaire  -  lecture d'un InputStream (remplace readAllBytes)
    // =========================================================

    private String readStream(InputStream is) throws IOException
    {
        if (is == null) return "";
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(chunk)) != -1)
            buffer.write(chunk, 0, bytesRead);
        return buffer.toString("UTF-8");
    }

    private byte[] readStreamBytes(InputStream is) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(chunk)) != -1)
            buffer.write(chunk, 0, bytesRead);
        return buffer.toByteArray();
    }

    // =========================================================
    // Construction du payload JSON Graph API
    // =========================================================

    private String buildMessagePayload() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode   root   = mapper.createObjectNode();
        ObjectNode   msg    = mapper.createObjectNode();

        msg.put("subject", m_subject);

        // Corps du message
        ObjectNode bodyNode = mapper.createObjectNode();
        if (m_messageHTML != null && !m_messageHTML.isEmpty())
        { bodyNode.put("contentType", "HTML"); bodyNode.put("content", m_messageHTML); }
        else
        { bodyNode.put("contentType", "Text"); bodyNode.put("content", m_messageText != null ? m_messageText : ""); }
        msg.set("body", bodyNode);

        // Destinataires
        ArrayNode toArray = mapper.createArrayNode();
        for (String addr : m_to) toArray.add(recipientNode(mapper, addr));
        msg.set("toRecipients", toArray);

        if (!m_cc.isEmpty()) {
            ArrayNode ccArray = mapper.createArrayNode();
            for (String addr : m_cc) ccArray.add(recipientNode(mapper, addr));
            msg.set("ccRecipients", ccArray);
        }
        if (!m_bcc.isEmpty()) {
            ArrayNode bccArray = mapper.createArrayNode();
            for (String addr : m_bcc) bccArray.add(recipientNode(mapper, addr));
            msg.set("bccRecipients", bccArray);
        }
        if (m_replyTo != null && !m_replyTo.isEmpty()) {
            ArrayNode ra = mapper.createArrayNode();
            ra.add(recipientNode(mapper, m_replyTo));
            msg.set("replyTo", ra);
        }

        // Pieces jointes
        if (m_attachments != null && !m_attachments.isEmpty()) {
            ArrayNode aa = mapper.createArrayNode();
            for (Object att : m_attachments) {
                ObjectNode an = buildAttachmentNode(mapper, att);
                if (an != null) aa.add(an);
            }
            msg.set("attachments", aa);
        }

        root.set("message",         msg);
        root.put("saveToSentItems", "true");
        return mapper.writeValueAsString(root);
    }

    private ObjectNode recipientNode(ObjectMapper mapper, String address)
    {
        ObjectNode node = mapper.createObjectNode();
        ObjectNode ea   = mapper.createObjectNode();
        ea.put("address", address);
        node.set("emailAddress", ea);
        return node;
    }

    private ObjectNode buildAttachmentNode(ObjectMapper mapper, Object attachment)
            throws Exception
    {
        byte[] data = null; String fileName = "attachment"; String mimeType = "application/octet-stream";
        if (attachment instanceof File) {
            File f = (File) attachment;
            if (!f.exists()) { log.warning("File not found: " + f); return null; }
            data = Files.readAllBytes(f.toPath()); fileName = f.getName();
            String g = URLConnection.guessContentTypeFromName(fileName); if (g != null) mimeType = g;
        } else if (attachment instanceof URL) {
            URL url = (URL) attachment;
            InputStream is = url.openStream();
            try { data = readStreamBytes(is); } finally { is.close(); }
            fileName = Paths.get(url.getPath()).getFileName().toString();
            String g = URLConnection.guessContentTypeFromName(fileName); if (g != null) mimeType = g;
        } else if (attachment instanceof RawAttachment) {
            RawAttachment ra = (RawAttachment) attachment;
            data = ra.data; fileName = ra.name; mimeType = ra.mimeType;
        } else { log.warning("Unknown attachment type: " + attachment.getClass()); return null; }

        ObjectNode node = mapper.createObjectNode();
        node.put("@odata.type",  "#microsoft.graph.fileAttachment");
        node.put("name",         fileName);
        node.put("contentType",  mimeType);
        node.put("contentBytes", Base64.getEncoder().encodeToString(data));
        return node;
    }

    // =========================================================
    // Inner class  -  remplace ByteArrayDataSource
    // =========================================================

    public static class RawAttachment {
        public final byte[] data; public final String mimeType; public final String name;
        public RawAttachment(byte[] data, String mimeType, String name)
        { this.data = data; this.mimeType = mimeType; this.name = name; }
    }

    // =========================================================
    // Getters/Setters
    // =========================================================

    public String  getSentMsg()          { return m_sentMsg; }
    public boolean isSentOK()            { return m_sentMsg != null && SENT_OK.equals(m_sentMsg); }
    public void    setFrom(String v)     { if (v == null || v.isEmpty()) m_valid = false; else m_from = v.trim(); }
    public String  getFrom()             { return m_from; }
    public boolean addTo(String v)       { if (v == null || v.isEmpty()) { m_valid = false; return false; } m_to.add(v.trim()); return true; }
    public String  getTo()               { return m_to.isEmpty() ? null : m_to.get(0); }
    public boolean addCc(String v)       { if (v == null || v.isEmpty()) return false; m_cc.add(v.trim());  return true; }
    public boolean addBcc(String v)      { if (v == null || v.isEmpty()) return false; m_bcc.add(v.trim()); return true; }
    public boolean setReplyTo(String v)  { if (v == null || v.isEmpty()) return false; m_replyTo = v.trim(); return true; }
    public void    setSubject(String v)  { if (v == null || v.isEmpty()) m_valid = false; else m_subject = v; }
    public String  getSubject()          { return m_subject; }
    public void setMessageText(String v) { if (v == null || v.isEmpty()) m_valid = false; else m_messageText = v.endsWith("\n") ? v : v + "\n"; }
    public void setMessageHTML(String v) { if (v == null || v.isEmpty()) m_valid = false; else m_messageHTML = v; }
    public void setMessageHTML(String subject, String message) {
        m_subject = subject;
        m_messageHTML = "<HTML><HEAD><TITLE>" + subject + "</TITLE></HEAD><BODY><H2>" + subject + "</H2>" + message + "</BODY></HTML>";
    }
    public String  getMessageHTML()      { return m_messageHTML; }
    public void addAttachment(File f)    { if (f == null) return; if (m_attachments == null) m_attachments = new ArrayList<Object>(); m_attachments.add(f); }
    public void addAttachment(URL u)     { if (u == null) return; if (m_attachments == null) m_attachments = new ArrayList<Object>(); m_attachments.add(u); }
    public void addAttachment(byte[] data, String mimeType, String name) { if (m_attachments == null) m_attachments = new ArrayList<Object>(); m_attachments.add(new RawAttachment(data, mimeType, name)); }
    public boolean isValid()             { return m_valid; }
    public boolean isValid(boolean recheck) {
        if (!recheck) return m_valid;
        if (m_from == null || m_from.isEmpty()) { log.warning("From invalid"); return false; }
        if (m_to == null || m_to.isEmpty()) { log.warning("No To"); return false; }
        if (m_tenantId == null || m_tenantId.isEmpty() || m_clientId == null || m_clientId.isEmpty() || m_clientSecret == null || m_clientSecret.isEmpty())
        { log.warning("Missing Graph credentials"); return false; }
        if (m_subject == null || m_subject.isEmpty()) { log.warning("Subject invalid"); return false; }
        return true;
    }
    public void setTenantId(String v)     { m_tenantId     = v; }
    public void setClientId(String v)     { m_clientId     = v; }
    public void setClientSecret(String v) { m_clientSecret = v; }

    @Override
    public String toString() {
        return "EMail[Graph,From:" + m_from + ",To:" + getTo() + ",Subject=" + m_subject + "]";
    }

    public static void main(String[] args) {
        if (args.length != 7) { System.out.println("Parameters: tenantId clientId clientSecret from to subject message"); System.exit(1); }
        EmailMicrosoftGraphApi e = new EmailMicrosoftGraphApi(new Properties(), args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
        System.out.println("Result: " + e.send());
    }
}