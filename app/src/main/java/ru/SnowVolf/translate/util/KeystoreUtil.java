package ru.SnowVolf.translate.util;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.zip.CRC32;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Snow Volf on 15.07.2017, 14:48
 */

public class KeystoreUtil
{
    private static final int MAGIC = -1395514454;
    private static final String FILE_NAME = "FileName";
    private static final String KEYSTORE_PASSWORD = "KeystorePassword";
    private static final String ALIAS_NAME = "AliasName";
    private static final String ALIAS_PASSWORD = "AliasPassword";
    private static final String KEY_PASSWORD = "KeyPassword";

    public static void main(final String[] args) {
        try {
            final File dir = new File(System.getProperty("java.class.path")).getParentFile();
            final File ini = new File(dir, "key.ini");
            final HashMap<String, String> map = new HashMap<>();
            readIni(map, ini);
            if (!map.containsKey("FileName")) {
                throw new NullPointerException("FileName not found");
            }
            if (!map.containsKey("KeystorePassword")) {
                throw new NullPointerException("KeystorePassword not found");
            }
            if (!map.containsKey("AliasName")) {
                throw new NullPointerException("AliasName not found");
            }
            if (!map.containsKey("AliasPassword")) {
                throw new NullPointerException("AliasPassword not found");
            }
            final String name = map.get("FileName");
            final String password = map.get("KeystorePassword");
            final String alias = map.get("AliasName");
            final String aliasPass = map.get("AliasPassword");
            final File file = new File(dir, name);
            final File outDir = new File(dir, "keys");
            outDir.mkdirs();
            final KeyStore keyStore = loadKeyStore(file, password);
            System.err.println("Output:");
            if (map.containsKey("KeyPassword") && map.get("KeyPassword").length() > 0) {
                final String keyPass = map.get("KeyPassword");
                final File out = new File(outDir, getName(file.getName()) + ".aes");
                encryptSplit(keyStore, out, keyPass, alias, aliasPass);
            }
            else {
                split(keyStore, getName(file.getName()), outDir, alias, aliasPass);
            }
            System.err.println();
            System.out.println("succeed.");
        }
        catch (Exception e) {
            System.err.println();
            e.printStackTrace();
        }
        try {
            System.in.read();
        }
        catch (IOException ex) {}
    }

    private static String getName(final String string) {
        final int i = string.lastIndexOf(46);
        if (i == -1) {
            return string;
        }
        return string.substring(0, i);
    }

    private static void readIni(final HashMap<String, String> map, final File ini) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(ini));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("^#.*$", "").replaceAll("^//.*$", "").replaceAll("[ \\t]+$", "");
                final int separator = line.indexOf(61);
                if (separator != -1) {
                    final String head = line.substring(0, separator).trim();
                    final String body = line.substring(separator + 1).trim();
                    map.put(head, body);
                }
            }
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static KeyStore loadKeyStore(final File file, final String passWord) throws Exception {
        final KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(file), passWord.toCharArray());
        return keyStore;
    }

    private static void split(final KeyStore keyStore, final String name, final File dir, final String alias, final String passWord) throws Exception {
        final Certificate pubkey = keyStore.getCertificate(alias);
        final Key key = keyStore.getKey(alias, passWord.toCharArray());
        final KeyPair kp = new KeyPair(pubkey.getPublicKey(), (PrivateKey)key);
        final FileOutputStream m_fos_x509 = new FileOutputStream(new File(dir, name + ".x509.pem"));
        m_fos_x509.write("-----BEGIN CERTIFICATE-----\n".getBytes());
        m_fos_x509.write(Base64.encode(pubkey.getEncoded(), Base64.NO_WRAP));
        m_fos_x509.write("\n-----END CERTIFICATE-----".getBytes());
        m_fos_x509.close();
        System.out.println("-> keys/" + name + ".x509.pem");
        final FileOutputStream m_fos_pk8 = new FileOutputStream(new File(dir, name + ".pk8"));
        m_fos_pk8.write(kp.getPrivate().getEncoded());
        m_fos_pk8.close();
        System.out.println("-> keys/" + name + ".pk8");
    }

    private static void encryptSplit(final KeyStore keyStore, final File outFile, final String filePassWord, final String alias, final String passWord) throws Exception {
        final Certificate pubkey = keyStore.getCertificate(alias);
        final Key key = keyStore.getKey(alias, passWord.toCharArray());
        final KeyPair kp = new KeyPair(pubkey.getPublicKey(), (PrivateKey)key);
        DataOutputStream dos = null;
        FileOutputStream fos = null;
        final CRC32 crc32 = new CRC32();
        try {
            fos = new FileOutputStream(outFile);
            dos = new DataOutputStream(fos);
            dos.writeInt(-1395514454);
            byte[] data = ("-----BEGIN CERTIFICATE-----\n" + Base64.encodeToString(pubkey.getEncoded(), Base64.NO_WRAP) + "\n-----END CERTIFICATE-----").getBytes();
            crc32.update(data);
            data = encrypt(data, filePassWord);
            dos.writeInt(data.length);
            dos.write(data);
            data = kp.getPrivate().getEncoded();
            crc32.update(data);
            data = encrypt(data, filePassWord);
            dos.writeInt(data.length);
            dos.write(data);
            dos.writeLong(crc32.getValue());
            dos.flush();
            System.out.println("-> keys/" + outFile.getName());
        }
        finally {
            try {
                if (dos != null) {
                    dos.close();
                }
            }
            catch (IOException ex) {}
            try {
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ex2) {}
        }
    }

    private static byte[] encrypt(final byte[] data, final String password) throws Exception {
        final MessageDigest mdInst = MessageDigest.getInstance("MD5");
        final byte[] pass = new byte[16];
        byte[] md5 = mdInst.digest(password.getBytes());
        System.arraycopy(md5, 0, pass, 0, 16);
        final SecretKeySpec key = new SecretKeySpec(pass, "AES");
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        md5 = mdInst.digest(md5);
        System.arraycopy(md5, 0, pass, 0, 16);
        cipher.init(1, key, new IvParameterSpec(pass));
        return cipher.doFinal(data);
    }
}

