/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crack_password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Pierre
 */
class Cracker implements Runnable {

 private int start; // maximum and minimum of the length of the passwords to be tested
 private int end;
 private final MessageDigest digest = MessageDigest.getInstance("SHA-256"); // le type de chiffrement du mot de passe à décrypter
 private static boolean DONE = false;
 private String found;  // la chaine de caractère sensée contenir le résultat du crack

 public Cracker(int s, int e) throws NoSuchAlgorithmException {
  start = s;
  end = e;
 }

 public void generate(StringBuilder sb, int n) {
  if (DONE)
   return;

  if (n == sb.length()) {
   String candidate = sb.toString();
   // check password
   byte[] bytes = digest.digest(candidate.getBytes());

   if (Arrays.equals(bytes, PasswordCracker.BYTES_SHA_256_TO_FIND)) {
    found = candidate;
    DONE = true;
   }

   return;
  }

  for (int i = 0; i < PasswordCracker.ALPHABET.length && !DONE; i++) {
   char letter = PasswordCracker.ALPHABET[i];
   sb.setCharAt(n, letter);
   generate(sb, n + 1);
  }

 }

 @Override
 public void run() {

  for (int length = start; length <= end && !DONE; length++) {
   StringBuilder sb = new StringBuilder();
   sb.setLength(length);
   generate(sb, 0);
  }

  if (DONE) {
   long duration = System.currentTimeMillis() - PasswordCracker.START_TIME;
   System.out.println("Password cracked in " + TimeUnit.MILLISECONDS.toSeconds(duration) + "." + TimeUnit.MILLISECONDS.toMillis(duration) + " sec. Password was = " + found);
  } else {
   System.out.println("Password not cracked for subset [" + start + ", " + end + "]");
  }
 }

}
