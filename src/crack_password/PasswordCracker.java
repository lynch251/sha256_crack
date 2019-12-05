/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crack_password;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Pierre
 */
public class PasswordCracker {

 public static char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789@#&!()^=+/:.;,".toCharArray();
 public static final int PASSWORD_MAX_LENGTH = 5;
 public static final String PASSWORD_SHA_256_TO_FIND = "dc32335fd116be9b832733b77623b00961590421f3ea57618ed4dbdd6816ae84";
 public static final byte[] BYTES_SHA_256_TO_FIND = PasswordCracker.hexStringToByteArray(PASSWORD_SHA_256_TO_FIND);
 public static long START_TIME;

 public static byte[] hexStringToByteArray(String s) {
  int len = s.length();
  byte[] data = new byte[len / 2];
  for (int i = 0; i < len; i += 2) {
   data[i / 2] = (byte)((Character.digit(s.charAt(i), 16) << 4) +
    Character.digit(s.charAt(i + 1), 16));
  }
  return data;
 }

 public static void main(String[] args) throws NoSuchAlgorithmException {
  int cores = Runtime.getRuntime().availableProcessors();
  int lengthSet = PASSWORD_MAX_LENGTH / cores;
  int end = 0;

  START_TIME = System.currentTimeMillis();

  ExecutorService executorService = Executors.newFixedThreadPool(cores);

  while (end < PASSWORD_MAX_LENGTH) {
   int start = end + 1;
   end = start + lengthSet;

   if (end > PASSWORD_MAX_LENGTH)
    end = PASSWORD_MAX_LENGTH;

   executorService.submit(new Cracker(start, end));
  }

  executorService.shutdown();

 }

}
